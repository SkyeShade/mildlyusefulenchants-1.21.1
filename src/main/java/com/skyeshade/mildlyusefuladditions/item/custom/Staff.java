// Staff.java
package com.skyeshade.mildlyusefuladditions.item.custom;

import com.skyeshade.mildlyusefuladditions.datacomponents.ModDataComponents;
import com.skyeshade.mildlyusefuladditions.menu.StaffMenu;

import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;


public class Staff extends Item {
    public Staff(Properties properties) { super(properties); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack self = player.getItemInHand(hand);


        if (player.isShiftKeyDown()) {
            if (!level.isClientSide && player instanceof ServerPlayer sp) {
                sp.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new StaffMenu(id, inv, hand),
                        self.getHoverName()
                ), buf -> buf.writeEnum(hand));
            }
            return InteractionResultHolder.sidedSuccess(self, level.isClientSide);
        }


        if (!level.isClientSide) {
            ItemContainerContents cont = self.getOrDefault(ModDataComponents.STAFF_STORAGE.get(), ItemContainerContents.EMPTY);
            boolean doubleSpell = enchantLevelGameplay(self, level, DOUBLE_SPELL_KEY) > 0;


            java.util.List<ItemStack> templates = new java.util.ArrayList<>(2);
            ItemStack a0 = (cont.getSlots() > 0) ? cont.getStackInSlot(0) : ItemStack.EMPTY;
            ItemStack a1 = (cont.getSlots() > 1) ? cont.getStackInSlot(1) : ItemStack.EMPTY;
            if (!a0.isEmpty()) templates.add(a0);
            if (doubleSpell && !a1.isEmpty()) templates.add(a1);

            int totalFired = 0;
            for (ItemStack ammo : templates) {
                totalFired += fireProjectilesFromItem(level, player, self, ammo);
            }

            if (totalFired > 0) {
                if (!player.isCreative()) {
                    self.hurtAndBreak(1, player,
                            hand == InteractionHand.MAIN_HAND
                                    ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                                    : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
                }
                player.getCooldowns().addCooldown(this, 10);
            }
        }



        return InteractionResultHolder.sidedSuccess(self, level.isClientSide);
    }

    private int fireProjectilesFromItem(Level level, Player player, ItemStack staffStack, ItemStack ammoStack) {
        final int multishot = Math.max(0, enchantLevelGameplay(staffStack, level, MULTISHOT_KEY));
        final int desiredShots = (multishot >= 1) ? 3 : 1;
        final float[] yawOffsets = (desiredShots == 3) ? new float[]{-10f, 0f, 10f} : new float[]{0f};

        int fired = 0;
        for (int i = 0; i < desiredShots; i++) {
            if (fireOne(level, player, staffStack, ammoStack, yawOffsets[Math.min(i, yawOffsets.length - 1)])) {
                fired++;
            }
        }
        return fired;
    }


    private static void shootWithYawOffset(Level level, Player player, Projectile projectile,
                                           float velocity, float inaccuracy, float yawOffsetDeg) {
        projectile.setOwner(player);
        projectile.shootFromRotation(
                player,
                player.getXRot(),
                player.getYRot() + yawOffsetDeg,
                0.0F,
                velocity,
                inaccuracy
        );
        level.addFreshEntity(projectile);
    }
    private void playThrowFx(Level level, Player player) {
        player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(player.getMainHandItem().getItem()));
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                net.minecraft.sounds.SoundEvents.SNOWBALL_THROW, net.minecraft.sounds.SoundSource.PLAYERS,
                0.5F, 0.4F/(level.getRandom().nextFloat()*0.4F+0.8F));
    }
    private boolean fireOne(Level level, Player player, ItemStack staffStack, ItemStack ammoStack, float yawOffsetDeg) {
        Item item = ammoStack.getItem();


        if (item == Items.SPLASH_POTION || item == Items.LINGERING_POTION) {
            ItemStack modified = applyPotionBoostsFromStaff(ammoStack, staffStack, level).copyWithCount(1);
            ThrownPotion t = new ThrownPotion(level, player);
            t.setItem(modified);
            shootWithYawOffset(level, player, t, 0.75F, 1.0F, yawOffsetDeg);
            playThrowFx(level, player);
            return true;
        }


        if (item instanceof EnderpearlItem) {
            ThrownEnderpearl pearl = new ThrownEnderpearl(level, player);
            pearl.setItem(ammoStack.copyWithCount(1));
            shootWithYawOffset(level, player, pearl, 1.5F, 1.0F, yawOffsetDeg);
            playThrowFx(level, player);
            return true;
        }


        if (item instanceof ProjectileItem projItem) {
            var pos = new net.minecraft.core.Position() {
                @Override public double x() { return player.getX(); }
                @Override public double y() { return player.getEyeY() - 0.1; }
                @Override public double z() { return player.getZ(); }
            };
            Projectile proj = projItem.asProjectile(level, pos, ammoStack.copyWithCount(1), player.getDirection());
            proj.setOwner(player);
            shootWithYawOffset(level, player, proj, 1.5F, 1.0F, yawOffsetDeg);
            playThrowFx(level, player);
            return true;
        }


        if (item instanceof FireworkRocketItem) {
            FireworkRocketEntity fw = new FireworkRocketEntity(
                    level, ammoStack.copyWithCount(1), player,
                    player.getX(), player.getEyeY(), player.getZ(), true);
            level.addFreshEntity(fw);
            playThrowFx(level, player);
            return true;
        }


        return false;
    }


    private ItemStack applyPotionBoostsFromStaff(ItemStack potionStack, ItemStack staffStack, Level level) {
        int ampLv = Math.max(0, enchantLevelGameplay(staffStack, level, AMPLIFY_KEY));
        int durLv = Math.max(0, enchantLevelGameplay(staffStack, level, DURABLE_KEY));
        if (ampLv == 0 && durLv == 0) return potionStack.copyWithCount(1);

        ItemStack out = potionStack.copyWithCount(1);
        PotionContents contents = out.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);


        java.util.List<MobEffectInstance> merged = new java.util.ArrayList<>();
        contents.potion().ifPresent(holder -> merged.addAll(holder.value().getEffects()));
        merged.addAll(contents.customEffects());
        if (merged.isEmpty()) return out;

        double durScale = 1.0 + 2.5 * durLv;

        java.util.List<MobEffectInstance> boosted = new java.util.ArrayList<>(merged.size());
        for (MobEffectInstance e : merged) {
            var effectHolder = e.getEffect();
            var effect       = effectHolder.value();

            int newAmplifier = e.getAmplifier() + ampLv;
            int newDuration  = effect.isInstantenous()
                    ? e.getDuration()
                    : (int)Math.max(1, Math.round(e.getDuration() * durScale));

            boosted.add(new MobEffectInstance(
                    effectHolder,
                    newDuration,
                    newAmplifier,
                    e.isAmbient(),
                    e.isVisible(),
                    e.showIcon()
            ));
        }


        // rebuild contents from boosted effects only (avoid double-apply)
        PotionContents newContents = PotionContents.EMPTY;
        for (MobEffectInstance e : boosted) {
            newContents = newContents.withEffectAdded(e);
        }


        contents.customColor().ifPresent(col -> {

        });

        out.set(DataComponents.POTION_CONTENTS, newContents);
        return out;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> ench) {
        if (ench.is(Enchantments.MULTISHOT)) return true;
        return super.supportsEnchantment(stack, ench);
    }
    private static final ResourceKey<Enchantment> MULTISHOT_KEY = ResourceKey.create(
            Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("minecraft", "multishot"));
    private static final ResourceKey<Enchantment> DOUBLE_SPELL_KEY = ResourceKey.create(
            Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("mildlyusefuladditions", "doublespell"));



    private static final ResourceKey<Enchantment> AMPLIFY_KEY = ResourceKey.create(
            Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("mildlyusefuladditions", "amplify"));

    private static final ResourceKey<Enchantment> DURABLE_KEY = ResourceKey.create(
            Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("mildlyusefuladditions", "durable"));

    private static int enchantLevelGameplay(ItemStack stack, Level level, ResourceKey<Enchantment> key) {
        Holder<Enchantment> h = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(key);
        return ((IItemStackExtension)(Object)stack).getEnchantmentLevel(h);
    }

}
