package com.skyeshade.mildlyusefuladditions.block;

import com.mojang.serialization.MapCodec;
import com.skyeshade.mildlyusefuladditions.entity.PrimedExplosive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class ImprovisedExplosive extends Block {
    public static final MapCodec<TntBlock> CODEC = simpleCodec(TntBlock::new);
    public static final BooleanProperty UNSTABLE;

    public MapCodec<TntBlock> codec() {
        return CODEC;
    }

    public ImprovisedExplosive(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(UNSTABLE, false));
    }

    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        explode(world, pos, igniter);
    }

    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock()) && level.hasNeighborSignal(pos)) {
            this.onCaughtFire(state, level, pos, (Direction)null, (LivingEntity)null);
            level.removeBlock(pos, false);
        }

    }

    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.hasNeighborSignal(pos)) {
            this.onCaughtFire(state, level, pos, (Direction)null, (LivingEntity)null);
            level.removeBlock(pos, false);
        }

    }

    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && !player.isCreative() && (Boolean)state.getValue(UNSTABLE)) {
            this.onCaughtFire(state, level, pos, (Direction)null, (LivingEntity)null);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            PrimedExplosive primedExplosive = new PrimedExplosive(level, (double)pos.getX() + (double)0.5F, (double)pos.getY(), (double)pos.getZ() + (double)0.5F, explosion.getIndirectSourceEntity());
            int i = primedExplosive.getFuse();
            primedExplosive.setFuse((short)(level.random.nextInt(i / 4) + i / 8));
            level.addFreshEntity(primedExplosive);
        }

    }

    /** @deprecated */
    @Deprecated
    public static void explode(Level level, BlockPos pos) {
        explode(level, pos, (LivingEntity)null);
    }

    /** @deprecated */
    @Deprecated
    private static void explode(Level level, BlockPos pos, @Nullable LivingEntity entity) {
        if (!level.isClientSide) {
            PrimedExplosive primedExplosive = new PrimedExplosive(level, (double)pos.getX() + (double)0.5F, (double)pos.getY(), (double)pos.getZ() + (double)0.5F, entity);
            level.addFreshEntity(primedExplosive);
            level.playSound((Player)null, primedExplosive.getX(), primedExplosive.getY(), primedExplosive.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(entity, GameEvent.PRIME_FUSE, pos);
        }

    }

    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        } else {
            this.onCaughtFire(state, level, pos, hitResult.getDirection(), player);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            Item item = stack.getItem();
            if (stack.is(Items.FLINT_AND_STEEL)) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            } else {
                stack.consume(1, player);
            }

            player.awardStat(Stats.ITEM_USED.get(item));
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide) {
            BlockPos blockpos = hit.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.mayInteract(level, blockpos)) {
                this.onCaughtFire(state, level, blockpos, (Direction)null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                level.removeBlock(blockpos, false);
            }
        }

    }

    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{UNSTABLE});
    }

    static {
        UNSTABLE = BlockStateProperties.UNSTABLE;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {

        if (player.isShiftKeyDown()) return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        this.onCaughtFire(state, level, pos, null, player);
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}