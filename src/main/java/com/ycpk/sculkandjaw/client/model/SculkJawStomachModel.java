package com.ycpk.sculkandjaw.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SculkJawStomachModel {
    private static final String SCULK_JAW_STOMACH_SIDE = "sculk_jaw_stomach_side";
    private final ModelPart sculkJawStomach;
    public static final Material SCULK_JAW_STOMACH = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "entity/sculk_jaw/sculk_jaw_stomach"));

    public SculkJawStomachModel(ModelPart modelPart) {
        this.sculkJawStomach = modelPart.getChild("sculk_jaw_stomach_side1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinitionRoot = meshDefinition.getRoot();
        PartDefinition partDefinition1 = partDefinitionRoot.addOrReplaceChild("sculk_jaw_stomach_side1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(8.0F, 0.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("sculk_jaw_stomach_side2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(-7.0F, 0.0F, 7.0F, 0.0F, 1.5707964F, 0.0F));
        PartDefinition partDefinition3 = partDefinition2.addOrReplaceChild("sculk_jaw_stomach_side3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, 0.0F, 1.5707964F, 0.0F));
        partDefinition3.addOrReplaceChild("sculk_jaw_stomach_side4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, 0.0F, 1.5707964F, 0.0F));
        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    public void render(SculkJawBlockEntity sculkJawBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        float m = 1.0F;
        float n = 1.0F;
        float cosineValue = 1.0F;
        int phase = sculkJawBlockEntity.tickCount;
        cosineValue = (float) (0.5 * (Mth.cos((float) phase) * 0.05F));
        cosineValue = 0.9F + cosineValue;
        this.sculkJawStomach.xScale = m * cosineValue;
        this.sculkJawStomach.yScale = 0.99F;
        this.sculkJawStomach.zScale = n * cosineValue;
        RenderType renderType = SCULK_JAW_STOMACH.renderType(RenderType::entitySolid);
        VertexConsumer vertexConsumer = SCULK_JAW_STOMACH.buffer(multiBufferSource, RenderType::entitySolid);
        this.sculkJawStomach.render(poseStack, vertexConsumer, i, j);
    }
}
