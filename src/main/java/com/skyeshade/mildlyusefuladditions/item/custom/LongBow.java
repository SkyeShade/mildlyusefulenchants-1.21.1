package com.skyeshade.mildlyusefuladditions.item.custom;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.entity.LongBowArrow;
import com.skyeshade.mildlyusefuladditions.util.TickScheduler;
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
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
        if (!(entityLiving instanceof Player player)) return;

        ItemStack ammo = player.getProjectile(stack);
        if (ammo.isEmpty()) return;

        int used = this.getUseDuration(stack, entityLiving) - timeLeft;

        used = EventHooks.onArrowLoose(stack, level, player, used, !ammo.isEmpty());
        if (used < 0) return;


        float f = getPowerForTimeScaled(used, getMaxDrawTicks(stack, level));

        if (f < 0.1F) return;


        var drawn = draw(stack, ammo, player);
        if (level instanceof ServerLevel server && !drawn.isEmpty()) {
            ItemEnchantments enchants = stack.get(DataComponents.ENCHANTMENTS);
            int multiShotLevel = 0;

            for (var entry : enchants.entrySet()) {

                Holder<Enchantment> enchant = entry.getKey();


                if (enchant.unwrapKey().isPresent() && enchant.unwrapKey().get().location().equals(Enchantments.MULTISHOT)) {


                    multiShotLevel = entry.getIntValue();

                }
            }


            this.shoot(server, player, player.getUsedItemHand(), stack, drawn, f * 5.0F, 0.5F, f == 1.0F, null);



        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

        player.awardStat(Stats.ITEM_USED.get(this));
    }

    /** Vanilla bow curve, but normalized so full draw = maxDrawTicks instead of 20 */
    public static float getPowerForTimeScaled(int chargeTicks, int maxDrawTicks) {
        if (maxDrawTicks <= 0) return 1.0F;
        float n = Math.min(1.0F, (float) chargeTicks / (float) maxDrawTicks);
        float f = (n * n + n * 2.0F) / 3.0F;
        return Math.min(1.0F, f);
    }

    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit, @Nullable LivingEntity target) {
        super.shoot(level, shooter, hand, weapon, projectileItems, velocity, inaccuracy, isCrit, target);
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        super.shootProjectile(shooter, projectile, index, velocity, inaccuracy, angle, target);
        ItemStack stack = shooter.getUseItem();
        int usedTicks = stack.getUseDuration(shooter) - shooter.getUseItemRemainingTicks();
        int maxDraw = getMaxDrawTicks(stack, shooter.level());
        float drawProgress = getPowerForTimeScaled(usedTicks, maxDraw);

        int enchScopeLevel = enchantLevelGameplay(stack, shooter.level(), ResourceKey.create(Registries.ENCHANTMENT,ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "scope")));


        System.out.println(drawProgress);
        if (enchScopeLevel != 0 && drawProgress >= 0.9F) {

            projectile.setNoGravity(true);
        }
    }

    @Override
    protected net.minecraft.world.entity.projectile.Projectile createProjectile(
            net.minecraft.world.level.Level level,
            net.minecraft.world.entity.LivingEntity shooter,
            net.minecraft.world.item.ItemStack weapon,
            net.minecraft.world.item.ItemStack ammo,
            boolean isCrit) {

        var arrow = new LongBowArrow(level, shooter, ammo, weapon);
        arrow.setCritArrow(isCrit);


        var ench = level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);

        int power = ench.get(Enchantments.POWER)
                .map(h -> weapon.getEnchantmentLevel(h)).orElse(0);
        if (power > 0) arrow.setBaseDamage(arrow.getBaseDamage() + 0.5D * power + 0.5D);

        int enhanced_power = ench.get(ENHANCED_POWER_KEY)
                .map(h -> weapon.getEnchantmentLevel(h)).orElse(0);
        if (enhanced_power > 0) arrow.setBaseDamage(arrow.getBaseDamage() + 1.0D * enhanced_power + 1.0D);

        int perfected_power = ench.get(PERFECTED_POWER_KEY)
                .map(h -> weapon.getEnchantmentLevel(h)).orElse(0);
        if (perfected_power > 0) arrow.setBaseDamage(arrow.getBaseDamage() + 1.5D * perfected_power + 1.5D);

        boolean flame = ench.get(Enchantments.FLAME)
                .map(h -> weapon.getEnchantmentLevel(h) > 0).orElse(false);
        if (flame) arrow.setRemainingFireTicks(100);

        return arrow;
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
