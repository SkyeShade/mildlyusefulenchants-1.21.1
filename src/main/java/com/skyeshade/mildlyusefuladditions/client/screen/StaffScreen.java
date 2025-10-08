package com.skyeshade.mildlyusefuladditions.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.skyeshade.mildlyusefuladditions.menu.StaffMenu;
import net.minecraft.world.entity.player.Inventory;

public class StaffScreen extends AbstractContainerScreen<StaffMenu> {
    private static final ResourceLocation BG =
            ResourceLocation.fromNamespaceAndPath("mildlyusefuladditions", "textures/gui/staff.png");

    public StaffScreen(StaffMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        g.blit(BG, x, y, 0, 0, this.imageWidth, this.imageHeight);

    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(this.font, this.title, 8, 6, 0x404040, false);
    }
}
