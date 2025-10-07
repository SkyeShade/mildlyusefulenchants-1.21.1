package com.skyeshade.mildlyusefuladditions.item.custom;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LongBow extends BowItem {


    public static final int MAX_DRAW_TICKS = 40;


    private static final int CUSTOM_RANGE = 32;

    public LongBow(Properties properties) {
        super(properties);
    }
    static int enchantLevelGameplay(ItemStack stack, Level level, ResourceKey<Enchantment> key) {
        Holder<Enchantment> h = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(key);                     // turn ResourceKey -> Holder
        return ((IItemStackExtension)(Object)stack).getEnchantmentLevel(h);
    }
    public static int getMaxDrawTicks(ItemStack stack, Level level) {
        ItemEnchantments enchants = stack.get(DataComponents.ENCHANTMENTS);
        int enchLevel = enchantLevelGameplay(stack, level, Enchantments.QUICK_CHARGE);





        return MAX_DRAW_TICKS - (enchLevel*5);

    }

    private static final ResourceLocation ENHANCED_POWER_ID =
            ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_power");
    private static final ResourceKey<Enchantment> ENHANCED_POWER_KEY =
            ResourceKey.create(Registries.ENCHANTMENT, ENHANCED_POWER_ID);

    private static final ResourceLocation PERFECTED_POWER_ID =
            ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_power");
    private static final ResourceKey<Enchantment> PERFECTED_POWER_KEY =
            ResourceKey.create(Registries.ENCHANTMENT, PERFECTED_POWER_ID);


    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.PUNCH)) {
            return false;
        }
        if (enchantment.is(Enchantments.MULTISHOT)) {
            return true;
        }
        if (enchantment.is(Enchantments.QUICK_CHARGE)) {
            return false;
        }
        return super.supportsEnchantment(stack, enchantment);



    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);
            if (!itemstack.isEmpty()) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                i = EventHooks.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
                if (i < 0) {
                    return;
                }

                float f = getPowerForTime(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemstack, player);
                    if (level instanceof ServerLevel) {
                        ServerLevel serverlevel = (ServerLevel)level;
                        if (!list.isEmpty()) {
                            this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 4.0F, 1.0F, f == 1.0F, (LivingEntity)null);
                        }
                    }

                    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }

    }



    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        super.shoot(level, shooter, hand, weapon, projectileItems, velocity, inaccuracy, isCrit, target);
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        return super.use(level, player, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {

        return super.getUseDuration(stack, entity);
    }
    @Override
    public int getDefaultProjectileRange() {

        return CUSTOM_RANGE;
    }
}
