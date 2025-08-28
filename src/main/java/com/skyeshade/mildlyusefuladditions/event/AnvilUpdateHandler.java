package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class AnvilUpdateHandler {

    static final ResourceLocation WISDOM_RL   = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "wisdom");
    static final ResourceLocation ADV_WISDOM_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "advanced_wisdom");
    static final ResourceLocation MAS_WISDOM_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "masterful_wisdom");

    static final ResourceLocation BANE_OF_ARTHROPODS_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "bane_of_arthropods");
    static final ResourceLocation ENH_BANE_OF_ARTHROPODS_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_bane_of_arthropods");
    static final ResourceLocation PER_BANE_OF_ARTHROPODS_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_bane_of_arthropods");

    static final ResourceLocation BANE_OF_RAIDING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "bane_of_raiding");
    static final ResourceLocation ENH_BANE_OF_RAIDING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_bane_of_raiding");
    static final ResourceLocation PER_BANE_OF_RAIDING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_bane_of_raiding");

    static final ResourceLocation BANE_OF_SLIME_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "bane_of_slime");
    static final ResourceLocation ENH_BANE_OF_SLIME_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_bane_of_slime");
    static final ResourceLocation PER_BANE_OF_SLIME_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_bane_of_slime");

    static final ResourceLocation BLAST_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "blast_protection");
    static final ResourceLocation ENH_BLAST_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_blast_protection");
    static final ResourceLocation PER_BLAST_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_blast_protection");

    static final ResourceLocation BREACH_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "breach");
    static final ResourceLocation ENH_BREACH_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_breach");
    static final ResourceLocation PER_BREACH_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_breach");

    static final ResourceLocation BULWARK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "bulwark");
    static final ResourceLocation ENH_BULWARK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_bulwark");
    static final ResourceLocation PER_BULWARK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_bulwark");

    static final ResourceLocation DENSITY_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "density");
    static final ResourceLocation ENH_DENSITY_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_density");
    static final ResourceLocation PER_DENSITY_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_density");

    static final ResourceLocation DEPTH_STRIDER_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "depth_strider");
    static final ResourceLocation ENH_DEPTH_STRIDER_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_depth_strider");
    static final ResourceLocation PER_DEPTH_STRIDER_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_depth_strider");

    static final ResourceLocation EFFICIENCY_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "efficiency");
    static final ResourceLocation ENH_EFFICIENCY_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_efficiency");
    static final ResourceLocation PER_EFFICIENCY_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_efficiency");

    static final ResourceLocation ELEMENTAL_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "elemental_smite");
    static final ResourceLocation ENH_ELEMENTAL_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_elemental_smite");
    static final ResourceLocation PER_ELEMENTAL_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_elemental_smite");

    static final ResourceLocation ENDSMIT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "endsmit");
    static final ResourceLocation ENH_ENDSMIT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_endsmit");
    static final ResourceLocation PER_ENDSMIT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_endsmit");

    static final ResourceLocation FEATHER_FALLING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "feather_falling");
    static final ResourceLocation ENH_FEATHER_FALLING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_feather_falling");
    static final ResourceLocation PER_FEATHER_FALLING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_feather_falling");

    static final ResourceLocation FIRE_ASPECT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "fire_aspect");
    static final ResourceLocation ENH_FIRE_ASPECT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_fire_aspect");
    static final ResourceLocation PER_FIRE_ASPECT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_fire_aspect");

    static final ResourceLocation FIRE_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "fire_protection");
    static final ResourceLocation ENH_FIRE_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_fire_protection");
    static final ResourceLocation PER_FIRE_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_fire_protection");

    static final ResourceLocation FOOTWORK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "footwork");
    static final ResourceLocation ENH_FOOTWORK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_footwork");
    static final ResourceLocation PER_FOOTWORK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_footwork");

    static final ResourceLocation FORTUNE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "fortune");
    static final ResourceLocation ENH_FORTUNE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_fortune");
    static final ResourceLocation PER_FORTUNE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_fortune");

    static final ResourceLocation FROST_WALKER_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "frost_walker");
    static final ResourceLocation ENH_FROST_WALKER_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_frost_walker");
    static final ResourceLocation PER_FROST_WALKER_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_frost_walker");

    static final ResourceLocation IMPALING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "impaling");
    static final ResourceLocation ENH_IMPALING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_impaling");
    static final ResourceLocation PER_IMPALING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_impaling");

    static final ResourceLocation KNOCKBACK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "knockback");
    static final ResourceLocation ENH_KNOCKBACK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_knockback");
    static final ResourceLocation PER_KNOCKBACK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_knockback");

    static final ResourceLocation LEAP_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "leap");
    static final ResourceLocation ENH_LEAP_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_leap");
    static final ResourceLocation PER_LEAP_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_leap");

    static final ResourceLocation LEGWORK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "legwork");
    static final ResourceLocation ENH_LEGWORK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_legwork");
    static final ResourceLocation PER_LEGWORK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_legwork");

    static final ResourceLocation LOOTING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "looting");
    static final ResourceLocation ENH_LOOTING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_looting");
    static final ResourceLocation PER_LOOTING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_looting");

    static final ResourceLocation LOTS_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "lots");
    static final ResourceLocation ENH_LOTS_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_lots");
    static final ResourceLocation PER_LOTS_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_lots");

    static final ResourceLocation LOYALTY_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "loyalty");
    static final ResourceLocation ENH_LOYALTY_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_loyalty");
    static final ResourceLocation PER_LOYALTY_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_loyalty");

    static final ResourceLocation LURE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "lure");
    static final ResourceLocation ENH_LURE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_lure");
    static final ResourceLocation PER_LURE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_lure");

    static final ResourceLocation PIERCING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "piercing");
    static final ResourceLocation ENH_PIERCING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_piercing");
    static final ResourceLocation PER_PIERCING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_piercing");

    static final ResourceLocation POWER_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "power");
    static final ResourceLocation ENH_POWER_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_power");
    static final ResourceLocation PER_POWER_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_power");

    static final ResourceLocation PROJECTILE_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "projectile_protection");
    static final ResourceLocation ENH_PROJECTILE_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_projectile_protection");
    static final ResourceLocation PER_PROJECTILE_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_projectile_protection");

    static final ResourceLocation PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "protection");
    static final ResourceLocation ENH_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_protection");
    static final ResourceLocation PER_PROTECTION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_protection");

    static final ResourceLocation PUNCH_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "punch");
    static final ResourceLocation ENH_PUNCH_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_punch");
    static final ResourceLocation PER_PUNCH_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_punch");

    static final ResourceLocation QUICK_CHARGE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "quick_charge");
    static final ResourceLocation ENH_QUICK_CHARGE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_quick_charge");
    static final ResourceLocation PER_QUICK_CHARGE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_quick_charge");

    static final ResourceLocation REACH_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "reach");
    static final ResourceLocation ENH_REACH_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_reach");
    static final ResourceLocation PER_REACH_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_reach");

    static final ResourceLocation RESPIRATION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "respiration");
    static final ResourceLocation ENH_RESPIRATION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_respiration");
    static final ResourceLocation PER_RESPIRATION_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_respiration");

    static final ResourceLocation RIPTIDE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "riptide");
    static final ResourceLocation ENH_RIPTIDE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_riptide");
    static final ResourceLocation PER_RIPTIDE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_riptide");

    static final ResourceLocation ROTTEN_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "rotten_smite");
    static final ResourceLocation ENH_ROTTEN_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_rotten_smite");
    static final ResourceLocation PER_ROTTEN_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_rotten_smite");

    static final ResourceLocation SCULK_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "sculk_smite");
    static final ResourceLocation ENH_SCULK_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_sculk_smite");
    static final ResourceLocation PER_SCULK_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_sculk_smite");

    static final ResourceLocation SHARPNESS_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "sharpness");
    static final ResourceLocation ENH_SHARPNESS_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_sharpness");
    static final ResourceLocation PER_SHARPNESS_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_sharpness");

    static final ResourceLocation SLEIGHT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "sleight");
    static final ResourceLocation ENH_SLEIGHT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_sleight");
    static final ResourceLocation PER_SLEIGHT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_sleight");

    static final ResourceLocation SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "smite");
    static final ResourceLocation ENH_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_smite");
    static final ResourceLocation PER_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_smite");

    static final ResourceLocation SOUL_SPEED_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "soul_speed");
    static final ResourceLocation ENH_SOUL_SPEED_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_soul_speed");
    static final ResourceLocation PER_SOUL_SPEED_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_soul_speed");

    static final ResourceLocation SWEEPING_EDGE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "sweeping_edge");
    static final ResourceLocation ENH_SWEEPING_EDGE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_sweeping_edge");
    static final ResourceLocation PER_SWEEPING_EDGE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_sweeping_edge");

    static final ResourceLocation SWIFT_SNEAK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "swift_sneak");
    static final ResourceLocation ENH_SWIFT_SNEAK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_swift_sneak");
    static final ResourceLocation PER_SWIFT_SNEAK_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_swift_sneak");

    static final ResourceLocation UNBREAKING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "unbreaking");
    static final ResourceLocation ENH_UNBREAKING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_unbreaking");
    static final ResourceLocation PER_UNBREAKING_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_unbreaking");

    static final ResourceLocation WITHER_ASPECT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "wither_aspect");
    static final ResourceLocation ENH_WITHER_ASPECT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_wither_aspect");
    static final ResourceLocation PER_WITHER_ASPECT_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_wither_aspect");

    static final ResourceLocation WITHERED_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "withered_smite");
    static final ResourceLocation ENH_WITHERED_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "enhanced_withered_smite");
    static final ResourceLocation PER_WITHERED_SMITE_RL = ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "perfected_withered_smite");

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left  = event.getLeft();
        ItemStack right = event.getRight();
        if (left.isEmpty() || right.isEmpty()) return;

        if (!left.is(Items.ENCHANTED_BOOK) || !right.is(Items.ECHO_SHARD)) return;


        HolderLookup.RegistryLookup<Enchantment> enchantsLookup =
                event.getPlayer().level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);


        // Holders for bane_of_arthropods
        Holder<Enchantment> baneofarthropodsH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, BANE_OF_ARTHROPODS_RL)).orElse(null);
        Holder<Enchantment> enhBaneofarthropodsH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_BANE_OF_ARTHROPODS_RL)).orElse(null);
        Holder<Enchantment> perBaneofarthropodsH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_BANE_OF_ARTHROPODS_RL)).orElse(null);

        // Holders for bane_of_raiding
        Holder<Enchantment> baneofraidingH    = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, BANE_OF_RAIDING_RL)).orElse(null);
        Holder<Enchantment> enhBaneofraidingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_BANE_OF_RAIDING_RL)).orElse(null);
        Holder<Enchantment> perBaneofraidingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_BANE_OF_RAIDING_RL)).orElse((null));

        // Holders for bane_of_slime
        Holder<Enchantment> baneofslimeH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, BANE_OF_SLIME_RL)).orElse(null);
        Holder<Enchantment> enhBaneofslimeH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_BANE_OF_SLIME_RL)).orElse(null);
        Holder<Enchantment> perBaneofslimeH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_BANE_OF_SLIME_RL)).orElse(null);

        // Holders for blast_protection
        Holder<Enchantment> blastprotectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, BLAST_PROTECTION_RL)).orElse(null);
        Holder<Enchantment> enhBlastprotectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_BLAST_PROTECTION_RL)).orElse(null);
        Holder<Enchantment> perBlastprotectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_BLAST_PROTECTION_RL)).orElse(null);

        // Holders for breach
        Holder<Enchantment> breachH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, BREACH_RL)).orElse(null);
        Holder<Enchantment> enhBreachH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_BREACH_RL)).orElse(null);
        Holder<Enchantment> perBreachH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_BREACH_RL)).orElse(null);

        // Holders for bulwark
        Holder<Enchantment> bulwarkH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, BULWARK_RL)).orElse(null);
        Holder<Enchantment> enhBulwarkH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_BULWARK_RL)).orElse(null);
        Holder<Enchantment> perBulwarkH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_BULWARK_RL)).orElse(null);

        // Holders for density
        Holder<Enchantment> densityH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, DENSITY_RL)).orElse(null);
        Holder<Enchantment> enhDensityH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_DENSITY_RL)).orElse(null);
        Holder<Enchantment> perDensityH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_DENSITY_RL)).orElse(null);

        // Holders for depth_strider
        Holder<Enchantment> depthstriderH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, DEPTH_STRIDER_RL)).orElse(null);
        Holder<Enchantment> enhDepthstriderH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_DEPTH_STRIDER_RL)).orElse(null);
        Holder<Enchantment> perDepthstriderH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_DEPTH_STRIDER_RL)).orElse(null);

        // Holders for efficiency
        Holder<Enchantment> efficiencyH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, EFFICIENCY_RL)).orElse(null);
        Holder<Enchantment> enhEfficiencyH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_EFFICIENCY_RL)).orElse(null);
        Holder<Enchantment> perEfficiencyH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_EFFICIENCY_RL)).orElse(null);

        // Holders for elemental_smite
        Holder<Enchantment> elementalsmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ELEMENTAL_SMITE_RL)).orElse(null);
        Holder<Enchantment> enhElementalsmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_ELEMENTAL_SMITE_RL)).orElse(null);
        Holder<Enchantment> perElementalsmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_ELEMENTAL_SMITE_RL)).orElse(null);

        // Holders for endsmit
        Holder<Enchantment> endsmitH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENDSMIT_RL)).orElse(null);
        Holder<Enchantment> enhEndsmitH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_ENDSMIT_RL)).orElse(null);
        Holder<Enchantment> perEndsmitH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_ENDSMIT_RL)).orElse(null);

        // Holders for feather_falling
        Holder<Enchantment> featherfallingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, FEATHER_FALLING_RL)).orElse(null);
        Holder<Enchantment> enhFeatherfallingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_FEATHER_FALLING_RL)).orElse(null);
        Holder<Enchantment> perFeatherfallingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_FEATHER_FALLING_RL)).orElse(null);

        // Holders for fire_aspect
        Holder<Enchantment> fireaspectH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, FIRE_ASPECT_RL)).orElse(null);
        Holder<Enchantment> enhFireaspectH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_FIRE_ASPECT_RL)).orElse(null);
        Holder<Enchantment> perFireaspectH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_FIRE_ASPECT_RL)).orElse(null);

        // Holders for fire_protection
        Holder<Enchantment> fireprotectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, FIRE_PROTECTION_RL)).orElse(null);
        Holder<Enchantment> enhFireprotectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_FIRE_PROTECTION_RL)).orElse(null);
        Holder<Enchantment> perFireprotectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_FIRE_PROTECTION_RL)).orElse(null);

        // Holders for footwork
        Holder<Enchantment> footworkH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, FOOTWORK_RL)).orElse(null);
        Holder<Enchantment> enhFootworkH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_FOOTWORK_RL)).orElse(null);
        Holder<Enchantment> perFootworkH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_FOOTWORK_RL)).orElse(null);

        // Holders for fortune
        Holder<Enchantment> fortuneH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, FORTUNE_RL)).orElse(null);
        Holder<Enchantment> enhFortuneH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_FORTUNE_RL)).orElse(null);
        Holder<Enchantment> perFortuneH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_FORTUNE_RL)).orElse(null);

        // Holders for frost_walker
        Holder<Enchantment> frostwalkerH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, FROST_WALKER_RL)).orElse(null);
        Holder<Enchantment> enhFrostwalkerH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_FROST_WALKER_RL)).orElse(null);
        Holder<Enchantment> perFrostwalkerH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_FROST_WALKER_RL)).orElse(null);

        // Holders for impaling
        Holder<Enchantment> impalingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, IMPALING_RL)).orElse(null);
        Holder<Enchantment> enhImpalingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_IMPALING_RL)).orElse(null);
        Holder<Enchantment> perImpalingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_IMPALING_RL)).orElse(null);

        // Holders for knockback
        Holder<Enchantment> knockbackH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, KNOCKBACK_RL)).orElse(null);
        Holder<Enchantment> enhKnockbackH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_KNOCKBACK_RL)).orElse(null);
        Holder<Enchantment> perKnockbackH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_KNOCKBACK_RL)).orElse(null);

        // Holders for leap
        Holder<Enchantment> leapH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, LEAP_RL)).orElse(null);
        Holder<Enchantment> enhLeapH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_LEAP_RL)).orElse(null);
        Holder<Enchantment> perLeapH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_LEAP_RL)).orElse(null);

        // Holders for legwork
        Holder<Enchantment> legworkH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, LEGWORK_RL)).orElse(null);
        Holder<Enchantment> enhLegworkH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_LEGWORK_RL)).orElse(null);
        Holder<Enchantment> perLegworkH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_LEGWORK_RL)).orElse(null);

        // Holders for looting
        Holder<Enchantment> lootingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, LOOTING_RL)).orElse(null);
        Holder<Enchantment> enhLootingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_LOOTING_RL)).orElse(null);
        Holder<Enchantment> perLootingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_LOOTING_RL)).orElse(null);

        // Holders for lots
        Holder<Enchantment> lotsH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, LOTS_RL)).orElse(null);
        Holder<Enchantment> enhLotsH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_LOTS_RL)).orElse(null);
        Holder<Enchantment> perLotsH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_LOTS_RL)).orElse(null);

        // Holders for loyalty
        Holder<Enchantment> loyaltyH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, LOYALTY_RL)).orElse(null);
        Holder<Enchantment> enhLoyaltyH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_LOYALTY_RL)).orElse(null);
        Holder<Enchantment> perLoyaltyH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_LOYALTY_RL)).orElse(null);

        // Holders for lure
        Holder<Enchantment> lureH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, LURE_RL)).orElse(null);
        Holder<Enchantment> enhLureH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_LURE_RL)).orElse(null);
        Holder<Enchantment> perLureH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_LURE_RL)).orElse(null);

        // Holders for piercing
        Holder<Enchantment> piercingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PIERCING_RL)).orElse(null);
        Holder<Enchantment> enhPiercingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_PIERCING_RL)).orElse(null);
        Holder<Enchantment> perPiercingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_PIERCING_RL)).orElse(null);

        // Holders for power
        Holder<Enchantment> powerH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, POWER_RL)).orElse(null);
        Holder<Enchantment> enhPowerH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_POWER_RL)).orElse(null);
        Holder<Enchantment> perPowerH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_POWER_RL)).orElse(null);

        // Holders for projectile_protection
        Holder<Enchantment> projectileprotectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PROJECTILE_PROTECTION_RL)).orElse(null);
        Holder<Enchantment> enhProjectileprotectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_PROJECTILE_PROTECTION_RL)).orElse(null);
        Holder<Enchantment> perProjectileprotectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_PROJECTILE_PROTECTION_RL)).orElse(null);

        // Holders for protection
        Holder<Enchantment> protectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PROTECTION_RL)).orElse(null);
        Holder<Enchantment> enhProtectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_PROTECTION_RL)).orElse(null);
        Holder<Enchantment> perProtectionH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_PROTECTION_RL)).orElse(null);

        // Holders for punch
        Holder<Enchantment> punchH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PUNCH_RL)).orElse(null);
        Holder<Enchantment> enhPunchH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_PUNCH_RL)).orElse(null);
        Holder<Enchantment> perPunchH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_PUNCH_RL)).orElse(null);

        // Holders for quick_charge
        Holder<Enchantment> quickchargeH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, QUICK_CHARGE_RL)).orElse(null);
        Holder<Enchantment> enhQuickchargeH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_QUICK_CHARGE_RL)).orElse(null);
        Holder<Enchantment> perQuickchargeH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_QUICK_CHARGE_RL)).orElse(null);

        // Holders for reach
        Holder<Enchantment> reachH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, REACH_RL)).orElse(null);
        Holder<Enchantment> enhReachH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_REACH_RL)).orElse(null);
        Holder<Enchantment> perReachH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_REACH_RL)).orElse(null);

        // Holders for respiration
        Holder<Enchantment> respirationH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, RESPIRATION_RL)).orElse(null);
        Holder<Enchantment> enhRespirationH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_RESPIRATION_RL)).orElse(null);
        Holder<Enchantment> perRespirationH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_RESPIRATION_RL)).orElse(null);

        // Holders for riptide
        Holder<Enchantment> riptideH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, RIPTIDE_RL)).orElse(null);
        Holder<Enchantment> enhRiptideH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_RIPTIDE_RL)).orElse(null);
        Holder<Enchantment> perRiptideH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_RIPTIDE_RL)).orElse(null);

        // Holders for rotten_smite
        Holder<Enchantment> rottensmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ROTTEN_SMITE_RL)).orElse(null);
        Holder<Enchantment> enhRottensmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_ROTTEN_SMITE_RL)).orElse(null);
        Holder<Enchantment> perRottensmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_ROTTEN_SMITE_RL)).orElse(null);

        // Holders for sculk_smite
        Holder<Enchantment> sculksmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, SCULK_SMITE_RL)).orElse(null);
        Holder<Enchantment> enhSculksmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_SCULK_SMITE_RL)).orElse(null);
        Holder<Enchantment> perSculksmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_SCULK_SMITE_RL)).orElse(null);

        // Holders for sharpness
        Holder<Enchantment> sharpnessH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, SHARPNESS_RL)).orElse(null);
        Holder<Enchantment> enhSharpnessH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_SHARPNESS_RL)).orElse(null);
        Holder<Enchantment> perSharpnessH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_SHARPNESS_RL)).orElse(null);

        // Holders for sleight
        Holder<Enchantment> sleightH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, SLEIGHT_RL)).orElse(null);
        Holder<Enchantment> enhSleightH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_SLEIGHT_RL)).orElse(null);
        Holder<Enchantment> perSleightH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_SLEIGHT_RL)).orElse(null);

        // Holders for smite
        Holder<Enchantment> smiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, SMITE_RL)).orElse(null);
        Holder<Enchantment> enhSmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_SMITE_RL)).orElse(null);
        Holder<Enchantment> perSmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_SMITE_RL)).orElse(null);

        // Holders for soul_speed
        Holder<Enchantment> soulspeedH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, SOUL_SPEED_RL)).orElse(null);
        Holder<Enchantment> enhSoulspeedH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_SOUL_SPEED_RL)).orElse(null);
        Holder<Enchantment> perSoulspeedH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_SOUL_SPEED_RL)).orElse(null);

        // Holders for sweeping_edge
        Holder<Enchantment> sweepingedgeH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, SWEEPING_EDGE_RL)).orElse(null);
        Holder<Enchantment> enhSweepingedgeH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_SWEEPING_EDGE_RL)).orElse(null);
        Holder<Enchantment> perSweepingedgeH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_SWEEPING_EDGE_RL)).orElse(null);

        // Holders for swift_sneak
        Holder<Enchantment> swiftsneakH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, SWIFT_SNEAK_RL)).orElse(null);
        Holder<Enchantment> enhSwiftsneakH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_SWIFT_SNEAK_RL)).orElse(null);
        Holder<Enchantment> perSwiftsneakH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_SWIFT_SNEAK_RL)).orElse(null);

        // Holders for unbreaking
        Holder<Enchantment> unbreakingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, UNBREAKING_RL)).orElse(null);
        Holder<Enchantment> enhUnbreakingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_UNBREAKING_RL)).orElse(null);
        Holder<Enchantment> perUnbreakingH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_UNBREAKING_RL)).orElse(null);

        // Holders for wither_aspect
        Holder<Enchantment> witheraspectH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, WITHER_ASPECT_RL)).orElse(null);
        Holder<Enchantment> enhWitheraspectH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_WITHER_ASPECT_RL)).orElse(null);
        Holder<Enchantment> perWitheraspectH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_WITHER_ASPECT_RL)).orElse(null);

        // Holders for withered_smite
        Holder<Enchantment> witheredsmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, WITHERED_SMITE_RL)).orElse(null);
        Holder<Enchantment> enhWitheredsmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ENH_WITHERED_SMITE_RL)).orElse(null);
        Holder<Enchantment> perWitheredsmiteH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, PER_WITHERED_SMITE_RL)).orElse(null);

        Holder<Enchantment> wisdomH    = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, WISDOM_RL)).orElse(null);
        Holder<Enchantment> advWisdomH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, ADV_WISDOM_RL)).orElse(null);
        Holder<Enchantment> masWisdomH = enchantsLookup.get(ResourceKey.create(Registries.ENCHANTMENT, MAS_WISDOM_RL)).orElse((null));

        ItemEnchantments stored = left.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

        // All Enchantments Here;
        if (
                wisdomH == null || advWisdomH == null || masWisdomH == null ||
                baneofarthropodsH == null || enhBaneofarthropodsH == null || perBaneofarthropodsH == null ||
                baneofraidingH == null || enhBaneofraidingH == null || perBaneofraidingH == null ||
                baneofslimeH == null || enhBaneofslimeH == null || perBaneofslimeH == null ||
                blastprotectionH == null || enhBlastprotectionH == null || perBlastprotectionH == null ||
                breachH == null || enhBreachH == null || perBreachH == null ||
                bulwarkH == null || enhBulwarkH == null || perBulwarkH == null ||
                densityH == null || enhDensityH == null || perDensityH == null ||
                depthstriderH == null || enhDepthstriderH == null || perDepthstriderH == null ||
                efficiencyH == null || enhEfficiencyH == null || perEfficiencyH == null ||
                elementalsmiteH == null || enhElementalsmiteH == null || perElementalsmiteH == null ||
                endsmitH == null || enhEndsmitH == null || perEndsmitH == null ||
                featherfallingH == null || enhFeatherfallingH == null || perFeatherfallingH == null ||
                fireaspectH == null || enhFireaspectH == null || perFireaspectH == null ||
                fireprotectionH == null || enhFireprotectionH == null || perFireprotectionH == null ||
                footworkH == null || enhFootworkH == null || perFootworkH == null ||
                fortuneH == null || enhFortuneH == null || perFortuneH == null ||
                frostwalkerH == null || enhFrostwalkerH == null || perFrostwalkerH == null ||
                impalingH == null || enhImpalingH == null || perImpalingH == null ||
                knockbackH == null || enhKnockbackH == null || perKnockbackH == null ||
                leapH == null || enhLeapH == null || perLeapH == null ||
                legworkH == null || enhLegworkH == null || perLegworkH == null ||
                lootingH == null || enhLootingH == null || perLootingH == null ||
                lotsH == null || enhLotsH == null || perLotsH == null ||
                loyaltyH == null || enhLoyaltyH == null || perLoyaltyH == null ||
                lureH == null || enhLureH == null || perLureH == null ||
                piercingH == null || enhPiercingH == null || perPiercingH == null ||
                powerH == null || enhPowerH == null || perPowerH == null ||
                projectileprotectionH == null || enhProjectileprotectionH == null || perProjectileprotectionH == null ||
                protectionH == null || enhProtectionH == null || perProtectionH == null ||
                punchH == null || enhPunchH == null || perPunchH == null ||
                quickchargeH == null || enhQuickchargeH == null || perQuickchargeH == null ||
                reachH == null || enhReachH == null || perReachH == null ||
                respirationH == null || enhRespirationH == null || perRespirationH == null ||
                riptideH == null || enhRiptideH == null || perRiptideH == null ||
                rottensmiteH == null || enhRottensmiteH == null || perRottensmiteH == null ||
                sculksmiteH == null || enhSculksmiteH == null || perSculksmiteH == null ||
                sharpnessH == null || enhSharpnessH == null || perSharpnessH == null ||
                sleightH == null || enhSleightH == null || perSleightH == null ||
                smiteH == null || enhSmiteH == null || perSmiteH == null ||
                soulspeedH == null || enhSoulspeedH == null || perSoulspeedH == null ||
                sweepingedgeH == null || enhSweepingedgeH == null || perSweepingedgeH == null ||
                swiftsneakH == null || enhSwiftsneakH == null || perSwiftsneakH == null ||
                unbreakingH == null || enhUnbreakingH == null || perUnbreakingH == null ||
                witheraspectH == null || enhWitheraspectH == null || perWitheraspectH == null ||
                witheredsmiteH == null || enhWitheredsmiteH == null || perWitheredsmiteH == null
        ) return;

        // WISDOM → ADV. WISDOM
        if (stored.getLevel(wisdomH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(wisdomH, 0);
            mut.set(advWisdomH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());

            
            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(2);
        }
        // ADV. WISDOM → MAS. WISDOM
        if (stored.getLevel(advWisdomH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(advWisdomH, 0);
            mut.set(masWisdomH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(3);
        }
        // BANE OF ARTHROPODS → ENHANCED BANE OF ARTHROPODS
        if (stored.getLevel(baneofarthropodsH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(baneofarthropodsH, 0);
            mut.set(enhBaneofarthropodsH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED BANE OF ARTHROPODS → PERFECTED BANE OF ARTHROPODS
        if (stored.getLevel(enhBaneofarthropodsH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhBaneofarthropodsH, 0);
            mut.set(perBaneofarthropodsH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// BANE OF RAIDING → ENHANCED BANE OF RAIDING
        if (stored.getLevel(baneofraidingH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(baneofraidingH, 0);
            mut.set(enhBaneofraidingH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED BANE OF RAIDING → PERFECTED BANE OF RAIDING
        if (stored.getLevel(enhBaneofraidingH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhBaneofraidingH, 0);
            mut.set(perBaneofraidingH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// BANE OF SLIME → ENHANCED BANE OF SLIME
        if (stored.getLevel(baneofslimeH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(baneofslimeH, 0);
            mut.set(enhBaneofslimeH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED BANE OF SLIME → PERFECTED BANE OF SLIME
        if (stored.getLevel(enhBaneofslimeH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhBaneofslimeH, 0);
            mut.set(perBaneofslimeH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// BLAST PROTECTION → ENHANCED BLAST PROTECTION
        if (stored.getLevel(blastprotectionH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(blastprotectionH, 0);
            mut.set(enhBlastprotectionH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED BLAST PROTECTION → PERFECTED BLAST PROTECTION
        if (stored.getLevel(enhBlastprotectionH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhBlastprotectionH, 0);
            mut.set(perBlastprotectionH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// BREACH → ENHANCED BREACH
        if (stored.getLevel(breachH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(breachH, 0);
            mut.set(enhBreachH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED BREACH → PERFECTED BREACH
        if (stored.getLevel(enhBreachH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhBreachH, 0);
            mut.set(perBreachH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// BULWARK → ENHANCED BULWARK
        if (stored.getLevel(bulwarkH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(bulwarkH, 0);
            mut.set(enhBulwarkH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED BULWARK → PERFECTED BULWARK
        if (stored.getLevel(enhBulwarkH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhBulwarkH, 0);
            mut.set(perBulwarkH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// DENSITY → ENHANCED DENSITY
        if (stored.getLevel(densityH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(densityH, 0);
            mut.set(enhDensityH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED DENSITY → PERFECTED DENSITY
        if (stored.getLevel(enhDensityH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhDensityH, 0);
            mut.set(perDensityH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// DEPTH STRIDER → ENHANCED DEPTH STRIDER
        if (stored.getLevel(depthstriderH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(depthstriderH, 0);
            mut.set(enhDepthstriderH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED DEPTH STRIDER → PERFECTED DEPTH STRIDER
        if (stored.getLevel(enhDepthstriderH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhDepthstriderH, 0);
            mut.set(perDepthstriderH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// EFFICIENCY → ENHANCED EFFICIENCY
        if (stored.getLevel(efficiencyH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(efficiencyH, 0);
            mut.set(enhEfficiencyH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED EFFICIENCY → PERFECTED EFFICIENCY
        if (stored.getLevel(enhEfficiencyH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhEfficiencyH, 0);
            mut.set(perEfficiencyH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// ELEMENTAL SMITE → ENHANCED ELEMENTAL SMITE
        if (stored.getLevel(elementalsmiteH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(elementalsmiteH, 0);
            mut.set(enhElementalsmiteH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED ELEMENTAL SMITE → PERFECTED ELEMENTAL SMITE
        if (stored.getLevel(enhElementalsmiteH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhElementalsmiteH, 0);
            mut.set(perElementalsmiteH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// ENDSMIT → ENHANCED ENDSMIT
        if (stored.getLevel(endsmitH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(endsmitH, 0);
            mut.set(enhEndsmitH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED ENDSMIT → PERFECTED ENDSMIT
        if (stored.getLevel(enhEndsmitH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhEndsmitH, 0);
            mut.set(perEndsmitH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// FEATHER FALLING → ENHANCED FEATHER FALLING
        if (stored.getLevel(featherfallingH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(featherfallingH, 0);
            mut.set(enhFeatherfallingH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED FEATHER FALLING → PERFECTED FEATHER FALLING
        if (stored.getLevel(enhFeatherfallingH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhFeatherfallingH, 0);
            mut.set(perFeatherfallingH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// FIRE ASPECT → ENHANCED FIRE ASPECT
        if (stored.getLevel(fireaspectH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(fireaspectH, 0);
            mut.set(enhFireaspectH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED FIRE ASPECT → PERFECTED FIRE ASPECT
        if (stored.getLevel(enhFireaspectH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhFireaspectH, 0);
            mut.set(perFireaspectH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

// FIRE PROTECTION → ENHANCED FIRE PROTECTION
        if (stored.getLevel(fireprotectionH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(fireprotectionH, 0);
            mut.set(enhFireprotectionH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED FIRE PROTECTION → PERFECTED FIRE PROTECTION
        if (stored.getLevel(enhFireprotectionH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhFireprotectionH, 0);
            mut.set(perFireprotectionH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }

        // FOOTWORK → ENHANCED FOOTWORK
        if (stored.getLevel(footworkH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(footworkH, 0);
            mut.set(enhFootworkH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());

            event.setOutput(output);
            event.setCost(5);
            event.setMaterialCost(1);
        }
        // ENHANCED FOOTWORK → PERFECTED FOOTWORK
        if (stored.getLevel(enhFootworkH) == 3) {

            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(stored);
            mut.set(enhFootworkH, 0);
            mut.set(perFootworkH, 1);

            ItemStack output = new ItemStack(Items.ENCHANTED_BOOK);
            output.set(DataComponents.STORED_ENCHANTMENTS, mut.toImmutable());


            var name = left.get(DataComponents.CUSTOM_NAME);
            if (name != null) output.set(DataComponents.CUSTOM_NAME, name.copy());
            event.setOutput(output);
            event.setCost(10);
            event.setMaterialCost(2);
        }
    }
}
