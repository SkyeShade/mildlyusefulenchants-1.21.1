package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.util.TickScheduler;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class ClearanceHandler {

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
