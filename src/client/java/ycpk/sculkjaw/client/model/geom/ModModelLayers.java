package ycpk.sculkjaw.client.model.geom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.client.model.SculkJawCombinedStomachModel;
import ycpk.sculkjaw.client.model.SculkJawStomachModel;

@Environment(EnvType.CLIENT)
public class ModModelLayers {
    public static void registerModModelLayers() {
        Sculkjaw.LOGGER.info("Registering Model Layers for Mod " + Sculkjaw.MOD_ID);
        EntityModelLayerRegistry.registerModelLayer(SCULK_JAW_STOMACH, SculkJawStomachModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(SCULK_JAW_COMBINED_STOMACH, SculkJawCombinedStomachModel::createBodyLayer);
    }

    public static final ModelLayerLocation SCULK_JAW_STOMACH = createLocation("sculk_jaw_stomach", "main");
    public static final ModelLayerLocation SCULK_JAW_COMBINED_STOMACH = createLocation("sculk_jaw_combined_stomach", "main");

    public ModModelLayers() {
    }

    private static ModelLayerLocation createLocation(String string, String string2) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, string), string2);
    }
}
