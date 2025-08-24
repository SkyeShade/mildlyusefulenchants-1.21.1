package com.skyeshade.mildlyusefuladditions.item.custom;

import com.skyeshade.mildlyusefuladditions.datacomponents.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Random;



public class PipeBomb extends Item {

    public static final float EXPLOSION_RADIUS = 6.0f;
    public static final boolean EXPLOSION_FIRE = false;
    public PipeBomb(Properties properties) {
        super(properties);

    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (stack.get(ModDataComponents.TIMER) == null) return;
        if (entity instanceof Player player) {
            if (player.getMainHandItem().is(stack.getItem())){
                int timer = (int)Math.ceil((float)stack.get(ModDataComponents.TIMER.value()) / 20f);
                if (stack.get(ModDataComponents.TIMER.value()) % 20 == 1) {
                }
                player.displayClientMessage(Component.literal(String.valueOf(timer)).withStyle(ChatFormatting.RED), true);
            }
        }
        if (stack.get(ModDataComponents.TIMER) > 0) {
            stack.set(ModDataComponents.TIMER.value(), stack.get(ModDataComponents.TIMER) - 1);
        }
        if (stack.get(ModDataComponents.TIMER) == 0) {
            boom(level, entity.getOnPos(), null,EXPLOSION_RADIUS,EXPLOSION_FIRE);
            stack.consume(1,null);
        }

    }
    public static void boom(Level level, BlockPos pos, @Nullable Entity source,
                            float radius, boolean causeFire) {
        if (level.isClientSide) return;

        level.explode(
                source,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                radius,
                causeFire,
                Level.ExplosionInteraction.TNT
        );
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        Random random = new Random();
        if (item.get(ModDataComponents.TIMER) == null) {
            item.set(ModDataComponents.TIMER.value(), 16*20);
        }
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        Random random = new Random();
        ItemStack stack = player.getItemInHand(usedHand);
        if (stack.get(ModDataComponents.TIMER) == null) {
            //TODO: make randomness a config
            stack.set(ModDataComponents.TIMER.value(), 16*20);
        }
        return super.use(level, player, usedHand);
    }


}
