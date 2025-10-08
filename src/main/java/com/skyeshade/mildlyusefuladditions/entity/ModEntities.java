package com.skyeshade.mildlyusefuladditions.entity;
// ModEntities.java
import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, MildlyUsefulAdditions.MODID);


    public static final DeferredHolder<EntityType<?>, EntityType<PrimedExplosive>> PRIMED_EXPLOSIVE =
            ENTITIES.register("primed_explosive",
                    () -> EntityType.Builder.<PrimedExplosive>of(PrimedExplosive::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build(MildlyUsefulAdditions.MODID + ":primed_explosive"));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownSpear>> THROWN_SPEAR =
            ENTITIES.register("thrown_spear",
                    () -> EntityType.Builder.<ThrownSpear>of(ThrownSpear::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build(ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "thrown_spear").toString())
            );
    public static void register(IEventBus bus) { ENTITIES.register(bus); }
}
