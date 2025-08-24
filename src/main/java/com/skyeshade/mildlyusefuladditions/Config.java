package com.skyeshade.mildlyusefuladditions;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Config {
    // ----- spec -----
    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ---- veinMining ----
    public static final ModConfigSpec.IntValue     VEIN_DELAY_TICKS;
    public static final ModConfigSpec.BooleanValue INCLUDE_ORES_TAG;
    public static final ModConfigSpec.BooleanValue REQUIRE_SAME_BLOCK;
    public static final ModConfigSpec.IntValue     MAX_BLOCKS_CAP;

    public static final ModConfigSpec.ConfigValue<List<? extends String>> EXTRA_BLOCK_TAGS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> FAMILY_BLOCK_TAGS;

    public static final ModConfigSpec.BooleanValue ALLOW_BLOCK_ENTITIES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> BE_ALLOWED_TAGS;

    // ---- stripTunneling (example 2nd category) ----
    public static final ModConfigSpec.BooleanValue     STRIP_TOOL_CHECK;
    public static final ModConfigSpec.IntValue     STRIP_LAYER_DELAY_TICKS;
    public static final ModConfigSpec.IntValue     STRIP_LENGTH_PER_LEVEL;

    // ---- tunneling (example 3rd category) ----
    public static final ModConfigSpec.BooleanValue     TUNNEL_TOOL_CHECK;
    public static final ModConfigSpec.IntValue     TUNNEL_LAYER_DELAY_TICKS;
    public static final ModConfigSpec.IntValue     TUNNEL_LENGTH_PER_LVL;

    static {
        BUILDER.push("veinMining");

        INCLUDE_ORES_TAG = BUILDER
                .comment("Include all ores via Tags.Blocks.ORES")
                .define("includeOresTag", true);

        REQUIRE_SAME_BLOCK = BUILDER
                .comment("If true, only exactly the same block can chain. If false, use familyBlockTags to group.")
                .define("requireSameBlock", false);

        ALLOW_BLOCK_ENTITIES = BUILDER
                .comment("Allow breaking blocks with BlockEntities if they match beAllowedTags.")
                .define("allowBlockEntities", true);

        BE_ALLOWED_TAGS = BUILDER
                .comment("Block TAGS whose blocks are allowed even if they have BlockEntities (e.g. 'mildlyusefuladditions:sculk_blocks').")
                .defineListAllowEmpty(
                        "beAllowedTags",
                        List.of("mildlyusefuladditions:sculk_blocks"), // your sculk tag
                        () -> "mildlyusefuladditions:sculk_blocks",
                        Config::looksLikeRL
                );
        EXTRA_BLOCK_TAGS = BUILDER
                .comment("Additional block tags that are vein-minable (e.g. 'minecraft:logs', 'c:ores/coal').")
                .defineListAllowEmpty("extraBlockTags",
                        List.of("minecraft:logs", "mildlyusefuladditions:sculk_blocks"),
                        () -> "minecraft:logs",
                        Config::looksLikeRL);

        FAMILY_BLOCK_TAGS = BUILDER
                .comment("Tags that define a 'family' when requireSameBlock = false. Share any to chain.")
                .defineList("familyBlockTags",
                        List.of("c:ores","minecraft:logs", "mildlyusefuladditions:sculk_blocks"),
                        Config::looksLikeRL);

        VEIN_DELAY_TICKS = BUILDER
                .comment("Delay (ticks) between each mining operation.")
                .defineInRange("layerDelayTicks", 3, 0, 40);
        MAX_BLOCKS_CAP = BUILDER
                .comment("Maximum blocks vein-mined per activation.")
                .defineInRange("maxBlocksCap", 150, 1, 1048576);



        BUILDER.pop();
        // ===== stripTunneling =====
        BUILDER.push("stripTunneling");


        STRIP_TOOL_CHECK = BUILDER
                .comment("True = Only blocks matching the tool requirements. False = Allows mining anything that is considered 'harvestable', like Gravel, Sand, Dirt etc.")
                .define("stripToolCheck", true);

        STRIP_LAYER_DELAY_TICKS = BUILDER
                .comment("Delay (ticks) between each 1x2 layer.")
                .defineInRange("layerDelayTicks", 3, 0, 40);

        STRIP_LENGTH_PER_LEVEL = BUILDER
                .comment("How many blocks of length per enchantment level.")
                .defineInRange("lengthPerLevel", 5, 1, 64);

        BUILDER.pop();

        // ===== tunneling =====
        BUILDER.push("tunneling");


        TUNNEL_TOOL_CHECK = BUILDER
                .comment("True = Only blocks matching the tool requirements. False = Allows mining anything that is considered 'harvestable', like Gravel, Sand, Dirt etc.")
                .define("tunnelToolCheck", true);

        TUNNEL_LAYER_DELAY_TICKS = BUILDER
                .comment("Delay (ticks) between each 1x2 layer.")
                .defineInRange("layerDelayTicks", 3, 0, 40);

        TUNNEL_LENGTH_PER_LVL = BUILDER
                .comment("How many blocks of length per enchantment level.")
                .defineInRange("lengthPerLevel", 5, 1, 64);

        BUILDER.pop();


        SPEC = BUILDER.build();



    }


    public static Set<TagKey<Block>> beAllowedTags() {
        return BE_ALLOWED_TAGS.get().stream()
                .map(ResourceLocation::parse)
                .map(rl -> TagKey.create(Registries.BLOCK, rl))
                .collect(Collectors.toUnmodifiableSet());
    }


    private static boolean looksLikeRL(Object o) {
        return o instanceof String s && s.contains(":");
    }


    public static Set<TagKey<Block>> extraTags() {
        return EXTRA_BLOCK_TAGS.get().stream()
                .map(ResourceLocation::parse)
                .map(rl -> TagKey.create(Registries.BLOCK, rl))
                .collect(Collectors.toUnmodifiableSet());
    }

    public static Set<TagKey<Block>> familyTags() {
        return FAMILY_BLOCK_TAGS.get().stream()
                .map(ResourceLocation::parse)
                .map(rl -> TagKey.create(Registries.BLOCK, rl))
                .collect(Collectors.toUnmodifiableSet());
    }

    private Config() {}
}