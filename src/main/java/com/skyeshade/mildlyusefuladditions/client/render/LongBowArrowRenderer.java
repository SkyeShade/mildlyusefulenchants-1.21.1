package com.skyeshade.mildlyusefuladditions.client.render;


import com.skyeshade.mildlyusefuladditions.entity.LongBowArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LongBowArrowRenderer extends ArrowRenderer<LongBowArrow> {

    private static final ResourceLocation TEX =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/projectiles/arrow.png");

    public LongBowArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(LongBowArrow entity) {
        return TEX;
    }
}