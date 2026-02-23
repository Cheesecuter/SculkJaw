package ycpk.sculkandjaw.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;
import ycpk.sculkandjaw.client.model.SculkJawCombinedStomachModel;
import ycpk.sculkandjaw.client.model.SculkJawStomachModel;
import ycpk.sculkandjaw.client.model.geom.ModModelLayers;
import ycpk.sculkandjaw.client.renderer.blockentity.state.SculkJawBlockEntityRenderState;

@Environment(EnvType.CLIENT)
public class SculkJawBlockEntityRenderer implements BlockEntityRenderer<SculkJawBlockEntity, SculkJawBlockEntityRenderState> {
    public static final Material SCULK_JAW_STOMACH = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_jaw/sculk_jaw_stomach"));
    public static final Material SCULK_JAW_COMBINED_STOMACH = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_jaw/sculk_jaw_combined_stomach"));
    private final MaterialSet materials;
    private final SculkJawStomachModel sculkJawStomachModel;
    private final SculkJawCombinedStomachModel sculkJawCombinedStomachModel;

    public SculkJawBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.materials = context.materials();
        this.sculkJawStomachModel = new SculkJawStomachModel(context.bakeLayer(ModModelLayers.SCULK_JAW_STOMACH));
        this.sculkJawCombinedStomachModel = new SculkJawCombinedStomachModel(context.bakeLayer(ModModelLayers.SCULK_JAW_COMBINED_STOMACH));
    }

    @Override
    public SculkJawBlockEntityRenderState createRenderState() {
        return new SculkJawBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(SculkJawBlockEntity blockEntity, SculkJawBlockEntityRenderState state,
                                   float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.tickCount = blockEntity.tickCount;
        state.hasCombined = blockEntity.getHasCombined();
    }

    @Override
    public void submit(SculkJawBlockEntityRenderState sculkJawBlockEntityRenderState, PoseStack matrices,
                       SubmitNodeCollector queue, CameraRenderState cameraState) {
        SculkJawStomachModel.State state = new SculkJawStomachModel.State(sculkJawBlockEntityRenderState.tickCount, sculkJawBlockEntityRenderState.hasCombined);
        if(sculkJawBlockEntityRenderState.hasCombined) {
            SculkJawCombinedStomachModel.State state2 = new SculkJawCombinedStomachModel.State(sculkJawBlockEntityRenderState.tickCount, sculkJawBlockEntityRenderState.hasCombined);
            this.sculkJawCombinedStomachModel.setupAnim(state2);
            RenderType renderType = SCULK_JAW_COMBINED_STOMACH.renderType(RenderType::entitySolid);
            queue.submitModel(this.sculkJawCombinedStomachModel, state2, matrices, renderType, sculkJawBlockEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, this.materials.get(SCULK_JAW_COMBINED_STOMACH), 0, sculkJawBlockEntityRenderState.breakProgress);
            return;
        }
        this.sculkJawStomachModel.setupAnim(state);
        RenderType renderType = SCULK_JAW_STOMACH.renderType(RenderType::entitySolid);
        queue.submitModel(this.sculkJawStomachModel, state, matrices, renderType, sculkJawBlockEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, this.materials.get(SCULK_JAW_STOMACH), 0, sculkJawBlockEntityRenderState.breakProgress);
    }
}
