package io.radston12.unfall.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class PlaneModel<T extends Entity> extends EntityModel<T> {
	private final ModelPart anim;
	private final ModelPart bb_main;

	public PlaneModel(ModelPart root) {
		this.anim = root.getChild("anim");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition anim = partdefinition.addOrReplaceChild("anim", CubeListBuilder.create(), PartPose.offset(25.0F, 16.0F, 0.0F));

		PartDefinition cube_r1 = anim.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(75, 114).addBox(-11.0F, 1.0F, -1.0F, 22.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.2654F, -1.5708F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -1.0F, -24.0F, 16.0F, 1.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(75, 114).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(25.0F, -8.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(67, 106).addBox(-1.0F, 17.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(67, 106).addBox(15.0F, 17.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(66, 105).addBox(7.0F, 17.0F, 2.0F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(66, 105).addBox(-8.0F, 17.0F, 2.0F, 1.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(64, 103).addBox(-6.0F, 17.0F, 1.0F, 12.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(64, 87).addBox(-7.0F, 7.0F, 0.0F, 13.0F, 1.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, 0.0F, 1.5708F, -1.5708F));

		PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 22).addBox(-6.0F, -15.0F, 8.0F, 12.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-6.0F, -1.0F, 8.0F, 12.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 6.0F, 8.0F, 13.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(1.0F, -7.0F, 8.0F, 13.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(80, 0).addBox(-7.0F, -16.0F, -18.0F, 14.0F, 1.0F, 36.0F, new CubeDeformation(0.0F))
		.texOffs(0, 86).addBox(-7.0F, 0.0F, -18.0F, 14.0F, 1.0F, 36.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 49).addBox(0.0F, 7.0F, -18.0F, 15.0F, 1.0F, 36.0F, new CubeDeformation(0.0F))
		.texOffs(68, 50).addBox(0.0F, -8.0F, -18.0F, 15.0F, 1.0F, 36.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		anim.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}