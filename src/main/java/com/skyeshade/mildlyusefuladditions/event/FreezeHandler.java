package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

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
                    target.setTicksFrozen(levelEnchant * 800);
                }
                if (enchant.unwrapKey().isPresent() && enchant.unwrapKey().get().location().equals(efreezeAspectId)) {
                    int levelEnchant = enchants.getLevel(enchant);
                    target.setTicksFrozen(levelEnchant * 1600);
                }
                if (enchant.unwrapKey().isPresent() && enchant.unwrapKey().get().location().equals(pfreezeAspectId)) {
                    int levelEnchant = enchants.getLevel(enchant);
                    target.setTicksFrozen(levelEnchant * 3200);
                }
            }
        }
    }
}
