package com.skyeshade.mildlyusefuladditions.menu;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;



public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MildlyUsefulAdditions.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<StaffMenu>> STAFF =
            MENUS.register("staff", () ->
                    IMenuTypeExtension.create((id, inv, buf) -> new StaffMenu(id, inv, buf))
            );
}