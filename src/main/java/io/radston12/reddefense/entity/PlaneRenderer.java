package io.radston12.reddefense.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.radston12.reddefense.RedDefenseMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class PlaneRenderer extends EntityRenderer<PlaneEntity> {
    private static final ResourceLocation PLANE_TEXTURE = new ResourceLocation(RedDefenseMod.MOD_ID, "textures/entity/plane.png");

    protected final EntityModel<PlaneEntity> model;
    public PlaneRenderer(EntityRendererProvider.Context context    ) {
        super(context);
        model = new PlaneModel<>(context.bakeLayer(ModModelLayer.PLANE_LAYER));
    }

    public void render(PlaneEntity entity, float p_115419_, float p_115420_, PoseStack poseStack, MultiBufferSource bufferSource, int p_115423_) {
        super.render(entity, p_115419_, p_115420_, poseStack, bufferSource, p_115423_);
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0f, -1.4f, 0f);
        poseStack.rotateAround(Axis.YN.rotationDegrees(90f), 0.0f, 0.0f, 0.0f);
        this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, p_115423_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(PlaneEntity p_114482_) {
        return PLANE_TEXTURE;
    }
}
