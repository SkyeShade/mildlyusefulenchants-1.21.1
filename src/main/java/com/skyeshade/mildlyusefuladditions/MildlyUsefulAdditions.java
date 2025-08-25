package com.skyeshade.mildlyusefuladditions;

import com.mojang.serialization.Codec;
import com.skyeshade.mildlyusefuladditions.block.ModBlocks;
import com.skyeshade.mildlyusefuladditions.brewing.ModMobEffects;
import com.skyeshade.mildlyusefuladditions.datacomponents.ModDataComponents;
import com.skyeshade.mildlyusefuladditions.entity.ModEntities;
import com.skyeshade.mildlyusefuladditions.item.ModItems;
import com.skyeshade.mildlyusefuladditions.loot.ModLootModifiers;
import com.skyeshade.mildlyusefuladditions.util.TickScheduler;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


@Mod(MildlyUsefulAdditions.MODID)
public class MildlyUsefulAdditions {

    public static final String MODID = "mildlyusefuladditions";

    public static final Logger LOGGER = LogUtils.getLogger();





    public MildlyUsefulAdditions(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(
                net.neoforged.fml.config.ModConfig.Type.COMMON,
                Config.SPEC,
                MODID + "-common.toml"
        );

        NeoForge.EVENT_BUS.register(this);

        ModEntities.register(modEventBus);

        ModDataComponents.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModMobEffects.register(modEventBus);

        ModLootModifiers.GLM_CODECS.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        TickScheduler.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts

    }
}
