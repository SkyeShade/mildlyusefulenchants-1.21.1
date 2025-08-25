package com.skyeshade.mildlyusefuladditions.loot;

import com.mojang.serialization.MapCodec;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModLootModifiers {


    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>>
            GLM_CODECS = DeferredRegister.create(
            net.neoforged.neoforge.registries.NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS,
            MildlyUsefulAdditions.MODID
    );

    public static final Supplier<MapCodec<EnhancedFortuneLootModifier>> ENHANCED_FORTUNE =
            GLM_CODECS.register("enhanced_fortune", () -> EnhancedFortuneLootModifier.CODEC);

    public static final Supplier<MapCodec<EnhancedLootingLootModifier>> ENHANCED_LOOTING =
            GLM_CODECS.register("enhanced_looting", () -> EnhancedLootingLootModifier.CODEC);
    private ModLootModifiers() {}
}
