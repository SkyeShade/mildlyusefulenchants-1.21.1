package com.skyeshade.mildlyusefuladditions.block;


import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import com.skyeshade.mildlyusefuladditions.item.custom.LongBow;
import com.skyeshade.mildlyusefuladditions.item.custom.PipeBomb;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MildlyUsefulAdditions.MODID);

    public static final DeferredBlock<Block> IMPROVISED_EXPLOSIVE = BLOCKS.registerBlock(
            "improvised_explosive",
            ImprovisedExplosive::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)
    );
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
