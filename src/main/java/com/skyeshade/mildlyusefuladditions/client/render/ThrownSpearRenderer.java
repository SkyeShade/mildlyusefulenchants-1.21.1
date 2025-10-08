package com.skyeshade.mildlyusefuladditions.client.render;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import com.skyeshade.mildlyusefuladditions.entity.ThrownSpear;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
@OnlyIn(Dist.CLIENT)
public class ThrownSpearRenderer extends EntityRenderer<ThrownSpear> {
    private final ItemRenderer itemRenderer;

    public ThrownSpearRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(ThrownSpear e, float entityYaw, float partialTicks,
                       PoseStack pose, MultiBufferSource buf, int packedLight) {


        pose.pushPose();


        float yaw   = Mth.lerp(partialTicks, e.yRotO, e.getYRot()) - 90.0F;
        float pitch = Mth.lerp(partialTicks, e.xRotO, e.getXRot()) + 90.0F;
        pose.mulPose(Axis.YP.rotationDegrees(yaw));
        pose.mulPose(Axis.ZP.rotationDegrees(pitch));

        float shake = e.shakeTime - partialTicks;
        if (shake > 0) pose.mulPose(Axis.XP.rotationDegrees(-Mth.sin(shake * 3.0F) * shake));


        pose.scale(1.0F, 1.0F, 1.0F);


        ItemStack stack = e.getRenderStack();

        if (stack.isEmpty()) {

            pose.popPose();
            super.render(e, entityYaw, partialTicks, pose, buf, packedLight);
            return;
        }

        if (e.isFoil() && !Boolean.TRUE.equals(stack.get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE))) {
            stack = stack.copy();
            stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, Boolean.TRUE);
        }

        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.HEAD,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                pose,
                buf,
                e.level(),
                e.getId()
        );



        pose.popPose();


        super.render(e, entityYaw, partialTicks, pose, buf, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownSpear e) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
