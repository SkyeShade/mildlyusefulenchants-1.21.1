package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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

    static final ResourceLocation WISDOM_RL   = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "wisdom");
    static final ResourceLocation ADV_WISDOM_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "advanced_wisdom");
    static final ResourceLocation MASTER_WISDOM_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "masterful_wisdom"); // if needed later

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left  = event.getLeft();
        ItemStack right = event.getRight();
        if (left.isEmpty() || right.isEmpty()) return;

        if (!left.is(Items.ENCHANTED_BOOK) || !right.is(Items.ECHO_SHARD)) return;


        HolderLookup.RegistryLookup<Enchantment> enchantsLookup =
                event.getPlayer().level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);


        Holder<Enchantment> wisdomH    = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, WISDOM_RL)).orElse(null);
        Holder<Enchantment> advWisdomH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ADV_WISDOM_RL)).orElse(null);
        if (wisdomH == null || advWisdomH == null) return;


        ItemEnchantments stored = left.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);


        if (stored.getLevel(wisdomH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(wisdomH, 0);
            mut.set(advWisdomH, 1);

            mut.set(advWisdomH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());

            
            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
    }
}
