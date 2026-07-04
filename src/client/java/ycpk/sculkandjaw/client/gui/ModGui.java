package ycpk.sculkandjaw.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModEffects;

@Environment(EnvType.CLIENT)
public class ModGui extends Gui {
    private static final ResourceLocation SCULKOPHOBIA_OUTLINE = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/misc/sculkophobia_outline.png");
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

        if(localPlayer.hasEffect(ModEffects.SCULKOPHOBIA)) {
            this.renderTextureOverlay(guiGraphics, SCULKOPHOBIA_OUTLINE, 1);
        }
    }

    private void renderTextureOverlay(GuiGraphics guiGraphics, ResourceLocation resourceLocation, float f) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, f);
        guiGraphics.blit(resourceLocation, 0, 0, -90, 0.0F, 0.0F, guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
