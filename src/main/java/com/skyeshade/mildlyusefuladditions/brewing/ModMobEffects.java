package com.skyeshade.mildlyusefuladditions.brewing;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMobEffects {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, MildlyUsefulAdditions.MODID);

    public static final DeferredHolder<Potion, Potion> NAUSEA_POTION =
            POTIONS.register("nausea", () -> new Potion(new MobEffectInstance(MobEffects.CONFUSION, 20 * 180)));

    public static final DeferredHolder<Potion, Potion> LONG_NAUSEA_POTION = POTIONS.register("long_nausea",
            () -> new Potion(new MobEffectInstance(MobEffects.CONFUSION, 20 * 480)));

    public static final DeferredHolder<Potion, Potion> WITHER_POTION = POTIONS.register("wither",
            () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 20 * 80)));
    public static final DeferredHolder<Potion, Potion> LONG_WITHER_POTION = POTIONS.register("long_wither",
            () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 20 * 160)));

    public static final DeferredHolder<Potion, Potion> STRONG_WITHER_POTION = POTIONS.register("strong_wither",
            () -> new Potion(new MobEffectInstance(MobEffects.WITHER, 20 * 40,1)));

    public static final DeferredHolder<Potion, Potion> DARKNESS_POTION =
            POTIONS.register("darkness", () -> new Potion(new MobEffectInstance(MobEffects.DARKNESS, 20 * 60)));

    public static final DeferredHolder<Potion, Potion> LONG_DARKNESS_POTION = POTIONS.register("long_darkness",
            () -> new Potion(new MobEffectInstance(MobEffects.DARKNESS, 20 * 120)));

    public static final DeferredHolder<Potion, Potion> BLINDNESS_POTION =
            POTIONS.register("blindness", () -> new Potion(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 30)));

    public static final DeferredHolder<Potion, Potion> LONG_BLINDNESS_POTION = POTIONS.register("long_blindness",
            () -> new Potion(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 60)));


    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
