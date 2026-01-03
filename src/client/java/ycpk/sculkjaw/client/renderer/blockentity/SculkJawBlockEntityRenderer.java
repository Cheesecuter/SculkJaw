package ycpk.sculkjaw.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import ycpk.sculkjaw.blocks.blockentities.SculkJawBlockEntity;
import ycpk.sculkjaw.client.renderer.blockentity.state.SculkJawBlockEntityRenderState;

public class SculkJawBlockEntityRenderer implements BlockEntityRenderer<SculkJawBlockEntity, SculkJawBlockEntityRenderState> {
    public SculkJawBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public SculkJawBlockEntityRenderState createRenderState() {
        return new SculkJawBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(SculkJawBlockEntity blockEntity, SculkJawBlockEntityRenderState state,
                                   float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    }

    @Override
    public void submit(SculkJawBlockEntityRenderState state, PoseStack matrices,
                       SubmitNodeCollector queue, CameraRenderState cameraState) {
    }
}
