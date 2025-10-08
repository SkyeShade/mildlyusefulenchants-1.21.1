// Staff.java
package com.skyeshade.mildlyusefuladditions.item.custom;

import com.skyeshade.mildlyusefuladditions.datacomponents.ModDataComponents;
import com.skyeshade.mildlyusefuladditions.menu.StaffMenu;

import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.SimpleMenuProvider;

import java.util.List;

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
            ItemContainerContents c = self.getOrDefault(ModDataComponents.STAFF_STORAGE.get(), ItemContainerContents.EMPTY);
            ItemStack ammo = (c.getSlots() > 0) ? c.getStackInSlot(0) : ItemStack.EMPTY;

            if (!ammo.isEmpty()) {
                if (fireProjectileFromItem(level, player, ammo)) {
                    if (!player.isCreative()) {
                        ItemStack after = ammo.copy();

                        self.set(ModDataComponents.STAFF_STORAGE.get(),
                                after.isEmpty() ? ItemContainerContents.EMPTY
                                        : ItemContainerContents.fromItems(List.of(after)));

                    }
                    self.hurtAndBreak(
                            1,
                            player,
                            hand == InteractionHand.MAIN_HAND
                                    ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                                    : net.minecraft.world.entity.EquipmentSlot.OFFHAND
                    );
                    player.getCooldowns().addCooldown(this, 10);
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(self, level.isClientSide);
    }

    private boolean fireProjectileFromItem(Level level, Player player, ItemStack stack) {
        Item item = stack.getItem();


        if (item instanceof ProjectileItem projItem) {

            var pos = new Position() {
                @Override public double x() { return player.getX(); }
                @Override public double y() { return player.getEyeY() - 0.1; }
                @Override public double z() { return player.getZ(); }
            };


            Projectile projectile =
                    projItem.asProjectile(level, pos, stack.copyWithCount(1), player.getDirection());

            projectile.setOwner(player);
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(projectile);


            player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(player.getMainHandItem().getItem()));
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    net.minecraft.sounds.SoundEvents.SNOWBALL_THROW, net.minecraft.sounds.SoundSource.PLAYERS,
                    0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            return true;
        }


        if (item instanceof FireworkRocketItem) {
            FireworkRocketEntity fw = new FireworkRocketEntity(
                    level, stack.copyWithCount(1), player,
                    player.getX(), player.getEyeY(), player.getZ(), true);
            level.addFreshEntity(fw);
            return true;
        }
        if (item instanceof PotionItem) {
            ThrownPotion t = new ThrownPotion(level, player); t.setItem(stack.copyWithCount(1)); shootLikeHand(level, player, t, 0.75F, 1.0F);
            return true;
        }

        return false;
    }


    private static void shootLikeHand(Level level, Player player, Projectile projectile, float velocity, float inaccuracy) {
        projectile.setOwner(player);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, inaccuracy);
        level.addFreshEntity(projectile);
        player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(player.getMainHandItem().getItem()));
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                net.minecraft.sounds.SoundEvents.SNOWBALL_THROW, net.minecraft.sounds.SoundSource.PLAYERS, 0.5F,
                0.4F/(level.getRandom().nextFloat()*0.4F+0.8F));

    }
}
