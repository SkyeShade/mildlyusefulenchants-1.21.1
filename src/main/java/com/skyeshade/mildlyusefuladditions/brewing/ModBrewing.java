package com.skyeshade.mildlyusefuladditions.brewing;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public final class ModBrewing {
    @SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.MUNDANE, Items.EMERALD, Potions.LUCK);

        builder.addMix(Potions.MUNDANE, Items.ROTTEN_FLESH, ModMobEffects.NAUSEA_POTION);
        builder.addMix(ModMobEffects.NAUSEA_POTION, Items.REDSTONE, ModMobEffects.LONG_NAUSEA_POTION);

        builder.addMix(Potions.MUNDANE, Items.WITHER_ROSE, ModMobEffects.WITHER_POTION);
        builder.addMix(ModMobEffects.WITHER_POTION, Items.REDSTONE, ModMobEffects.LONG_WITHER_POTION);
        builder.addMix(ModMobEffects.WITHER_POTION, Items.GLOWSTONE_DUST, ModMobEffects.STRONG_WITHER_POTION);

        builder.addMix(Potions.MUNDANE, Items.ECHO_SHARD, ModMobEffects.DARKNESS_POTION);
        builder.addMix(ModMobEffects.DARKNESS_POTION, Items.REDSTONE, ModMobEffects.LONG_DARKNESS_POTION);

        builder.addMix(Potions.MUNDANE, Items.SAND, ModMobEffects.BLINDNESS_POTION);
        builder.addMix(ModMobEffects.BLINDNESS_POTION, Items.REDSTONE, ModMobEffects.LONG_BLINDNESS_POTION);


    }


}