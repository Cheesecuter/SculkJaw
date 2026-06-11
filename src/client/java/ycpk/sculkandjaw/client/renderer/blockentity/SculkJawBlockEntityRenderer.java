package ycpk.sculkandjaw.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;
import ycpk.sculkandjaw.client.model.geom.ModModelLayers;

@Environment(EnvType.CLIENT)
public class SculkJawBlockEntityRenderer implements BlockEntityRenderer<SculkJawBlockEntity> {
    public static final Material SCULK_JAW_STOMACH = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "entity/sculk_jaw/sculk_jaw_stomach"));
    public static final Material SCULK_JAW_COMBINED_STOMACH = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "entity/sculk_jaw/sculk_jaw_combined_stomach"));
    private static final String SCULK_JAW_STOMACH_SIDE = "sculk_jaw_stomach_side";
    private static final String SCULK_JAW_COMBINED_STOMACH_SIDE = "sculk_jaw_combined_stomach_side";
    private final ModelPart sculkJawStomach;
    private final ModelPart sculkJawCombinedStomach;

    public SculkJawBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart1 = context.bakeLayer(ModModelLayers.SCULK_JAW_STOMACH);
        ModelPart modelPart2 = context.bakeLayer(ModModelLayers.SCULK_JAW_COMBINED_STOMACH);
        this.sculkJawStomach = modelPart1.getChild("sculk_jaw_stomach_side1");
        this.sculkJawCombinedStomach = modelPart2.getChild("sculk_jaw_combined_stomach_side1");
    }

    public static LayerDefinition createBodyLayer1() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinitionRoot = meshDefinition.getRoot();
        PartDefinition partDefinition1 = partDefinitionRoot.addOrReplaceChild("sculk_jaw_stomach_side1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(8.0F, 0.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("sculk_jaw_stomach_side2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(-7.0F, 0.0F, 7.0F, 0.0F, 1.5707964F, 0.0F));
        PartDefinition partDefinition3 = partDefinition2.addOrReplaceChild("sculk_jaw_stomach_side3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, 0.0F, 1.5707964F, 0.0F));
        partDefinition3.addOrReplaceChild("sculk_jaw_stomach_side4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, 0.0F, 1.5707964F, 0.0F));
        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    public static LayerDefinition createBodyLayer2() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinitionRoot = meshDefinition.getRoot();
        PartDefinition partDefinition1 = partDefinitionRoot.addOrReplaceChild("sculk_jaw_combined_stomach_side1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 32.0F, 0.0F), PartPose.offsetAndRotation(8.0F, 0.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("sculk_jaw_combined_stomach_side2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 32.0F, 0.0F), PartPose.offsetAndRotation(-7.0F, 0.0F, 7.0F, 0.0F, 1.5707964F, 0.0F));
        PartDefinition partDefinition3 = partDefinition2.addOrReplaceChild("sculk_jaw_combined_stomach_side3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 32.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, 0.0F, 1.5707964F, 0.0F));
        partDefinition3.addOrReplaceChild("sculk_jaw_combined_stomach_side4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 32.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, 0.0F, 1.5707964F, 0.0F));
        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    @Override
    public void render(SculkJawBlockEntity sculkJawBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        float m = 1.0F;
        float n = 1.0F;
        float cosineValue = 1.0F;
        int phase = sculkJawBlockEntity.tickCount;
        cosineValue = (float) (0.5 * (Mth.cos((float) phase) * 0.05F));
        cosineValue = 0.9F + cosineValue;
        if (sculkJawBlockEntity.getHasCombined()) {
            this.sculkJawCombinedStomach.y = -16.0F;
            this.sculkJawCombinedStomach.xScale = m * cosineValue;
            this.sculkJawCombinedStomach.yScale = 0.99F;
            this.sculkJawCombinedStomach.zScale = n * cosineValue;
            RenderType renderType = SCULK_JAW_COMBINED_STOMACH.renderType(RenderType::entitySolid);
            VertexConsumer vertexConsumer = SCULK_JAW_COMBINED_STOMACH.buffer(multiBufferSource, RenderType::entitySolid);
            this.sculkJawCombinedStomach.render(poseStack, vertexConsumer, i, j);
        }
        else {
            this.sculkJawStomach.xScale = m * cosineValue;
            this.sculkJawStomach.yScale = 0.99F;
            this.sculkJawStomach.zScale = n * cosineValue;
            RenderType renderType = SCULK_JAW_STOMACH.renderType(RenderType::entitySolid);
            VertexConsumer vertexConsumer = SCULK_JAW_STOMACH.buffer(multiBufferSource, RenderType::entitySolid);
            this.sculkJawStomach.render(poseStack, vertexConsumer, i, j);
        }
    }
}
