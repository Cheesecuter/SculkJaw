package ycpk.sculkjaw.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MaterialMapper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkjaw.Sculkjaw;

@Environment(EnvType.CLIENT)
public class ModSheets {
    public static final ResourceLocation SCULK_JAW_STOMACH_SHEET = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/atlas/sculk_jaw_stomach.png");
    private static final RenderType SCULK_JAW_STOMACH_SHEET_TYPE = RenderType.entitySolid(SCULK_JAW_STOMACH_SHEET);
    public static final MaterialMapper SCULK_JAW_STOMACH_MAPPER = new MaterialMapper(SCULK_JAW_STOMACH_SHEET, "entity/sculk_jaw");
    public static final Material SCULK_JAW_STOMACH = SCULK_JAW_STOMACH_MAPPER.apply(ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "sculk_jaw_stomach"));

    public static void registerModSheets() {
        Sculkjaw.LOGGER.info("Registering Sheets for Mod " + Sculkjaw.MOD_ID);
    }

    public static RenderType sculkJawStomachSheet() {
        return SCULK_JAW_STOMACH_SHEET_TYPE;
    }
}
