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
import ycpk.sculkandjaw.client.model.SculkJawCombinedStomachModel;
import ycpk.sculkandjaw.client.model.SculkJawStomachModel;
import ycpk.sculkandjaw.client.model.geom.ModModelLayers;

@Environment(EnvType.CLIENT)
public class SculkJawBlockEntityRenderer implements BlockEntityRenderer<SculkJawBlockEntity> {
    private final SculkJawStomachModel sculkJawStomachModel;
    private final SculkJawCombinedStomachModel sculkJawCombinedStomachModel;

    public SculkJawBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.sculkJawStomachModel = new SculkJawStomachModel(context.bakeLayer(ModModelLayers.SCULK_JAW_STOMACH));
        this.sculkJawCombinedStomachModel = new SculkJawCombinedStomachModel(context.bakeLayer(ModModelLayers.SCULK_JAW_COMBINED_STOMACH));
    }

    @Override
    public void render(SculkJawBlockEntity sculkJawBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        if (sculkJawBlockEntity.getHasCombined()) {
            this.sculkJawCombinedStomachModel.render(sculkJawBlockEntity, f, poseStack, multiBufferSource, i, j);
        }
        else {
            this.sculkJawStomachModel.render(sculkJawBlockEntity, f, poseStack, multiBufferSource, i, j);
        }
    }
}
