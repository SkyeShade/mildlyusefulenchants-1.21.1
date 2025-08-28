package com.skyeshade.mildlyusefuladditions.item;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.block.ImprovisedExplosive;
import com.skyeshade.mildlyusefuladditions.block.ModBlocks;
import com.skyeshade.mildlyusefuladditions.item.custom.HalberdItem;
import com.skyeshade.mildlyusefuladditions.item.custom.LongBow;
import com.skyeshade.mildlyusefuladditions.item.custom.PipeBomb;
import com.skyeshade.mildlyusefuladditions.item.custom.Staff;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
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

    public static final DeferredHolder<Item, AxeItem> WOOD_HALBERD = ITEMS.register("wooden_halberd",
            () -> new AxeItem(Tiers.WOOD,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.WOOD, 9.0F, -3.6F)))
    );
    public static final DeferredHolder<Item, AxeItem> STONE_HALBERD = ITEMS.register("stone_halberd",
            () -> new AxeItem(Tiers.STONE,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.STONE, 10.0F, -3.6F)))
    );
    public static final DeferredHolder<Item, AxeItem> IRON_HALBERD = ITEMS.register("iron_halberd",
            () -> new AxeItem(Tiers.IRON,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.IRON, 11.0F, -3.6F)))
    );
    public static final DeferredHolder<Item, AxeItem> GOLD_HALBERD = ITEMS.register("golden_halberd",
            () -> new AxeItem(Tiers.GOLD,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.GOLD, 9.0F, -3.6F)))
    );
    public static final DeferredHolder<Item, AxeItem> DIAMOND_HALBERD = ITEMS.register("diamond_halberd",
            () -> new AxeItem(Tiers.DIAMOND,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.DIAMOND, 12.0F, -3.6F)))
    );
    public static final DeferredHolder<Item, AxeItem> NETHERITE_ALBERD = ITEMS.register("netherite_halberd",
            () -> new AxeItem(Tiers.NETHERITE,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.NETHERITE, 13.0F, -3.6F)))
    );

    public static final DeferredHolder<Item, SwordItem> WOOD_SPEAR = ITEMS.register("wooden_spear",
            () -> new SwordItem(Tiers.WOOD,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.WOOD, 2.0F, -2.6F)))
    );
    public static final DeferredHolder<Item, SwordItem> STONE_SPEAR = ITEMS.register("stone_spear",
            () -> new SwordItem(Tiers.STONE,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.STONE, 3.0F, -2.6F)))
    );
    public static final DeferredHolder<Item, SwordItem> IRON_SPEAR = ITEMS.register("iron_spear",
            () -> new SwordItem(Tiers.IRON,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.IRON, 4.0F, -2.6F)))
    );
    public static final DeferredHolder<Item, SwordItem> GOLD_SPEAR = ITEMS.register("golden_spear",
            () -> new SwordItem(Tiers.GOLD,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.GOLD, 3.0F, -2.6F)))
    );
    public static final DeferredHolder<Item, SwordItem> DIAMOND_SPEAR = ITEMS.register("diamond_spear",
            () -> new SwordItem(Tiers.DIAMOND,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.DIAMOND, 5.0F, -2.6F)))
    );
    public static final DeferredHolder<Item, SwordItem> NETHERITE_SPEAR = ITEMS.register("netherite_spear",
            () -> new SwordItem(Tiers.NETHERITE,new Item.Properties().attributes(AxeItem.createAttributes(Tiers.NETHERITE, 6.0F, -2.6F)))
    );


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
