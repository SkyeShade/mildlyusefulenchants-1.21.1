package com.skyeshade.mildlyusefuladditions.client;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.item.ModItems;
import com.skyeshade.mildlyusefuladditions.item.custom.LongBow;
import com.skyeshade.mildlyusefuladditions.util.TickScheduler;
import net.minecraft.advancements.critereon.UsingItemTrigger;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public final class ClientFovHandler {

    private static final float MAX_EXTRA_ZOOM = 0.15f;
    private static final ResourceLocation SCOPE_ID =
            ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "scope");
    private static final ResourceKey<Enchantment> SCOPE_KEY =
            ResourceKey.create(Registries.ENCHANTMENT, SCOPE_ID);
    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {

        if (event.getPlayer() instanceof LocalPlayer localPlayer) {

        }

        Player player = event.getPlayer();
        if (player == null || !player.isUsingItem()) return;

        ItemStack using = player.getUseItem();
        var reg = player.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        var scopeHolder = reg.getHolder(SCOPE_KEY).orElse(null);
        //System.out.println(scopeHolder);
        float scopeLevel = 1.0f;
        if (scopeHolder != null && player.isUsingItem()) {
            using = player.getUseItem();
            scopeLevel += using.getEnchantments().getLevel(scopeHolder) * 3.0f;

        }



        int remaining = player.getUseItemRemainingTicks();
        int duration  = using.getUseDuration(player);
        int used      = Math.max(0, duration - remaining);


        if (using.is(ModItems.LONG_BOW.get())) {

            float charge = Mth.clamp((float) used / LongBow.getMaxDrawTicks(using, player.level()), 0.0f, 1.0f);

            charge = charge * charge;


            float baseFov = event.getFovModifier();
            float extra   = 1.0f - (MAX_EXTRA_ZOOM * charge * scopeLevel);
            event.setNewFovModifier(baseFov * extra);
        } else if (scopeLevel > 1){

            float charge = Mth.clamp((float) used / 20.f, 0.0f, 1.0f);

            charge = charge * charge;


            float baseFov = event.getFovModifier();
            float extra   = 1.0f - ((MAX_EXTRA_ZOOM-0.02f) * charge * scopeLevel);
            event.setNewFovModifier(baseFov * extra);
        }




    }
}