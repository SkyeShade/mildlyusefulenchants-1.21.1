package com.skyeshade.mildlyusefuladditions.event;

import com.mojang.logging.LogUtils;
import com.skyeshade.mildlyusefuladditions.Config;
import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.util.TickScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class StripTunnelingHandler {
    private static final org.slf4j.Logger LOG = LogUtils.getLogger();
    private static final ThreadLocal<Boolean> REENTRANT = ThreadLocal.withInitial(() -> false);




    private static final ResourceLocation STRIP_TUNNELING_ID =
            ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "strip_tunneling");
    private static final ResourceKey<Enchantment> STRIP_TUNNELING_KEY =
            ResourceKey.create(Registries.ENCHANTMENT, STRIP_TUNNELING_ID);


    private static final ResourceKey<Enchantment> TUNNELING_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "tunneling"));
    private static final ResourceKey<Enchantment> MULTI_CARVER_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "multi_carver"));

    private static final java.util.Map<java.util.UUID, Pending> PENDING = new java.util.HashMap<>();
    private record Pending(BlockPos center, Direction face) {}

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (!(e.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel)) return;
        if (e.getHand() != InteractionHand.MAIN_HAND) return;
        BlockPos center = e.getPos();
        ItemStack tool = player.getMainHandItem();
        if (!shouldActivate(player, ((ServerLevel) player.level()), center, tool)) return;
        Direction face = e.getFace() == null ? player.getDirection() : e.getFace();
        PENDING.put(player.getUUID(), new Pending(e.getPos().immutable(), face));
    }
    private static boolean shouldActivate(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack tool) {
        if (player.isShiftKeyDown()) return false;
        BlockState state = level.getBlockState(pos);
        return canMineThis(player, tool, state);
    }
    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent e) {
        if (!(e.getPlayer() instanceof ServerPlayer player)) return;
        if (!(e.getLevel() instanceof ServerLevel level)) return;
        if (Boolean.TRUE.equals(REENTRANT.get())) return;

        Pending p = PENDING.get(player.getUUID());
        if (p == null || !p.center.equals(e.getPos())) return;

        ItemStack tool = player.getMainHandItem();

        if (tool.isEmpty()) { PENDING.remove(player.getUUID()); return; }

        var lookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);


        var stripTunnel = lookup.get(STRIP_TUNNELING_KEY).orElse(null);
        if (stripTunnel == null) { PENDING.remove(player.getUUID()); return; }
        int stripLvl = EnchantmentHelper.getItemEnchantmentLevel(stripTunnel, tool);
        if (stripLvl <= 0) { PENDING.remove(player.getUUID()); return; }


        var tunneling = lookup.get(TUNNELING_KEY).orElse(null);
        var multi     = lookup.get(MULTI_CARVER_KEY).orElse(null);
        if ((tunneling != null && EnchantmentHelper.getItemEnchantmentLevel(tunneling, tool) > 0) ||
                (multi != null && EnchantmentHelper.getItemEnchantmentLevel(multi, tool) > 0)) {
            PENDING.remove(player.getUUID());
            return;
        }

        int length = Math.max(1, stripLvl * Config.STRIP_LENGTH_PER_LEVEL.get());

        final BlockPos center = p.center();


        if (!shouldActivate(player, ((ServerLevel) player.level()), center, tool)) return;
        final Direction face = p.face();
        final Vec3i n = face.getOpposite().getNormal();

        // schedule 1×2 layers (vertical) from depth 0 .. length
        int idx = 0;
        for (int d = 0; d <= length; d++, idx++) {
            final int depth = d;
            final int delay = idx * Config.STRIP_LAYER_DELAY_TICKS.get();
            TickScheduler.schedule(delay, () -> runGuarded(() ->
                    mineLayer1x2(level, player, center, n, face, depth)
            ));
        }

        PENDING.remove(player.getUUID());
    }

    // ---------- helpers ----------
    private static void runGuarded(Runnable r) {
        if (Boolean.TRUE.equals(REENTRANT.get())) return;
        try {
            REENTRANT.set(true);
            r.run();
        } catch (Throwable t) {
            LOG.error("Strip Tunneling task crashed", t);
        } finally {
            REENTRANT.set(false);
        }
    }


    private static void mineLayer1x2(ServerLevel level, ServerPlayer player, BlockPos center, Vec3i n, Direction face, int depth) {
        int ox = n.getX() * depth, oy = n.getY() * depth, oz = n.getZ() * depth;
        ItemStack tool = player.getMainHandItem();

        BlockPos a = center.offset(ox, oy, oz);
        // make tunnel 2 blocks tall
        BlockPos b = a.below();

        tryBreak(level, player, tool, a);
        tryBreak(level, player, tool, b);
    }

    private static void tryBreak(ServerLevel level, ServerPlayer player, ItemStack tool, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;
        if (state.getDestroySpeed(level, pos) < 0f) return;
        if (!canMineThis(player, tool, state)) return;

        if (!player.isCreative() && !player.hasCorrectToolForDrops(state)) return;
        if (level.getBlockEntity(pos) != null) return;

        final BlockState before = state;
        if (player.gameMode.destroyBlock(pos)) {
            level.levelEvent(2001, pos, Block.getId(before));
            SoundType snd = before.getSoundType(level, pos, player);
            level.playSound(null, pos, snd.getBreakSound(), SoundSource.BLOCKS,
                    (snd.getVolume() + 1.0F) / 2.0F, snd.getPitch() * 0.8F);
        }
    }
    private static boolean canMineThis(ServerPlayer player, ItemStack tool, BlockState state) {
        if (player.isCreative()) return true;

        boolean correctType = toolMatchesBlock(tool, state);
        boolean hasCorrect  = player.hasCorrectToolForDrops(state);
        boolean requires    = state.requiresCorrectToolForDrops();

        if (Config.STRIP_TOOL_CHECK.get()) {

            return correctType && hasCorrect;
        } else {

            return (correctType && hasCorrect) || !requires;
        }
    }

    private static boolean toolMatchesBlock(ItemStack tool, BlockState state) {
        return (tool.is(net.minecraft.tags.ItemTags.PICKAXES) && state.is(BlockTags.MINEABLE_WITH_PICKAXE)) ||
                (tool.is(net.minecraft.tags.ItemTags.AXES)     && state.is(BlockTags.MINEABLE_WITH_AXE))     ||
                (tool.is(net.minecraft.tags.ItemTags.SHOVELS)  && state.is(BlockTags.MINEABLE_WITH_SHOVEL))  ||
                (tool.is(net.minecraft.tags.ItemTags.HOES)     && state.is(BlockTags.MINEABLE_WITH_HOE));
    }
}
