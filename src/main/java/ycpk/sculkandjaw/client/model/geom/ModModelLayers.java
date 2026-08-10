package ycpk.sculkandjaw.client.model.geom;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModModelLayers {
    public static void registerModModelLayers() {
        SculkAndJaw.LOGGER.info("Registering Model Layers for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final ModelLayerLocation SCULK_JAW_STOMACH = createLocation("sculk_jaw_stomach", "main");
    public static final ModelLayerLocation SCULK_JAW_COMBINED_STOMACH = createLocation("sculk_jaw_combined_stomach", "main");

    public ModModelLayers() {
    }

    private static ModelLayerLocation createLocation(String string, String string2) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, string), string2);
    }
}
