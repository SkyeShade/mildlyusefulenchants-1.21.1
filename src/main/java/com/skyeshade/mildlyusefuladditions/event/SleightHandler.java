package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.util.TickScheduler;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.enchanting.EnchantmentLevelSetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEnchantItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class SleightHandler {

    @SubscribeEvent
    public static void onLivingDamage(AttackEntityEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        ItemStack mainHand = player.getMainHandItem();
        ResourceLocation sleightId = ResourceLocation.fromNamespaceAndPath("mildlyusefuladditions", "sleight");


        ItemEnchantments enchants = mainHand.get(DataComponents.ENCHANTMENTS);
        if (enchants != null) {

            for (var entry : enchants.entrySet()) {

                Holder<Enchantment> enchant = entry.getKey();
                int level = entry.getIntValue();

                if (enchant.unwrapKey().isPresent() && enchant.unwrapKey().get().location().equals(sleightId)) {


                    e.getTarget().invulnerableTime -= 5;

                    TickScheduler.schedule(20, () -> {

                    });
                }
            }
        }
    }










}
