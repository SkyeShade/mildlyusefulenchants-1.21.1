package com.skyeshade.mildlyusefuladditions.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class EnhancedFortuneLootModifier extends LootModifier {
    private final int bonusMultiplier;

    private static final ResourceKey<Enchantment> ENHANCED_FORTUNE_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_fortune"));
    private static final ResourceKey<Enchantment> PERFECTED_FORTUNE_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_fortune"));

    private static final org.slf4j.Logger LOG = com.mojang.logging.LogUtils.getLogger();

    public static final MapCodec<EnhancedFortuneLootModifier> CODEC =
            RecordCodecBuilder.mapCodec(inst ->
                    LootModifier.codecStart(inst)
                            .and(
                                    Codec.INT
                                            .optionalFieldOf("bonus_multiplier", 1)
                                            .forGetter(m -> m.bonusMultiplier)
                            )
                            .apply(inst, EnhancedFortuneLootModifier::new)
            );



    protected EnhancedFortuneLootModifier(LootItemCondition[] conditionsIn, int bonusMultiplier) {
        super(conditionsIn);
        this.bonusMultiplier = Math.max(0, bonusMultiplier);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext ctx) {
        BlockState state = ctx.getParamOrNull(LootContextParams.BLOCK_STATE);
        ItemStack  tool  = ctx.getParamOrNull(LootContextParams.TOOL);
        if (state == null || tool == null) return generatedLoot;

        Level lvl = ctx.getLevel();
        if (!(lvl instanceof ServerLevel sLevel)) return generatedLoot;


        boolean harvestable = !state.requiresCorrectToolForDrops() || tool.isCorrectToolForDrops(state);
        if (!harvestable) return generatedLoot;

        var enchLookup          = sLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var silkRef             = enchLookup.getOrThrow(Enchantments.SILK_TOUCH);
        var enhancedFortuneRef  = enchLookup.getOrThrow(ENHANCED_FORTUNE_KEY);
        var perfectedFortuneRef = enchLookup.getOrThrow(PERFECTED_FORTUNE_KEY);

        int enhancedLvl  = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(enhancedFortuneRef, tool);
        int perfectedLvl = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(perfectedFortuneRef, tool);
        boolean hasSilk  = net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel(silkRef, tool) > 0;


        boolean hasCustomFortune = enhancedLvl > 0 || perfectedLvl > 0;
        if (!hasCustomFortune || bonusMultiplier <= 0) return generatedLoot;


        boolean vanillaFortunable =
                state.is(net.neoforged.neoforge.common.Tags.Blocks.ORES) ||
                        state.is(BlockTags.CROPS) ||
                        state.is(BlockTags.LEAVES) ||
                        (state.getBlock() instanceof net.minecraft.world.level.block.DropExperienceBlock);

        boolean targetBlock = vanillaFortunable;
        if (!targetBlock) return generatedLoot;


        int fortuneLike =
                (enhancedLvl > 0 ? enhancedLvl * 2 : 0) +
                        (perfectedLvl > 0 ? perfectedLvl * 3 : 0);
        if (fortuneLike <= 0) return generatedLoot;


        int extra = ctx.getRandom().nextInt(fortuneLike * bonusMultiplier + 1);
        if (extra <= 0) return generatedLoot;

        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack st = generatedLoot.get(i);
            if (st.isEmpty()) continue;

            if (hasSilk &&
                    st.getItem() instanceof net.minecraft.world.item.BlockItem bi &&
                    bi.getBlock() == state.getBlock()) {
                continue;
            }

            st.grow(extra);

        }

        return generatedLoot;
    }





    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
