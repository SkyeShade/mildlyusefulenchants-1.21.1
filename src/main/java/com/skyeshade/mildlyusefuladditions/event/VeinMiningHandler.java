package com.skyeshade.mildlyusefuladditions.event;

import com.mojang.logging.LogUtils;
import com.skyeshade.mildlyusefuladditions.Config;
import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.util.TickScheduler;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static com.skyeshade.mildlyusefuladditions.Config.BE_ALLOWED_TAGS;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class VeinMiningHandler {
    private static final Logger LOG = LogUtils.getLogger();
    private static final ThreadLocal<Boolean> REENTRANT = ThreadLocal.withInitial(() -> false);
    private static final int LAYER_DELAY_TICKS = 3;

    private static final ResourceKey<Enchantment> VEIN_MINING_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "vein_mining"));

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent e) {
        if (!(e.getPlayer() instanceof ServerPlayer player)) return;
        if (!(e.getLevel() instanceof ServerLevel level)) return;
        if (Boolean.TRUE.equals(REENTRANT.get())) return;


        if (player.isShiftKeyDown()) return;

        final BlockPos origin = e.getPos();
        final BlockState originState = level.getBlockState(origin);


        final ItemStack tool = player.getMainHandItem();
        if (!isAllowedTarget(originState)) return;
        if (!toolMatchesBlock(tool, originState)) return;
        if (!player.isCreative() && !player.hasCorrectToolForDrops(originState)) return;

        // Enchant present?
        var lookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var vein = lookup.get(VEIN_MINING_KEY).orElse(null);
        if (vein == null) return;
        int lvl = EnchantmentHelper.getItemEnchantmentLevel(vein, tool);
        if (lvl <= 0) return;


        int maxBlocks = Math.max(1, Config.MAX_BLOCKS_CAP.get());

        List<Wave> waves = discoverVeinWaves(level, origin, originState, maxBlocks, tool);
        if (waves.isEmpty()) return;

        // Schedule ripple: wave i after i*Config.VEIN_DELAY_TICKS.get()
        for (int i = 0; i < waves.size(); i++) {
            final int baseDelay = i * Config.VEIN_DELAY_TICKS.get();
            final BlockPos dropAt = origin;
            Wave w = waves.get(i);

            if (!w.faces.isEmpty()) {
                final List<BlockPos> faceWave = List.copyOf(w.faces);
                TickScheduler.schedule(baseDelay, () -> {
                    ItemStack curTool = player.getMainHandItem();
                    for (BlockPos pos : faceWave) tryBreak(level, player, curTool, pos, dropAt);
                });
            }
            if (!w.diags.isEmpty()) {
                final List<BlockPos> diagWave = List.copyOf(w.diags);
                TickScheduler.schedule(baseDelay + 1, () -> {
                    ItemStack curTool = player.getMainHandItem();
                    for (BlockPos pos : diagWave) tryBreak(level, player, curTool, pos, dropAt);
                });
            }
        }
    }

    private static class Wave {
        final List<BlockPos> faces = new ArrayList<>();
        final List<BlockPos> diags = new ArrayList<>();
    }

    private static List<Wave> discoverVeinWaves(ServerLevel level,
                                                BlockPos start,
                                                BlockState startState,
                                                int maxBlocks,
                                                ItemStack toolForDiscovery) {
        final List<Vec3i> FACE_OFFS = Arrays.asList(
                new Vec3i( 1, 0, 0), new Vec3i(-1, 0, 0),
                new Vec3i( 0, 1, 0), new Vec3i( 0,-1, 0),
                new Vec3i( 0, 0, 1), new Vec3i( 0, 0,-1)
        );
        final List<Vec3i> DIAG_OFFS = new ArrayList<>(20);
        for (int dx = -1; dx <= 1; dx++)
            for (int dy = -1; dy <= 1; dy++)
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    final int m = Math.abs(dx) + Math.abs(dy) + Math.abs(dz);
                    if (m == 1) continue;
                    DIAG_OFFS.add(new Vec3i(dx, dy, dz));
                }

        final HashSet<BlockPos> seen = new HashSet<>();
        final ArrayDeque<BlockPos> frontier = new ArrayDeque<>();
        final List<Wave> waves = new ArrayList<>();

        seen.add(start);
        frontier.add(start);

        int remaining = Math.max(0, maxBlocks - 1); // we don't re-break the origin

        while (!frontier.isEmpty() && remaining > 0) {
            final List<BlockPos> layer = new ArrayList<>(frontier);
            frontier.clear();

            Wave wave = new Wave();

            // Block Faces pass
            List<BlockPos> added = new ArrayList<>();
            for (BlockPos cur : layer) {
                for (Vec3i o : FACE_OFFS) {
                    BlockPos np = cur.offset(o.getX(), o.getY(), o.getZ());
                    if (!seen.add(np)) continue;

                    BlockState st = level.getBlockState(np);
                    if (st.isAir()) continue;
                    if (level.getBlockEntity(np) != null) continue;

                    // CONFIG-DRIVEN FILTERS
                    if (!isAllowedTarget(st)) continue;
                    if (!toolMatchesBlock(toolForDiscovery, st)) continue;



                    if (!sameFamily(startState, st)) continue;

                    added.add(np);
                    if (added.size() >= remaining) break;
                }
                if (added.size() >= remaining) break;
            }
            if (!added.isEmpty()) {
                wave.faces.addAll(added);
                frontier.addAll(added);
                remaining -= added.size();
                added = new ArrayList<>();
            }

            if (remaining <= 0) { waves.add(wave); break; }

            // Block Diagonals pass
            for (BlockPos cur : layer) {
                for (Vec3i o : DIAG_OFFS) {
                    BlockPos np = cur.offset(o.getX(), o.getY(), o.getZ());
                    if (!seen.add(np)) continue;

                    BlockState st = level.getBlockState(np);
                    if (st.isAir()) continue;
                    BlockEntity be = level.getBlockEntity(np);
                    if (be != null && !beAllowed(st)) continue;


                    // CONFIG-DRIVEN FILTERS
                    if (!isAllowedTarget(st)) continue;
                    if (!toolMatchesBlock(toolForDiscovery, st)) continue;
                    if (!sameFamily(startState, st)) continue;

                    added.add(np);
                    if (added.size() >= remaining) break;
                }
                if (added.size() >= remaining) break;
            }
            if (!added.isEmpty()) {
                wave.diags.addAll(added);
                frontier.addAll(added);
                remaining -= added.size();
            }

            if (!wave.faces.isEmpty() || !wave.diags.isEmpty()) waves.add(wave);
        }

        return waves;
    }

    // ---------- breaking + redirect drops to origin ----------
    private static void tryBreak(ServerLevel level, ServerPlayer player, ItemStack tool, BlockPos pos, BlockPos dropAt) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return;
        if (state.getDestroySpeed(level, pos) < 0f) return;
        final BlockEntity be = level.getBlockEntity(pos);
        if (be != null && !beAllowed(state)) return;

        if (!isAllowedTarget(state)) return;
        if (!toolMatchesBlock(tool, state)) return;
        if (!player.isCreative() && !player.hasCorrectToolForDrops(state)) return;

        final BlockState before = state;


        // Item drops
        List<ItemStack> drops = Block.getDrops(before, level, pos, be, player, tool);

        // XP amount
        int baseXp = ((IBlockExtension) before.getBlock())
                .getExpDrop(before, level, pos, be, player, tool);


        var enchLookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);


        var silkHolder = enchLookup.get(Enchantments.SILK_TOUCH).orElse(null);


        boolean hasSilk = silkHolder != null &&
                EnchantmentHelper.getItemEnchantmentLevel(silkHolder, tool) > 0;


        final int xpToDrop = hasSilk ? 0 : baseXp;
        runGuarded(() -> {
            level.destroyBlock(pos, false, player);

            level.levelEvent(2001, pos, Block.getId(before));
            SoundType snd = before.getSoundType(level, pos, player);
            level.playSound(null, pos, snd.getBreakSound(), SoundSource.BLOCKS,
                    (snd.getVolume() + 1.0F) / 2.0F, snd.getPitch() * 0.8F);

            for (ItemStack s : drops) if (!s.isEmpty()) Block.popResource(level, dropAt, s);

            if (xpToDrop > 0) {
                before.getBlock().popExperience(level, dropAt, xpToDrop);
            }

            if (!player.isCreative()) {
                tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
        });
    }

    private static void runGuarded(Runnable r) {
        if (Boolean.TRUE.equals(REENTRANT.get())) return;
        try { REENTRANT.set(true); r.run(); }
        catch (Throwable t) { LOG.error("Vein Mining task crashed", t); }
        finally { REENTRANT.set(false); }
    }


    private static boolean toolMatchesBlock(ItemStack tool, BlockState st) {
        return (tool.is(net.minecraft.tags.ItemTags.PICKAXES) && st.is(BlockTags.MINEABLE_WITH_PICKAXE)) ||
                (tool.is(net.minecraft.tags.ItemTags.AXES)     && st.is(BlockTags.MINEABLE_WITH_AXE))     ||
                (tool.is(net.minecraft.tags.ItemTags.SHOVELS)  && st.is(BlockTags.MINEABLE_WITH_SHOVEL))  ||
                (tool.is(net.minecraft.tags.ItemTags.HOES)     && st.is(BlockTags.MINEABLE_WITH_HOE));
    }

    private static boolean isAllowedTarget(BlockState st) {
        if (Config.INCLUDE_ORES_TAG.get() && st.is(net.neoforged.neoforge.common.Tags.Blocks.ORES)) return true;
        for (var tag : Config.extraTags()) if (st.is(tag)) return true;
        return false;
    }

    private static boolean sameFamily(BlockState origin, BlockState candidate) {

        if (Config.REQUIRE_SAME_BLOCK.get()) {
            return candidate.getBlock() == origin.getBlock();
        }
        for (var tag : Config.familyTags()) {
            if (origin.is(tag) && candidate.is(tag)) return true;
        }

        return Config.familyTags().isEmpty();
    }

    private static boolean isInAnyTag(BlockState st, Set<TagKey<Block>> tags) {
        for (var t : tags) if (st.is(t)) return true;
        return false;
    }

    private static boolean beAllowed(BlockState st) {
        return Config.ALLOW_BLOCK_ENTITIES.get() && isInAnyTag(st, Config.beAllowedTags());
    }



}

