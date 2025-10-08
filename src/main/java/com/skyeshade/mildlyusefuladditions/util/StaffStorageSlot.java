package com.skyeshade.mildlyusefuladditions.util;

import com.skyeshade.mildlyusefuladditions.datacomponents.ModDataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;

public class StaffStorageSlot extends Slot {
    private final ItemStack staff;
    private final int index;

    public StaffStorageSlot(ItemStack staff, int index, int x, int y) {
        super(new SimpleContainer(1), 0, x, y);
        this.staff = staff;
        this.index = index;
    }

    private ItemContainerContents contents() {
        return staff.getOrDefault(ModDataComponents.STAFF_STORAGE.get(), ItemContainerContents.EMPTY);
    }

    private static ItemStack getAt(ItemContainerContents c, int idx) {
        int size = c.getSlots();
        return (idx < size) ? c.getStackInSlot(idx) : ItemStack.EMPTY;
    }

    private void setAt(ItemStack stack) {
        ItemContainerContents c = contents();
        int size = Math.max(c.getSlots(), index + 1);


        SimpleContainer tmp = new SimpleContainer(size);
        c.copyInto(tmp.getItems());
        tmp.setItem(index, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());


        java.util.List<ItemStack> list = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) list.add(tmp.getItem(i));
        staff.set(ModDataComponents.STAFF_STORAGE.get(), ItemContainerContents.fromItems(list));
        setChanged();
    }

    @Override public boolean mayPlace(ItemStack stack) {
        Item i = stack.getItem();

        return i instanceof ProjectileItem
                || i instanceof PotionItem
                || i instanceof FireworkRocketItem
                || i instanceof EnderpearlItem;
    }

    @Override public ItemStack getItem()                { return getAt(contents(), index); }
    @Override public void set(ItemStack stack)          { setAt(stack); }
    @Override public boolean mayPickup(Player player)   { return !getItem().isEmpty(); }
    @Override public int getMaxStackSize()              { return 64; }

    @Override public ItemStack remove(int amount) {
        ItemStack cur = getItem();
        if (cur.isEmpty()) return ItemStack.EMPTY;
        ItemStack split = cur.split(amount);
        setAt(cur);
        return split;
    }
}
