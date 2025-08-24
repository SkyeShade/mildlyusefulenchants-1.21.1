package com.skyeshade.mildlyusefuladditions.entity;

import com.skyeshade.mildlyusefuladditions.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.entity.*;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.portal.DimensionTransition;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class PrimedExplosive extends Entity implements TraceableEntity {
    private static final EntityDataAccessor<Integer> DATA_FUSE_ID;
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID;
    private static final int DEFAULT_FUSE_TIME = 40;
    private static final String TAG_BLOCK_STATE = "block_state";
    public static final String TAG_FUSE = "fuse";
    private static final ExplosionDamageCalculator USED_PORTAL_DAMAGE_CALCULATOR;
    @Nullable
    private LivingEntity owner;
    private boolean usedPortal;

    public PrimedExplosive(EntityType<? extends PrimedExplosive> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    public PrimedExplosive(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(ModEntities.PRIMED_EXPLOSIVE.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02, (double)0.2F, -Math.cos(d0) * 0.02);
        this.setFuse(40);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_FUSE_ID, 40);
        builder.define(DATA_BLOCK_STATE_ID, ModBlocks.IMPROVISED_EXPLOSIVE.get().defaultBlockState());
    }

    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    protected double getDefaultGravity() {
        return 0.04;
    }

    public void tick() {
        this.handlePortal();
        this.applyGravity();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, (double)-0.5F, 0.7));
        }

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            this.discard();
            if (!this.level().isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level().isClientSide) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + (double)0.5F, this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);


                RandomSource r = this.getRandom(); // or this.level().random
                double vx = net.minecraft.util.Mth.nextDouble(r, -0.05D, 0.05D);
                double vz = net.minecraft.util.Mth.nextDouble(r, -0.05D, 0.05D);

                this.level().addParticle(
                        ParticleTypes.FLAME,
                        this.getX(), this.getY() + 0.5D, this.getZ(),
                        vx, 0.1D, vz
                );

            }
        }

    }

    protected void explode() {
        float f = 10.0F;
        boolean fire = true;
        this.level().explode(this, Explosion.getDefaultDamageSource(this.level(), this), this.usedPortal ? USED_PORTAL_DAMAGE_CALCULATOR : null, this.getX(), this.getY((double)0.0625F), this.getZ(), f, fire, Level.ExplosionInteraction.TNT);
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putShort("fuse", (short)this.getFuse());
        compound.put("block_state", NbtUtils.writeBlockState(this.getBlockState()));
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setFuse(compound.getShort("fuse"));
        if (compound.contains("block_state", 10)) {
            this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("block_state")));
        }

    }

    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    public void restoreFrom(Entity entity) {
        super.restoreFrom(entity);
        if (entity instanceof PrimedExplosive primedExplosive) {
            this.owner = primedExplosive.owner;
        }

    }

    public void setFuse(int life) {
        this.entityData.set(DATA_FUSE_ID, life);
    }

    public int getFuse() {
        return (Integer)this.entityData.get(DATA_FUSE_ID);
    }

    public void setBlockState(BlockState blockState) {
        this.entityData.set(DATA_BLOCK_STATE_ID, blockState);
    }

    public BlockState getBlockState() {
        return (BlockState)this.entityData.get(DATA_BLOCK_STATE_ID);
    }

    private void setUsedPortal(boolean usedPortal) {
        this.usedPortal = usedPortal;
    }

    @Nullable
    public Entity changeDimension(DimensionTransition transition) {
        Entity entity = super.changeDimension(transition);
        if (entity instanceof PrimedExplosive primedExplosive) {
            primedExplosive.setUsedPortal(true);
        }

        return entity;
    }

    static {
        DATA_FUSE_ID = SynchedEntityData.defineId(PrimedExplosive.class, EntityDataSerializers.INT);
        DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(PrimedExplosive.class, EntityDataSerializers.BLOCK_STATE);
        USED_PORTAL_DAMAGE_CALCULATOR = new ExplosionDamageCalculator() {
            public boolean shouldBlockExplode(Explosion p_353087_, BlockGetter p_353096_, BlockPos p_353092_, BlockState p_353086_, float p_353094_) {
                return p_353086_.is(Blocks.NETHER_PORTAL) ? false : super.shouldBlockExplode(p_353087_, p_353096_, p_353092_, p_353086_, p_353094_);
            }

            public Optional<Float> getBlockExplosionResistance(Explosion p_353090_, BlockGetter p_353088_, BlockPos p_353091_, BlockState p_353093_, FluidState p_353095_) {
                return p_353093_.is(Blocks.NETHER_PORTAL) ? Optional.empty() : super.getBlockExplosionResistance(p_353090_, p_353088_, p_353091_, p_353093_, p_353095_);
            }
        };
    }
}
