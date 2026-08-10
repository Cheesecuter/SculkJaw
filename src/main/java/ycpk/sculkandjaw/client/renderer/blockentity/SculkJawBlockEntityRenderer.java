package ycpk.sculkandjaw.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;
import ycpk.sculkandjaw.client.model.SculkJawCombinedStomachModel;
import ycpk.sculkandjaw.client.model.SculkJawStomachModel;
import ycpk.sculkandjaw.client.model.geom.ModModelLayers;

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
