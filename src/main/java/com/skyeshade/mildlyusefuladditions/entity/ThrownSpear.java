package com.skyeshade.mildlyusefuladditions.entity;

import com.skyeshade.mildlyusefuladditions.item.ModItems;
import com.skyeshade.mildlyusefuladditions.item.custom.SpearItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;

import net.minecraft.world.entity.*;



import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;


import javax.annotation.Nullable;

public class ThrownSpear extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL    = SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<ItemStack> DATA_RENDER_STACK =
            SynchedEntityData.defineId(ThrownSpear.class, EntityDataSerializers.ITEM_STACK);
    private boolean dealtDamage;
    public  int     clientSideReturnTick;
    private float damage = 0f;

    private int pierced = 0;
    private static final int MAX_PIERCE = 2;


    private float baseDamage = 6.0F;

    public ThrownSpear(EntityType<? extends ThrownSpear> type, Level level) {
        super(type, level);
    }

    public ThrownSpear(Level level, LivingEntity owner, ItemStack weaponStack) {
        super(ModEntities.THROWN_SPEAR.get(),
                owner, level,
                weaponStack.copyWithCount(1),
                weaponStack.copyWithCount(1));
        this.entityData.set(DATA_RENDER_STACK, weaponStack.copy());
        this.entityData.set(ID_LOYALTY, getLoyaltyFromItem(weaponStack));
        this.entityData.set(ID_FOIL, weaponStack.hasFoil());
        this.damage = computeAttackFromStack(weaponStack);
    }


    public ThrownSpear(Level level, double x, double y, double z, ItemStack weaponStack) {
        super(
                ModEntities.THROWN_SPEAR.get(),
                x, y, z,
                level,
                weaponStack.copy(),
                weaponStack.copyWithCount(1)
        );
        this.entityData.set(DATA_RENDER_STACK, weaponStack.copy());
        this.entityData.set(ID_LOYALTY, getLoyaltyFromItem(weaponStack));
        this.entityData.set(ID_FOIL, weaponStack.hasFoil());
        this.damage = computeAttackFromStack(weaponStack);

        System.out.println("[SPEAR] snap=" + this.damage + " item=" + weaponStack);

    }

    private static float computeAttackFromStack(ItemStack stack) {
        double base = 1.0;
        double add = 0.0, mulBase = 0.0, mulTotal = 0.0;

        ItemAttributeModifiers mods = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        for (var entry : mods.modifiers()) {

            if (!entry.slot().test(EquipmentSlot.MAINHAND)) continue;


            if (!entry.attribute().is(Attributes.ATTACK_DAMAGE)) continue;

            AttributeModifier m = entry.modifier();
            switch (m.operation()) {
                case ADD_VALUE -> add += m.amount();
                case ADD_MULTIPLIED_BASE -> mulBase += m.amount();
                case ADD_MULTIPLIED_TOTAL -> mulTotal += m.amount();
            }
        }

        double result = (base + add) * (1.0 + mulBase) * (1.0 + mulTotal);


        if (result == 1.0 && stack.getItem() instanceof TieredItem ti) {
            result = 1.0 + 4.0 + ti.getTier().getAttackDamageBonus();
        }
        return (float)result;
    }





    @Override
    protected void defineSynchedData(SynchedEntityData.Builder b) {
        super.defineSynchedData(b);
        b.define(ID_LOYALTY, (byte)0);
        b.define(ID_FOIL, false);
        b.define(DATA_RENDER_STACK, ItemStack.EMPTY);
    }

    public boolean isFoil() { return this.entityData.get(ID_FOIL); }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) this.dealtDamage = true;



        Entity owner = this.getOwner();
        int loyalty  = this.entityData.get(ID_LOYALTY);
        if (loyalty > 0 && (this.dealtDamage || this.isNoPhysics()) && owner != null) {
            if (!isAcceptableReturnOwner()) {
                if (!level().isClientSide && this.pickup == Pickup.ALLOWED) this.spawnAtLocation(getPickupItem(), 0.1F);
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 toOwner = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(getX(), getY() + toOwner.y * 0.015 * loyalty, getZ());
                if (level().isClientSide) this.yOld = getY();
                this.setDeltaMovement(getDeltaMovement().scale(0.95).add(toOwner.normalize().scale(0.05 * loyalty)));
                if (this.clientSideReturnTick++ == 0) this.playSound(SoundEvents.TRIDENT_RETURN, 10f, 1f);
            }
        }

        super.tick();
    }

    private boolean isAcceptableReturnOwner() {
        Entity e = getOwner();
        return e != null && e.isAlive() && (!(e instanceof ServerPlayer sp) || !sp.isSpectator());
    }

    @Override
    protected @Nullable EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
        return super.findHitEntity(start, end);
    }
    public void setSnapshotDamage(float dmg) {
        this.damage = Math.max(0f, dmg);
    }



    @Override
    protected void onHitEntity(EntityHitResult hit) {
        Entity target = hit.getEntity();
        this.dealtDamage = true;


        ItemStack weapon = getWeaponItem();
        if (weapon.isEmpty()) weapon = getPickupItemStackOrigin();

        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity ownerLiving) {
            DamageSource src = (owner instanceof Player p)
                    ? level().damageSources().playerAttack(p)
                    : level().damageSources().mobAttack(ownerLiving);

            float finalDmg = this.damage;

            if (level() instanceof ServerLevel sl) {
                finalDmg = EnchantmentHelper.modifyDamage(sl, weapon, hit.getEntity(), src, finalDmg);
            }



            if (hit.getEntity().hurt(src, finalDmg) && level() instanceof ServerLevel sl2) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(sl2, hit.getEntity(), src, weapon);
            }
        }





        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));


        this.pierced++;
        if (this.pierced >= MAX_PIERCE) {
            this.dealtDamage = true;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.1));
        }
    }


    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel lvl, BlockHitResult br, ItemStack stack) {

        Vec3 pos = br.getBlockPos().clampLocationWithin(br.getLocation());
        EnchantmentHelper.onHitBlock(
                lvl, stack, this.getOwner() instanceof LivingEntity le ? le : null,
                this, null, pos, lvl.getBlockState(br.getBlockPos()), p -> this.kill()
        );
    }
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (result.getType() == HitResult.Type.BLOCK) {
            this.dealtDamage = true;
        }
        if (!level().isClientSide) {

            Entity owner = this.getOwner();
            LivingEntity ownerLiving = owner instanceof LivingEntity le ? le : null;

            ItemStack stack = this.getWeaponItem();
            if (!stack.isEmpty() && stack.isDamageableItem()) {

                EquipmentSlot slot = EquipmentSlot.MAINHAND;
                if (ownerLiving != null) {
                    if (ownerLiving.getOffhandItem() == stack) slot = EquipmentSlot.OFFHAND;
                    stack.hurtAndBreak(1, ownerLiving, slot);
                } else {
                    stack.hurtAndBreak(1, ownerLiving, slot);
                }

                if (stack.isEmpty() || stack.getDamageValue() >= stack.getMaxDamage()) {
                    this.discard();
                }
            }
        }
    }


    @Override public ItemStack getWeaponItem() { return getRenderStack(); }
    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }
    @Override
    public ItemStack getPickupItem() {
        ItemStack s = getRenderStack();
        return s.isEmpty() ? ItemStack.EMPTY : s.copy();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        ItemStack s = getRenderStack();
        return s.isEmpty() ? ItemStack.EMPTY : s.copy();
    }



    @Override protected SoundEvent getDefaultHitGroundSoundEvent() { return SoundEvents.TRIDENT_HIT_GROUND; }

    @Override
    public void playerTouch(Player p) {
        if (this.ownedBy(p) || this.getOwner() == null) super.playerTouch(p);
    }

    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack s = getRenderStack();
        if (!s.isEmpty()) tag.put("RenderStack", s.save(this.registryAccess()));
        tag.putFloat("BaseDamage", this.baseDamage);
        tag.putBoolean("DealtDamage", this.dealtDamage);
        tag.putInt("Pierced", this.pierced);
    }

    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.dealtDamage = tag.getBoolean("DealtDamage");
        this.pierced = tag.getInt("Pierced");
        this.baseDamage = tag.getFloat("BaseDamage");
        if (tag.contains("RenderStack")) {
            ItemStack s = ItemStack.parse(this.registryAccess(), tag.getCompound("RenderStack")).orElse(ItemStack.EMPTY);
            this.entityData.set(DATA_RENDER_STACK, s);
            this.entityData.set(ID_LOYALTY, getLoyaltyFromItem(s));
            this.entityData.set(ID_FOIL, s.hasFoil());
        }
    }

    public ItemStack getRenderStack() {
        ItemStack s = this.entityData.get(DATA_RENDER_STACK);
        return s == null ? ItemStack.EMPTY : s;
    }
    private byte getLoyaltyFromItem(ItemStack stack) {
        var reg = this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var loyalty = reg.getOrThrow(Enchantments.LOYALTY);
        int lvl = stack.getEnchantmentLevel(loyalty);
        return (byte) Mth.clamp(lvl, 0, 127);
    }

    @Override public void tickDespawn() {
        int l = this.entityData.get(ID_LOYALTY);
        if (this.pickup != Pickup.ALLOWED || l <= 0) super.tickDespawn();
    }

    @Override protected float getWaterInertia() { return 0.99F; }
    @Override public boolean shouldRender(double x, double y, double z) { return true; }
}
