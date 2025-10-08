package com.skyeshade.mildlyusefuladditions.util;

import com.skyeshade.mildlyusefuladditions.datacomponents.ModDataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.List;

public class StaffAmmoSlot extends Slot {
    private final ItemStack staff;

    public StaffAmmoSlot(ItemStack staff, int x, int y) {
        super(new SimpleContainer(1), 0, x, y);
        this.staff = staff;
    }

    private ItemContainerContents contents() {
        return staff.getOrDefault(ModDataComponents.STAFF_STORAGE.get(), ItemContainerContents.EMPTY);
    }

    private static ItemStack first(ItemContainerContents c) {
        int size = c.getSlots();
        return size > 0 ? c.getStackInSlot(0)
                : ItemStack.EMPTY;
    }


    private void setContents(ItemContainerContents c) {
        staff.set(ModDataComponents.STAFF_STORAGE.get(), c);
        setChanged();
    }

    private void setFirst(ItemStack stack) {
        if (stack.isEmpty()) {
            setContents(ItemContainerContents.EMPTY);
        } else {

            setContents(ItemContainerContents.fromItems(List.of(stack.copy())));
        }
    }

    @Override public boolean mayPlace(ItemStack stack) {
        Item i = stack.getItem();
        return i instanceof EnderpearlItem
                || i instanceof PotionItem

                || i instanceof ProjectileItem;
    }

    @Override public ItemStack getItem() { return first(contents()); }

    @Override public void set(ItemStack stack) { setFirst(stack); }

    @Override public boolean mayPickup(Player player) { return !getItem().isEmpty(); }

    @Override public int getMaxStackSize() { return 64; }

    @Override public ItemStack remove(int amount) {
        ItemStack cur = getItem();
        if (cur.isEmpty()) return ItemStack.EMPTY;
        ItemStack split = cur.split(amount);
        setFirst(cur);
        return split;
    }
}
