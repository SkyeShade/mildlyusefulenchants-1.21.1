package com.skyeshade.mildlyusefuladditions.item;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.block.ImprovisedExplosive;
import com.skyeshade.mildlyusefuladditions.block.ModBlocks;
import com.skyeshade.mildlyusefuladditions.item.custom.LongBow;
import com.skyeshade.mildlyusefuladditions.item.custom.PipeBomb;
import com.skyeshade.mildlyusefuladditions.item.custom.Staff;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.tools.Tool;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, MildlyUsefulAdditions.MODID);

    public static final DeferredHolder<Item, BowItem> LONG_BOW = ITEMS.register("long_bow",
            () -> new LongBow(new Item.Properties().durability(541)));
    public static final DeferredHolder<Item, Item> PIPE_BOMB = ITEMS.register("pipe_bomb",
            () -> new PipeBomb(new Item.Properties().stacksTo(16)));
    public static final DeferredHolder<Item, BlockItem> IMPROVISED_EXPLOSIVE = ITEMS.register("improvised_explosive",
            () -> new BlockItem(ModBlocks.IMPROVISED_EXPLOSIVE.get(), new Item.Properties()));

    public static final DeferredHolder<Item, Item> STAFF = ITEMS.register("staff",
            () -> new Staff(new Item.Properties().durability(235)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
