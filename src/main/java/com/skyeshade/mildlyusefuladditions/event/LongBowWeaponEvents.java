package com.skyeshade.mildlyusefuladditions.event;


import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.item.custom.LongBow;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;


@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public final class LongBowWeaponEvents {


    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow arrow)) return;
        if (arrow.level().isClientSide) return;

        final var hit = event.getRayTraceResult();
        if (arrow.getWeaponItem() == null) {
            return;
        }
        if (arrow.getWeaponItem().getItem() instanceof LongBow) {
            if (hit instanceof EntityHitResult ehit) {
                if (ehit.getEntity() instanceof LivingEntity liv) {
                    if (liv.isBlocking()) {

                            liv.stopUsingItem();

                        if (liv instanceof Player player) {
                            player.getCooldowns().addCooldown(Items.SHIELD, 100);
                            player.level().playSound(null,player.getX(), player.getY(), player.getZ(),
                                    SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0F, 0.8F);
                        }

                    }

                }


            }
        }

    }

}