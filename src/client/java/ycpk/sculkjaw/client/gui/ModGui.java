package ycpk.sculkjaw.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.registry.ModEffects;

@Environment(EnvType.CLIENT)
public class ModGui extends Gui {
    private static final ResourceLocation SCULKOPHOBIA_OUTLINE = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/misc/sculkophobia_outline.png");
    private final Minecraft minecraft;

    public ModGui(Minecraft minecraft) {
        super(minecraft);
        this.minecraft = minecraft;
    }

    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        super.render(guiGraphics, deltaTracker);
        if(!(this.minecraft.screen instanceof LevelLoadingScreen)) {
            if(!this.minecraft.options.hideGui) {
                this.renderCameraOverlays(guiGraphics, deltaTracker);
            }
        }
    }

    private void renderCameraOverlays(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        LocalPlayer localPlayer = this.minecraft.player;

        if(localPlayer.hasEffect(ModEffects.SCULKOPHOBIA_EFFECT)) {
            this.renderTextureOverlay(guiGraphics, SCULKOPHOBIA_OUTLINE, 1);
        }
    }

    private void renderTextureOverlay(GuiGraphics guiGraphics, ResourceLocation resourceLocation, float f) {
        int i = ARGB.white(f);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourceLocation, 0, 0, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight(), i);
    }
}
