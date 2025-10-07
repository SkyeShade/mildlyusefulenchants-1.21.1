package com.skyeshade.mildlyusefuladditions.item;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public final class ModCreativeModeTabs {

    @SubscribeEvent
    public static void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        // COMBAT tab
        if (event.getTabKey().equals(CreativeModeTabs.COMBAT)) {
            event.insertAfter(Items.BOW.getDefaultInstance(), ModItems.LONG_BOW.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS );
            event.insertAfter(Items.WIND_CHARGE.getDefaultInstance(), ModItems.PIPE_BOMB.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS );
            event.insertAfter(Items.TNT.getDefaultInstance(), ModItems.IMPROVISED_EXPLOSIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS );
            //event.insertAfter(Items.MACE.getDefaultInstance(), ModItems.STAFF.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS );
        }
        if (event.getTabKey().equals(CreativeModeTabs.REDSTONE_BLOCKS)) {

            event.insertAfter(Items.TNT.getDefaultInstance(), ModItems.IMPROVISED_EXPLOSIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS );
        }
    }
}