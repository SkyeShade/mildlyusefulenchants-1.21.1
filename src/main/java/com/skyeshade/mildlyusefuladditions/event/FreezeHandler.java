package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.util.TickScheduler;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class FreezeHandler {

    @SubscribeEvent
    public static void onHurtEntity (AttackEntityEvent e) {
        Player attacker = e.getEntity();
        if (attacker.level().isClientSide) return;
        Entity target = e.getTarget();

        ItemStack weapon = attacker.getWeaponItem();
        ResourceLocation freezeAspectId = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "freeze_aspect");
        ResourceLocation efreezeAspectId = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_freeze_aspect");
        ResourceLocation pfreezeAspectId = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_freeze_aspect");

        ItemEnchantments enchants = weapon.get(DataComponents.ENCHANTMENTS);
        if (enchants != null) {
            for (var entry : enchants.entrySet()) {
                Holder<Enchantment> enchant = entry.getKey();

                if (enchant.unwrapKey().isPresent() && enchant.unwrapKey().get().location().equals(freezeAspectId)) {



                    int levelEnchant = enchants.getLevel(enchant);
                    System.out.println(levelEnchant);
                    int freezeTicks = levelEnchant * 80;
                    System.out.println(freezeTicks);
                    target.setTicksFrozen(80 * 2);

                }
            }
        }

    }

    @SubscribeEvent
    public static void onPlayerTick (PlayerTickEvent.Post e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        //System.out.println("event fired and is server");

        ResourceLocation clearanceId = ResourceLocation.fromNamespaceAndPath("mildlyusefuladditions", "clearance");


        Iterable<ItemStack> armorItems = player.getArmorSlots();
        for (ItemStack item : armorItems) {
            ItemEnchantments enchants = item.get(DataComponents.ENCHANTMENTS);
            if (enchants != null) {

                for (var entry : enchants.entrySet()) {

                    Holder<Enchantment> enchant = entry.getKey();

                    if (enchant.unwrapKey().isPresent() && enchant.unwrapKey().get().location().equals(clearanceId)) {
                        if (player.hasEffect(MobEffects.DARKNESS)) {
                            player.removeEffect(MobEffects.DARKNESS);
                        }

                        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false));


                    }
                }

            }

        }


    }

}
