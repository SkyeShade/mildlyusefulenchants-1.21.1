package com.skyeshade.mildlyusefuladditions.datacomponents;

import com.mojang.serialization.Codec;
import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.item.custom.LongBow;
import com.skyeshade.mildlyusefuladditions.item.custom.PipeBomb;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class ModDataComponents {


    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MildlyUsefulAdditions.MODID);


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TIMER =
            DATA_COMPONENTS.registerComponentType("timer", b -> b
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> IGNITER_UUID =
            DATA_COMPONENTS.registerComponentType("igniter_uuid", b -> b
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
            );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> IGNITER_NAME =
            DATA_COMPONENTS.registerComponentType("igniter_name", b -> b
                    .persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> STAFF_STORAGE =
            DATA_COMPONENTS.registerComponentType("staff_storage", b -> b
                    .persistent(ItemContainerContents.CODEC)
                    .networkSynchronized(ItemContainerContents.STREAM_CODEC)
            );



    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}
