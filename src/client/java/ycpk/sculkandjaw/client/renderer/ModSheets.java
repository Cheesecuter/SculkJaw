package ycpk.sculkandjaw.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkandjaw.SculkAndJaw;

@Environment(EnvType.CLIENT)
public class ModSheets {
    public static final ResourceLocation SCULK_JAW_STOMACH_SHEET = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/atlas/sculk_jaw_stomach.png");
    private static final RenderType SCULK_JAW_STOMACH_SHEET_TYPE = RenderType.entitySolid(SCULK_JAW_STOMACH_SHEET);

    public static void registerModSheets() {
        SculkAndJaw.LOGGER.info("Registering Sheets for Mod " + SculkAndJaw.MOD_ID);
    }

    public static RenderType sculkJawStomachSheet() {
        return SCULK_JAW_STOMACH_SHEET_TYPE;
    }
}
