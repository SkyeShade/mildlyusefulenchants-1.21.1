package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.datacomponents.ModDataComponents;
import com.skyeshade.mildlyusefuladditions.item.custom.PipeBomb;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.tick.ServerTickEvent;



@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class PipeBombHandler {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        var server = event.getServer();
        //if (server == null) return;

        // Loop every loaded dimension
        for (ServerLevel level : server.getAllLevels()) {

            // Iterate all loaded ItemEntitys in this level
            for (var entity : level.getAllEntities()) {
                if (!(entity instanceof ItemEntity itemEntity)) continue;

                var stack = itemEntity.getItem();


                Integer timer = stack.get(ModDataComponents.TIMER.value());
                if (timer == null) continue;

                if (timer > 0) {
                    // Decrement
                    stack.set(ModDataComponents.TIMER.value(), timer - 1);
                } else {
                    // Explosion at the item’s current position (server-side only)
                    var p = itemEntity.position();
                    level.explode(
                            /* source */ null,
                            p.x, p.y, p.z,
                            /* radius */ PipeBomb.EXPLOSION_RADIUS,          // tweak
                            /* causeFire */ PipeBomb.EXPLOSION_FIRE,       // tweak
                            Level.ExplosionInteraction.TNT
                    );
                    // remove the item so it doesn't keep exploding
                    itemEntity.discard();
                }
            }
        }
        for (ServerPlayer sp : server.getPlayerList().getPlayers()) {
            var inv = sp.getInventory();

            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack container = inv.getItem(i);


                var ih = container.getCapability(Capabilities.ItemHandler.ITEM);
                if (ih == null) continue;

                for (int s = 0; s < ih.getSlots(); s++) {
                    ItemStack inside = ih.getStackInSlot(s);
                    if (inside.isEmpty()) continue;


                    Integer timer = inside.get(ModDataComponents.TIMER.value());
                    if (timer == null) continue;



                    if (timer > 0) {

                        inside.set(ModDataComponents.TIMER.value(), timer - 1);


                        if (ih instanceof net.neoforged.neoforge.items.IItemHandlerModifiable mod) {
                            mod.setStackInSlot(s, inside);
                        } else {

                            ih.extractItem(s, inside.getCount(), false);
                            ih.insertItem(s, inside, false);
                        }
                    } else {

                        var p = sp.position();
                        sp.level().explode(null, p.x, p.y, p.z,
                                PipeBomb.EXPLOSION_RADIUS, PipeBomb.EXPLOSION_FIRE,
                                Level.ExplosionInteraction.TNT);


                        if (ih instanceof net.neoforged.neoforge.items.IItemHandlerModifiable mod) {
                            mod.setStackInSlot(s, ItemStack.EMPTY);
                        } else {
                            ih.extractItem(s, inside.getCount(), false);
                        }
                    }
                }
            }
        }


    }
    @SubscribeEvent
    public static void onExplosionDetonate(net.neoforged.neoforge.event.level.ExplosionEvent.Detonate event) {
        Level lvl = event.getLevel();
        if (!(lvl instanceof net.minecraft.server.level.ServerLevel level)) return;


        for (Entity ent : event.getAffectedEntities()) {
            if (!(ent instanceof net.minecraft.world.entity.item.ItemEntity item)) continue;

            ItemStack stack = item.getItem();


            Integer timer = stack.get(ModDataComponents.TIMER.value());
            if (timer == null) continue;


            item.discard();


            var p = item.position();
            level.explode(
                    /* source */ null,
                    p.x, p.y, p.z,
                    PipeBomb.EXPLOSION_RADIUS,
                    PipeBomb.EXPLOSION_FIRE,
                    Level.ExplosionInteraction.TNT
            );
        }
    }

}
