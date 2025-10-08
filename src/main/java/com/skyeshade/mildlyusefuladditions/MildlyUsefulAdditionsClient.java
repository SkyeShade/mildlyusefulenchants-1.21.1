package com.skyeshade.mildlyusefuladditions;

import com.skyeshade.mildlyusefuladditions.client.render.PrimedExplosiveRenderer;
import com.skyeshade.mildlyusefuladditions.client.screen.StaffScreen;
import com.skyeshade.mildlyusefuladditions.entity.ModEntities;
import com.skyeshade.mildlyusefuladditions.item.ModItems;
import com.skyeshade.mildlyusefuladditions.item.custom.LongBow;
import com.skyeshade.mildlyusefuladditions.menu.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;


@Mod(value = MildlyUsefulAdditions.MODID, dist = Dist.CLIENT)

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID, value = Dist.CLIENT)
public class MildlyUsefulAdditionsClient {
    public MildlyUsefulAdditionsClient(ModContainer container) {

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.STAFF.get(), StaffScreen::new);
    }
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {



        EntityRenderers.register(ModEntities.PRIMED_EXPLOSIVE.get(), PrimedExplosiveRenderer::new);
        registerItemProperties();

    }
    public static void registerItemProperties() {


        ItemProperties.register(ModItems.LONG_BOW.get(), ResourceLocation.withDefaultNamespace("charging"),
                (stack, world, entity, seed) -> {
                    if (entity != null && entity.isUsingItem() && entity.getUseItem() == stack) {
                        return 1.0F;
                    }
                    return 0.0F;
                });
        ItemProperties.register(ModItems.LONG_BOW.get(),
                ResourceLocation.withDefaultNamespace( "charge"),
                (ItemStack stack, ClientLevel level, LivingEntity entity, int seed) -> {
                    if (entity != null && entity.isUsingItem() && entity.getUseItem() == stack) {
                        int useDuration = stack.getUseDuration(entity) - entity.getUseItemRemainingTicks();

                        if (useDuration >= LongBow.getMaxDrawTicks(stack, level)) return 4.0F;
                        if (useDuration >= LongBow.getMaxDrawTicks(stack, level) * 0.80) return 3.0F;
                        if (useDuration >= LongBow.getMaxDrawTicks(stack, level) * 0.50) return 2.0F;
                        return 1.0F;
                    }
                    return 0.0F;
                });
    }
}
