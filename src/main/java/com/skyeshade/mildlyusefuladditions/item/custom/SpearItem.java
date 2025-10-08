package com.skyeshade.mildlyusefuladditions.item.custom;

import com.skyeshade.mildlyusefuladditions.entity.ThrownSpear;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.*;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE;

public class SpearItem extends TieredItem implements ProjectileItem {
    public static final int   THROW_THRESHOLD_TICKS = 10;
    public static final float THROW_POWER           = 2.5F;

    public SpearItem(Tier tier, Properties props) {
        super(tier, props
                .stacksTo(1)
                .durability(tier.getUses())
                .component(DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(tier, -2.9))
        );
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, double attackSpeed) {
        double attackDamage = 4.0 + tier.getAttackDamageBonus();
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, attackDamage, ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }


    public static ItemAttributeModifiers createAttributesExact(double attackDamage, double attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, attackDamage, ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.SPEAR; }
    @Override public int getUseDuration(ItemStack stack, LivingEntity entity) { return 72000; }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isTooDamagedToUse(stack)) return InteractionResultHolder.fail(stack);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
        if (!(user instanceof Player player)) return;

        int used = this.getUseDuration(stack, user) - timeLeft;
        if (used < THROW_THRESHOLD_TICKS) return;

        if (!level.isClientSide) {
            ItemStack thrownCopy = stack.copyWithCount(1);


            ThrownSpear proj = new ThrownSpear(level, player.getX(), player.getEyeY() - 0.15, player.getZ(), thrownCopy);
            proj.setOwner(player);
            proj.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, THROW_POWER, 1.0F);


            proj.setSnapshotDamage(computeThrownDamage(thrownCopy));

            if (player.hasInfiniteMaterials()) {
                proj.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            level.addFreshEntity(proj);


            if (!player.hasInfiniteMaterials()) {
                player.getInventory().removeItem(stack);
            }


            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F,
                    1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F));
        }

        if (user instanceof Player p) p.awardStat(Stats.ITEM_USED.get(this));
    }


    public static float computeThrownDamage(ItemStack stack) {
        ItemAttributeModifiers comp = stack.getOrDefault(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        double base = 1.0;
        double add = 0.0, mulBase = 0.0, mulTotal = 0.0;

        for (ItemAttributeModifiers.Entry e : comp.modifiers()) {
            if (!e.slot().test(EquipmentSlot.MAINHAND)) continue;


            if (!e.attribute().is(Attributes.ATTACK_DAMAGE)) continue;

            AttributeModifier m = e.modifier();
            switch (m.operation()) {
                case ADD_VALUE            -> add     += m.amount();
                case ADD_MULTIPLIED_BASE  -> mulBase += m.amount();
                case ADD_MULTIPLIED_TOTAL -> mulTotal+= m.amount();
            }
        }

        double result = (base + add) * (1.0 + mulBase) * (1.0 + mulTotal);
        return (float) result;
    }


    private static boolean isTooDamagedToUse(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }


    @Override
    public Projectile asProjectile(Level level, net.minecraft.core.Position pos, ItemStack stack, Direction direction) {
        ThrownSpear spear = new ThrownSpear(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
        spear.pickup = AbstractArrow.Pickup.ALLOWED;
        spear.setSnapshotDamage(computeThrownDamage(stack));
        return spear;
    }


    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> ench) {
        if (ench.is(Enchantments.LOYALTY)) return true;
        if (ench.is(Enchantments.RIPTIDE)) return false;
        if (ench.is(Enchantments.FIRE_ASPECT)) return true;
        return super.supportsEnchantment(stack, ench);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
        return ItemAbilities.DEFAULT_TRIDENT_ACTIONS.contains(ability);
    }
}
