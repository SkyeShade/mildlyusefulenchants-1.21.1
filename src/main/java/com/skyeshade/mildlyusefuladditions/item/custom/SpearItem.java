package com.skyeshade.mildlyusefuladditions.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class SpearItem extends SwordItem {
    public SpearItem(Tier p_tier, Properties p_properties, Tool toolComponentData) {
        super(p_tier, p_properties.component(DataComponents.TOOL, toolComponentData));
    }

    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.LOYALTY)) {
            return true;
        }
        return super.supportsEnchantment(stack, enchantment);
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return createAttributes(tier, (float)attackDamage, attackSpeed);
    }
}
