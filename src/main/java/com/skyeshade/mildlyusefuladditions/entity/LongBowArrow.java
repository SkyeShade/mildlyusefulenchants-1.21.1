package com.skyeshade.mildlyusefuladditions.entity;



import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import javax.annotation.Nullable;

public class LongBowArrow extends AbstractArrow {
    private static final int EXPOSED_POTION_DECAY_TIME = 600;
    private static final int NO_EFFECT_COLOR = -1;
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR;
    private static final byte EVENT_POTION_PUFF = 0;

    public LongBowArrow(EntityType<? extends LongBowArrow> entityType, Level level) {
        super(entityType, level);
    }

    public LongBowArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(ModEntities.LONG_BOW_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
        this.updateColor();
    }

    public LongBowArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(ModEntities.LONG_BOW_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
        this.updateColor();
    }

    private PotionContents getPotionContents() {
        return (PotionContents)this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    private void setPotionContents(PotionContents potionContents) {
        this.getPickupItemStackOrigin().set(DataComponents.POTION_CONTENTS, potionContents);
        this.updateColor();
    }

    protected void setPickupItemStack(ItemStack pickupItemStack) {
        super.setPickupItemStack(pickupItemStack);
        this.updateColor();
    }

    private void updateColor() {
        PotionContents potioncontents = this.getPotionContents();
        this.entityData.set(ID_EFFECT_COLOR, potioncontents.equals(PotionContents.EMPTY) ? -1 : potioncontents.getColor());
    }

    public void addEffect(MobEffectInstance effectInstance) {
        this.setPotionContents(this.getPotionContents().withEffectAdded(effectInstance));
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_EFFECT_COLOR, -1);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        } else if (this.inGround && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContents.EMPTY) && this.inGroundTime >= 600) {
            this.level().broadcastEntityEvent(this, (byte)0);
            this.setPickupItemStack(new ItemStack(Items.ARROW));
        }

    }

    private void makeParticle(int particleAmount) {
        int i = this.getColor();
        if (i != -1 && particleAmount > 0) {
            for(int j = 0; j < particleAmount; ++j) {
                this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, i), this.getRandomX((double)0.5F), this.getRandomY(), this.getRandomZ((double)0.5F), (double)0.0F, (double)0.0F, (double)0.0F);
            }
        }

    }

    public int getColor() {
        return (Integer)this.entityData.get(ID_EFFECT_COLOR);
    }

    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        Entity entity = this.getEffectSource();
        PotionContents potioncontents = this.getPotionContents();
        if (potioncontents.potion().isPresent()) {
            for(MobEffectInstance mobeffectinstance : ((Potion)((Holder)potioncontents.potion().get()).value()).getEffects()) {
                living.addEffect(new MobEffectInstance(mobeffectinstance.getEffect(), Math.max(mobeffectinstance.mapDuration((p_268168_) -> p_268168_ / 8), 1), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), entity);
            }
        }

        for(MobEffectInstance mobeffectinstance1 : potioncontents.customEffects()) {
            living.addEffect(mobeffectinstance1, entity);
        }

    }

    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    public void handleEntityEvent(byte id) {
        if (id == 0) {
            int i = this.getColor();
            if (i != -1) {
                float f = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i >> 0 & 255) / 255.0F;

                for(int j = 0; j < 20; ++j) {
                    this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, f1, f2), this.getRandomX((double)0.5F), this.getRandomY(), this.getRandomZ((double)0.5F), (double)0.0F, (double)0.0F, (double)0.0F);
                }
            }
        } else {
            super.handleEntityEvent(id);
        }

    }

    static {
        ID_EFFECT_COLOR = SynchedEntityData.defineId(LongBowArrow.class, EntityDataSerializers.INT);
    }
    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        if (this.level().isClientSide) return;


        if (hit.getEntity() instanceof Player target) {
            // If they're actively blocking with a shield, cancel it and apply cooldown
            if (target.isBlocking()) {
                target.stopUsingItem();
                target.getCooldowns().addCooldown(Items.SHIELD, 100); // 5 seconds
                level().playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F);
            }
        }
    }

}

