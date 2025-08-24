package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public final class ReplenishHandler {
    private ReplenishHandler() {}


    private static final int baseTicksPerPoint = 9600;


    // mildlyusefuladditions:replenish (make sure this matches your registry id)
    private static final ResourceKey<Enchantment> REPLENISH_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "replenish"));

    /**
     * Per-player, per-slot timers. Keyed by player UUID -> (slotKey -> ticksSinceLastRepair).
     * slotKey layout:
     *   0..35  = main inventory (incl. hotbar)
     *   100..103 = armor (0: boots, 1: leggings, 2: chest, 3: helmet in Inventory.armor order)
     *   150..   = offhand (usually just 150)
     */
    private static final Map<UUID, Map<Integer, Integer>> TIMERS = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post e) {
        if (!(e.getEntity() instanceof ServerPlayer player)) return;
        ServerLevel level = (ServerLevel) player.level();

        // Resolve the enchant once per player tick
        var enchLookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var replenish = enchLookup.get(REPLENISH_KEY).orElse(null);
        if (replenish == null) return;

        Map<Integer, Integer> timers = TIMERS.computeIfAbsent(player.getUUID(), k -> new HashMap<>());
        Inventory inv = player.getInventory();

        // --- main inventory (0..35) ---
        for (int i = 0; i < inv.items.size(); i++) {
            tickStack(level, player, inv.items.get(i), /*slotKey*/ i, timers, replenish);
        }

        // --- armor (100..103) ---
        for (int i = 0; i < inv.armor.size(); i++) {
            tickStack(level, player, inv.armor.get(i), /*slotKey*/ 100 + i, timers, replenish);
        }

        // --- offhand (150..) ---
        for (int i = 0; i < inv.offhand.size(); i++) {
            tickStack(level, player, inv.offhand.get(i), /*slotKey*/ 150 + i, timers, replenish);
        }
    }

    private static void tickStack(ServerLevel level,
                                  ServerPlayer player,
                                  ItemStack stack,
                                  int slotKey,
                                  Map<Integer, Integer> timers,
                                  net.minecraft.core.Holder<Enchantment> replenish) {
        // Fast-path: not a candidate → reset timer
        if (stack.isEmpty() || !stack.isDamageableItem() || !stack.isDamaged()) {
            timers.put(slotKey, 0);
            return;
        }

        int lvl = EnchantmentHelper.getItemEnchantmentLevel(replenish, stack);
        if (lvl <= 0) {
            timers.put(slotKey, 0);
            return;
        }

        // Determine period from level

        int period = periodForLevel(baseTicksPerPoint, lvl);

        int val = timers.getOrDefault(slotKey, 0) + 1;
        if (val >= period) {
            int dmg = stack.getDamageValue();
            if (dmg > 0) stack.setDamageValue(dmg - 1);
            val = 0;
        }
        timers.put(slotKey, val);
    }
    // Halves per level: L1 = base, L2 = base/2, L3 = base/4, ...
    static int periodForLevel(int baseTicksPerPoint, int lvl) {
        int k = Math.max(0, lvl - 1);
        int shift = Math.min(k, 30);
        int denom = 1 << shift;
        int period = (baseTicksPerPoint + denom - 1) / denom;
        return Math.max(1, period);
    }



}