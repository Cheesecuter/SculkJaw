package ycpk.sculkjaw.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class SculkJawCombinedStomachModel extends Model<SculkJawCombinedStomachModel.State> {
    private static final String SCULK_JAW_COMBINED_STOMACH = "sculk_jaw_combined_stomach";
    private final ModelPart sculkJawCombinedStomach;

    public SculkJawCombinedStomachModel(ModelPart modelPart) {
        super(modelPart, RenderType::entitySolid);
        this.sculkJawCombinedStomach = modelPart.getChild("sculk_jaw_combined_stomach_side1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinitionRoot = meshDefinition.getRoot();
        PartDefinition partDefinition1 = partDefinitionRoot.addOrReplaceChild("sculk_jaw_combined_stomach_side1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 32.0F, 0.0F), PartPose.offsetAndRotation(8.0F, 0.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition partDefinition2 = partDefinition1.addOrReplaceChild("sculk_jaw_combined_stomach_side2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 32.0F, 0.0F), PartPose.offsetAndRotation(-7.0F, 0.0F, 7.0F, 0.0F, 1.5707964F, 0.0F));
        PartDefinition partDefinition3 = partDefinition2.addOrReplaceChild("sculk_jaw_combined_stomach_side3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 32.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, 0.0F, 1.5707964F, 0.0F));
        partDefinition3.addOrReplaceChild("sculk_jaw_combined_stomach_side4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 14.0F, 32.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 14.0F, 0.0F, 1.5707964F, 0.0F));
        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    public void setupAnim(SculkJawCombinedStomachModel.State state) {
        super.setupAnim(state);
        float f = 1.0F;
        float g = 1.0F;

        float cosineValue = 1.0F;

        int phase = state.tickCount();
        cosineValue = (float) (0.5 * (Mth.cos((float) phase) * 0.05F));
        cosineValue = 0.9F + cosineValue;

        this.sculkJawCombinedStomach.y = -16.0F;
        this.sculkJawCombinedStomach.xScale = f * cosineValue;
        this.sculkJawCombinedStomach.yScale = 0.99F;
        this.sculkJawCombinedStomach.zScale = g * cosineValue;
    }

    @Environment(EnvType.CLIENT)
    public static record State(int tickCount, boolean hasCombined) {
        public State(int tickCount, boolean hasCombined) {
            this.tickCount = tickCount;
            this.hasCombined = hasCombined;
        }

        public int tickCount() {
            return this.tickCount;
        }

        public boolean hasCombined() {
            return this.hasCombined;
        }
    }
}
