package ycpk.sculkandjaw.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.blockentities.TunedSculkJawBlockEntity;
import ycpk.sculkandjaw.blocks.modblocks.TunedSculkJawBlock;
import ycpk.sculkandjaw.client.renderer.blockentity.state.TunedSculkJawBlockEntityRenderState;

@Environment(EnvType.CLIENT)
public class TunedSculkJawBlockEntityRenderer implements BlockEntityRenderer<TunedSculkJawBlockEntity, TunedSculkJawBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;

    public TunedSculkJawBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public TunedSculkJawBlockEntityRenderState createRenderState() {
        return new TunedSculkJawBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(TunedSculkJawBlockEntity tunedSculkJawBlockEntity, TunedSculkJawBlockEntityRenderState tunedSculkJawBlockEntityRenderState,
                                   float f, Vec3 vec3, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderState.extractBase(tunedSculkJawBlockEntity, tunedSculkJawBlockEntityRenderState, crumblingOverlay);
        ItemStack itemStack = tunedSculkJawBlockEntity.getFilterItem();
        int i = HashCommon.long2int(tunedSculkJawBlockEntity.getBlockPos().asLong());
        if (!itemStack.isEmpty()) {
            ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(itemStackRenderState, itemStack, ItemDisplayContext.FIXED, tunedSculkJawBlockEntity.level(), tunedSculkJawBlockEntity, i + 1);
            tunedSculkJawBlockEntityRenderState.filterItem = itemStackRenderState;
        }
    }

    @Override
    public void submit(TunedSculkJawBlockEntityRenderState tunedSculkJawBlockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        ItemStackRenderState itemStackRenderState = tunedSculkJawBlockEntityRenderState.filterItem;
        for (Direction direction : Direction.values()) {
            if (direction != (Direction) tunedSculkJawBlockEntityRenderState.blockState.getValue(TunedSculkJawBlock.FACING)) {
                float f, g;
                if (direction.getAxis().isHorizontal()) {
                    f = 0.0F;
                    g = 180.0F - direction.toYRot();
                }
                else {
                    f = (float) (-90 * direction.getAxisDirection().getStep());
                    g = 180.0F;
                }
                if (itemStackRenderState != null) {
                    this.submitItem(tunedSculkJawBlockEntityRenderState, itemStackRenderState, poseStack, submitNodeCollector, f, g);
                }
            }
        }
    }

    private void submitItem(TunedSculkJawBlockEntityRenderState tunedSculkJawBlockEntityRenderState, ItemStackRenderState itemStackRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, float x, float y) {
        Vec3 vec3 = new Vec3(0.0,  0.0, -0.25);
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(x));
        poseStack.mulPose(Axis.YP.rotationDegrees(y));
        poseStack.translate(vec3);
        poseStack.scale(0.25F, 0.25F, 0.25F);
        AABB aABB = itemStackRenderState.getModelBoundingBox();
        double dx = -aABB.minX;
        dx += -(aABB.maxX - aABB.minX) / 2.0;
        double dy = -aABB.minY;
        dy += -(aABB.maxY - aABB.minY) / 2.0;
        poseStack.translate(dx, dy, -1);
        itemStackRenderState.submit(poseStack, submitNodeCollector, tunedSculkJawBlockEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
