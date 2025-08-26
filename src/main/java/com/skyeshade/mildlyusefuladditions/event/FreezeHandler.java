package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class FreezeHandler {

    private static final ResourceKey<Enchantment> FREEZE_ASPECT_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "freeze_aspect"));
    private static final ResourceKey<Enchantment> ENHANCED_FREEZE_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_freeze_aspect"));
    private static final ResourceKey<Enchantment> PERFECTED_FREEZE_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_freeze_aspect"));

    /*@SubscribeEvent
    public static void onAttack(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        ItemStack weapon = player.getMainHandItem();
        int freezeTicks = getTotalFreezeTicks(level, weapon);

        if (freezeTicks > 0 && event.getEntity() instanceof LivingEntity target) {
            target.setTicksFrozen(Math.min(target.getTicksFrozen() + freezeTicks, target.getTicksRequiredToFreeze()));

            // Opcional: causar um pequeno dano de congelamento imediato
            if (target.getTicksFrozen() >= target.getTicksRequiredToFreeze()) {
                event.getEntity().hurt(level.damageSources().freeze(), 1.0F);
            }
        }
    }

    private static int getTotalFreezeTicks(ServerLevel level, ItemStack stack) {
        var lookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int total = 0;

        var normal = lookup.get(FREEZE_ASPECT_KEY).orElse(null);
        if (normal != null) total += EnchantmentHelper.getItemEnchantmentLevel(normal, stack) * 40; // 2s por nível

        var enhanced = lookup.get(ENHANCED_FREEZE_KEY).orElse(null);
        if (enhanced != null) total += EnchantmentHelper.getItemEnchantmentLevel(enhanced, stack) * 200; // ~10s

        var perfected = lookup.get(PERFECTED_FREEZE_KEY).orElse(null);
        if (perfected != null) total += EnchantmentHelper.getItemEnchantmentLevel(perfected, stack) * 1000; // ~50s

        return total;
    }*/
}
