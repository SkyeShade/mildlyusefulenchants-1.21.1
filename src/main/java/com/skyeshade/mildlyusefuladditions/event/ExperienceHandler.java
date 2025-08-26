package com.skyeshade.mildlyusefuladditions.event;

import com.skyeshade.mildlyusefuladditions.MildlyUsefulAdditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = MildlyUsefulAdditions.MODID)
public class ExperienceHandler {

    //     // Deve bater com o ID do JSON em: data/mildlyusefuladditions/enchantment/wisdom.json
    private static final ResourceKey<Enchantment> WISDOM_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "wisdom"));
    private static final ResourceKey<Enchantment> ADVANCED_WISDOM_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "advanced_wisdom"));
    private static final ResourceKey<Enchantment> MASTERFUL_WISDOM_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "masterful_wisdom"));

    // Configurável: +25% de XP por nível
    private static final double BONUS_PER_LEVEL = 0.25;

    // ---------- MOBS (kill XP) ----------
    @SubscribeEvent
    public static void onMobXPDrop(LivingExperienceDropEvent event) {
        if (!(event.getAttackingPlayer() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        ItemStack stack = player.getMainHandItem();
        int multiplierLevels = getTotalWisdomLevels(level, stack);
        if (multiplierLevels <= 0) return;

        int base = event.getDroppedExperience();
        if (base <= 0) return;

        int extra = (int) Math.ceil(base * (BONUS_PER_LEVEL * multiplierLevels));
        event.setDroppedExperience(base + extra);
    }

    // ---------- BLOCOS/MINÉRIOS (block XP) ----------
    // Observação: aqui nós não mexemos no XP "oficial" do evento (porque BreakEvent não expõe get/set em tua versão).
    // Em vez disso calculamos o XP base do bloco (mesma lógica do VeinMiningHandler) e *spawnamos* orbs extras com popExperience().

    // Observation: we don't mess with the "official" xp of the event, because BreakEvent don't allow get/set in this version.
    // Instead, we calculate the Base XP of the block with the same logic as Vein Mining, and we spawn extra orbs with popExperience.
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        BlockPos pos = event.getPos();
        BlockState before = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);
        ItemStack tool = player.getMainHandItem();

        int multiplierLevels = getTotalWisdomLevels(level, tool);
        if (multiplierLevels <= 0) return;

        // Checa Silk Touch via lookup (mesmo padrão do VeinMiningHandler)
        var lookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var silkHolder = lookup.get(Enchantments.SILK_TOUCH).orElse(null);
        boolean hasSilk = silkHolder != null && EnchantmentHelper.getItemEnchantmentLevel(silkHolder, tool) > 0;

        int baseXp = 0;
        if (!hasSilk) {
            try {
                baseXp = ((IBlockExtension) before.getBlock()).getExpDrop(before, level, pos, be, player, tool);
            } catch (Throwable t) {
                // fallback seguro: se falhar, não adicionamos XP extra
                baseXp = 0;
            }
        }

        if (baseXp <= 0) return;

        int extra = (int) Math.ceil(baseXp * (BONUS_PER_LEVEL * multiplierLevels));
        if (extra > 0) {
            before.getBlock().popExperience(level, pos, extra);
        }
    }

    private static int getTotalWisdomLevels(ServerLevel level, ItemStack stack) {
        int total = 0;

        // obtém lookup tal como no VeinMiningHandler
        var lookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        // Wisdom normal
        var wisdom = lookup.get(WISDOM_KEY).orElse(null);
        if (wisdom != null) total += EnchantmentHelper.getItemEnchantmentLevel(wisdom, stack);

        // Advanced Wisdom -> cada nível equivale a Wisdom 5
        var adv = lookup.get(ADVANCED_WISDOM_KEY).orElse(null);
        if (adv != null) total += EnchantmentHelper.getItemEnchantmentLevel(adv, stack) * 5;

        // Masterful Wisdom -> cada nível equivale a Wisdom 25
        var master = lookup.get(MASTERFUL_WISDOM_KEY).orElse(null);
        if (master != null) total += EnchantmentHelper.getItemEnchantmentLevel(master, stack) * 25;

        return total;
    }
}
