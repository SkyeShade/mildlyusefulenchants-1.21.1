package com.skyeshade.mildlyusefuladditions.menu;


import com.skyeshade.mildlyusefuladditions.util.StaffAmmoSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

public class StaffMenu extends AbstractContainerMenu {
    private final InteractionHand hand;
    private final ItemStack staffStack;


    public StaffMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readEnum(InteractionHand.class));
    }


    public StaffMenu(int id, Inventory inv, InteractionHand hand) {
        super(ModMenus.STAFF.get(), id);
        this.hand = hand;
        this.staffStack = inv.player.getItemInHand(hand);


        this.addSlot(new StaffAmmoSlot(staffStack, 80, 35));


        layoutPlayerInventorySlots(new PlayerMainInvWrapper(inv), 8, 84);
    }

    @Override public boolean stillValid(Player player) {
        return player.getItemInHand(hand) == staffStack;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return empty;

        ItemStack in = slot.getItem();
        ItemStack copy = in.copy();

        if (index == 0) {
            if (!this.moveItemStackTo(in, 1, this.slots.size(), true)) return empty;
        } else {
            if (!this.moveItemStackTo(in, 0, 1, false)) return empty;
        }

        if (in.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }

    private void layoutPlayerInventorySlots(IItemHandler handler, int left, int top) {
        for (int r = 0; r < 3; ++r)
            for (int c = 0; c < 9; ++c)
                this.addSlot(new SlotItemHandler(handler, c + r * 9 + 9, left + c * 18, top + r * 18));
        for (int c = 0; c < 9; ++c)
            this.addSlot(new SlotItemHandler(handler, c, left + c * 18, top + 58));
    }
}
