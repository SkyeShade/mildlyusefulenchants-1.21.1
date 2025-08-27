package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.EnchantedBookItem;

import net.minecraft.world.item.enchantment.*;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class AnvilUpdateHandler {

    static ResourceLocation wisdom = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "wisdom");
    static ResourceLocation eWisdom = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "advanced_wisdom");
    static ResourceLocation pWisdom = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "masterful_wisdom");

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if (left.isEmpty() || right.isEmpty()) return;
        if (left.getItem() == Items.ENCHANTED_BOOK && right.getItem() == Items.ECHO_SHARD) {
            ItemEnchantments enchants = left.get(DataComponents.ENCHANTMENTS);
            if (enchants != null) {
                for (var entry : enchants.entrySet()) {
                    Holder<Enchantment> enchant = entry.getKey();
                    ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
                    if (enchant.unwrapKey().isPresent() && enchant.unwrapKey().get().location().equals(wisdom) && enchants.getLevel(enchant) == 3) {
                        // skye fix
                    }
                }
            }
        }
    }
}