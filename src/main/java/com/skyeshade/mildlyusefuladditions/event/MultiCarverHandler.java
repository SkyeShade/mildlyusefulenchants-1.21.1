package com.skyeshade.mildlyusefuladditions.event;

import com.mojang.logging.LogUtils;
import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class MultiCarverHandler {
    private static final org.slf4j.Logger LOG = LogUtils.getLogger();
    private static final ThreadLocal<Boolean> REENTRANT = ThreadLocal.withInitial(() -> false);


    private static final ResourceLocation MULTI_CARVER_ID =
            ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "multi_carver");
    private static final ResourceKey<Enchantment> MULTI_CARVER_KEY =
            ResourceKey.create(Registries.ENCHANTMENT, MULTI_CARVER_ID);

    private static final ResourceLocation TUNNELING_ID =
            ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "tunneling");
    private static final ResourceKey<Enchantment> TUNNELING_KEY =
            ResourceKey.create(Registries.ENCHANTMENT, TUNNELING_ID);
    private static final java.util.Map<java.util.UUID, Pending> PENDING = new java.util.HashMap<>();

    private static final class Pending {
        final BlockPos center;
        final Direction face;
        Pending(BlockPos c, Direction f) { center = c; face = f; }
    }


    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (!(e.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;
        if (e.getHand() != InteractionHand.MAIN_HAND) return;


        ItemStack tool = player.getMainHandItem();
        if (tool.isEmpty()) return;

        var enchLookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var multiCarver = enchLookup.get(MULTI_CARVER_KEY).orElse(null);
        if (multiCarver == null) return;

        int levelEnchant = EnchantmentHelper.getItemEnchantmentLevel(multiCarver, tool);
        if (levelEnchant <= 0) return;


        BlockPos center = e.getPos();

        if (!shouldActivate(player, level, center, tool)) return;
        BlockState state = level.getBlockState(center);
        if (!toolMatchesBlock(tool, state)) return;

        Direction face = e.getFace() == null ? player.getDirection() : e.getFace();
        PENDING.put(player.getUUID(), new Pending(center.immutable(), face));
    }
    private static boolean shouldActivate(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack tool) {
        if (player.isShiftKeyDown()) return false;
        BlockState state = level.getBlockState(pos);
        return toolMatchesBlock(tool, state);
    }


    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent e) {
        if (!(e.getPlayer() instanceof ServerPlayer player)) return;
        if (!(e.getLevel() instanceof ServerLevel level)) return;
        if (Boolean.TRUE.equals(REENTRANT.get())) return;

        Pending p = PENDING.get(player.getUUID());
        if (p == null || !p.center.equals(e.getPos())) return;

        ItemStack tool = player.getMainHandItem();
        BlockPos center = e.getPos();

        if (!shouldActivate(player, level, center, tool)) return;
        if (tool.isEmpty()) { PENDING.remove(player.getUUID()); return; }

        var enchLookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var multiCarver = enchLookup.get(MULTI_CARVER_KEY).orElse(null);
        var tunneling = enchLookup.get(TUNNELING_KEY).orElse(null);
        if (multiCarver == null) { PENDING.remove(player.getUUID()); return; }

        int levelEnchant = EnchantmentHelper.getItemEnchantmentLevel(multiCarver, tool);
        if (EnchantmentHelper.getItemEnchantmentLevel(tunneling, tool) > 0) {
            return;
        }
        if (levelEnchant <= 0) { PENDING.remove(player.getUUID()); return; }


        int[][] offsets = offsetsFor(p.face, levelEnchant);

        REENTRANT.set(true);
        try {


            for (int[] off : offsets) {
                int dx = off[0], dy = off[1], dz = off[2];
                if (dx == 0 && dy == 0 && dz == 0) continue;

                BlockPos pos = p.center.offset(dx, dy, dz);


                if (!canActuallyHarvest(level, player, tool, pos)) continue;



                player.gameMode.destroyBlock(pos);
            }
        } finally {
            REENTRANT.set(false);
            PENDING.remove(player.getUUID());
        }
    }




    private static boolean toolMatchesBlock(ItemStack tool, BlockState state) {
        return (tool.is(net.minecraft.tags.ItemTags.PICKAXES) && state.is(BlockTags.MINEABLE_WITH_PICKAXE)) ||
                (tool.is(net.minecraft.tags.ItemTags.AXES)     && state.is(BlockTags.MINEABLE_WITH_AXE))     ||
                (tool.is(net.minecraft.tags.ItemTags.SHOVELS)  && state.is(BlockTags.MINEABLE_WITH_SHOVEL))  ||
                (tool.is(net.minecraft.tags.ItemTags.HOES)     && state.is(BlockTags.MINEABLE_WITH_HOE));
    }


    private static boolean canActuallyHarvest(ServerLevel level, ServerPlayer player, ItemStack tool, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return false;


        if (state.getDestroySpeed(level, pos) < 0f) return false;


        if (!toolMatchesBlock(tool, state)) return false;


        if (!player.isCreative() && !player.hasCorrectToolForDrops(state)) return false;



        return true;
    }





    private static int[][] offsetsFor(Direction face, int depth) {
        depth = Math.max(1, depth);
        int[][] list = new int[9 * depth][3];
        int i = 0;


        Vec3i n = face.getOpposite().getNormal();

        for (int d = 0; d < depth; d++) {
            int ox = n.getX() * d;
            int oy = n.getY() * d;
            int oz = n.getZ() * d;

            switch (face) {
                case UP, DOWN -> { // plane is XZ, depth marches along Y
                    for (int x = -1; x <= 1; x++)
                        for (int z = -1; z <= 1; z++)
                            list[i++] = new int[]{ox + x, oy, oz + z};
                }
                case NORTH, SOUTH -> { // plane is XY, depth marches along Z
                    for (int x = -1; x <= 1; x++)
                        for (int y = -1; y <= 1; y++)
                            list[i++] = new int[]{ox + x, oy + y, oz};
                }
                case WEST, EAST -> { // plane is YZ, depth marches along X
                    for (int y = -1; y <= 1; y++)
                        for (int z = -1; z <= 1; z++)
                            list[i++] = new int[]{ox, oy + y, oz + z};
                }
                default -> { // fallback: treat like UP/DOWN
                    for (int x = -1; x <= 1; x++)
                        for (int z = -1; z <= 1; z++)
                            list[i++] = new int[]{ox + x, oy, oz + z};
                }
            }
        }
        return list;
    }

}
