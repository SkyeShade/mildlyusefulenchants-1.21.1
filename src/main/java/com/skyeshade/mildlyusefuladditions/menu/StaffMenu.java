package com.skyeshade.mildlyusefuladditions.menu;


import com.skyeshade.mildlyusefuladditions.util.StaffStorageSlot;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;
public class StaffMenu extends AbstractContainerMenu {
    private final InteractionHand hand;
    private final ItemStack staffStack;
    private final boolean doubleSpell;
    public StaffMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readEnum(InteractionHand.class));
    }
    private static final ResourceKey<Enchantment> DOUBLE_SPELL_KEY = ResourceKey.create(
            Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("mildlyusefuladditions", "doublespell"));
    private static int enchantLevelGameplay(ItemStack stack, Level level, ResourceKey<Enchantment> key) {
        Holder<Enchantment> h = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(key);
        return ((IItemStackExtension)(Object)stack).getEnchantmentLevel(h);
    }

    public StaffMenu(int id, Inventory inv, InteractionHand hand) {
        super(ModMenus.STAFF.get(), id);
        this.hand = hand;
        this.staffStack = inv.player.getItemInHand(hand);

        boolean doubleSpell = enchantLevelGameplay(staffStack, inv.player.level(), DOUBLE_SPELL_KEY) > 0;
        this.doubleSpell = enchantLevelGameplay(staffStack, inv.player.level(), DOUBLE_SPELL_KEY) > 0; // <—

        // one centered slot or two spaced slots
        if (doubleSpell) {
            this.addSlot(new StaffStorageSlot(staffStack, 0, 62, 35));
            this.addSlot(new StaffStorageSlot(staffStack, 1, 98, 35));
        } else {
            this.addSlot(new StaffStorageSlot(staffStack, 0, 80, 35));
        }

        layoutPlayerInventorySlots(new PlayerMainInvWrapper(inv), 8, 84);
    }
    public boolean hasDoubleSpell() { return doubleSpell; }
    @Override public boolean stillValid(Player player) {
        return player.getItemInHand(hand) == staffStack;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return empty;


        boolean doubleSpell = enchantLevelGameplay(staffStack, player.level(), DOUBLE_SPELL_KEY) > 0;
        int ammoSlots = doubleSpell ? 2 : 1;
        int playerStart = ammoSlots;
        int playerEnd = this.slots.size();

        ItemStack in = slot.getItem();
        ItemStack copy = in.copy();

        if (index < ammoSlots) {
            if (!this.moveItemStackTo(in, playerStart, playerEnd, true)) return empty;
        } else {
            if (!this.moveItemStackTo(in, 0, ammoSlots, false)) return empty;
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
