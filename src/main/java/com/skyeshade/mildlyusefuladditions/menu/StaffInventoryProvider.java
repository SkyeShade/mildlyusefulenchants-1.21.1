package com.skyeshade.mildlyusefuladditions.menu;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public class StaffInventoryProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final ItemStackHandler handler = new ItemStackHandler(1) {
        @Override protected void onContentsChanged(int slot) { setDirty(true); }
    };
    private boolean dirty = false;
    private void setDirty(boolean v){ dirty = v; }



    @Override
    public @Nullable Object getCapability(Object o, Object o2) {
        return null;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {

    }
}