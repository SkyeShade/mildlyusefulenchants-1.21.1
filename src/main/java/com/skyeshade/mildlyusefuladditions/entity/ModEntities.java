package com.skyeshade.mildlyusefuladditions.entity;
// ModEntities.java
import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, MildlyUsefulAdditions.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<LongBowArrow>> LONG_BOW_ARROW =
            ENTITIES.register("long_bow_arrow",
                    () -> EntityType.Builder.<LongBowArrow>of(LongBowArrow::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build(MildlyUsefulAdditions.MODID + ":long_bow_arrow"));

    public static final DeferredHolder<EntityType<?>, EntityType<PrimedExplosive>> PRIMED_EXPLOSIVE =
            ENTITIES.register("primed_explosive",
                    () -> EntityType.Builder.<PrimedExplosive>of(PrimedExplosive::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build(MildlyUsefulAdditions.MODID + ":primed_explosive"));

    public static void register(IEventBus bus) { ENTITIES.register(bus); }
}
