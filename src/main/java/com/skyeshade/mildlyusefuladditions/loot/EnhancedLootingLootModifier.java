package com.skyeshade.mildlyusefuladditions.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class EnhancedLootingLootModifier extends LootModifier {
    private final int bonusMultiplier;

    // 🔁 Switched to your *looting* enchant keys (NOT fortune)
    private static final ResourceKey<net.minecraft.world.item.enchantment.Enchantment> ENHANCED_LOOTING_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_looting"));
    private static final ResourceKey<net.minecraft.world.item.enchantment.Enchantment> PERFECTED_LOOTING_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_looting"));

    private static final org.slf4j.Logger LOG = com.mojang.logging.LogUtils.getLogger();

    public static final MapCodec<EnhancedLootingLootModifier> CODEC =
            RecordCodecBuilder.mapCodec(inst ->
                    LootModifier.codecStart(inst)
                            .and(Codec.INT.optionalFieldOf("bonus_multiplier", 1)
                                    .forGetter(m -> m.bonusMultiplier))
                            .apply(inst, EnhancedLootingLootModifier::new)
            );

    protected EnhancedLootingLootModifier(LootItemCondition[] conditionsIn, int bonusMultiplier) {
        super(conditionsIn);
        this.bonusMultiplier = Math.max(0, bonusMultiplier);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext ctx) {
        //  Only run for ENTITY loot. If there's no THIS_ENTITY, this is probably block loot -> ignore.
        Entity victim = ctx.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (victim == null) return generatedLoot;

        Level lvl = ctx.getLevel();
        if (!(lvl instanceof ServerLevel sLevel)) return generatedLoot;

        // Find the killer (player or mob) and their weapon.
        LivingEntity killer = null;
        Entity killerEnt = ctx.getParamOrNull(LootContextParams.ATTACKING_ENTITY);
        if (killerEnt instanceof LivingEntity le) {
            killer = le;
        } else {
            Entity direct = ctx.getParamOrNull(LootContextParams.DIRECT_ATTACKING_ENTITY);
            if (direct instanceof LivingEntity le2) killer = le2;
        }
        if (killer == null) return generatedLoot;

        ItemStack weapon = killer.getMainHandItem();
        if (weapon.isEmpty()) return generatedLoot;

        // Read ONLY your custom looting enchants (so vanilla Looting isn’t modified)
        HolderLookup.RegistryLookup<net.minecraft.world.item.enchantment.Enchantment> enchLookup =
                sLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder.Reference<net.minecraft.world.item.enchantment.Enchantment> enhancedLootingRef =
                enchLookup.getOrThrow(ENHANCED_LOOTING_KEY);
        Holder.Reference<net.minecraft.world.item.enchantment.Enchantment> perfectedLootingRef =
                enchLookup.getOrThrow(PERFECTED_LOOTING_KEY);

        int enhancedLvl  = EnchantmentHelper.getItemEnchantmentLevel(enhancedLootingRef, weapon);
        int perfectedLvl = EnchantmentHelper.getItemEnchantmentLevel(perfectedLootingRef, weapon);

        // No custom looting -> do nothing (preserves vanilla behavior entirely)
        if (bonusMultiplier <= 0) return generatedLoot;
        if (enhancedLvl <= 0 && perfectedLvl <= 0) return generatedLoot;

        // Strength only from YOUR enchants (choose weights to taste)
        int lootingLike = enhancedLvl * 3 + perfectedLvl * 5;
        if (lootingLike <= 0) return generatedLoot;

        int extra = ctx.getRandom().nextInt(lootingLike * bonusMultiplier + 1); // 0..N
        if (extra <= 0) return generatedLoot;

        // Apply to existing item drops (don’t invent new item types)
        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack st = generatedLoot.get(i);
            if (st.isEmpty()) continue;

            // If you want to avoid boosting equipment drops (armor/weapons), you can skip damageable items:
            // if (st.isDamageableItem()) continue;

            st.grow(extra);
            // If you only want to boost the primary/largest stack, break after growing the first one.
            // break;
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}