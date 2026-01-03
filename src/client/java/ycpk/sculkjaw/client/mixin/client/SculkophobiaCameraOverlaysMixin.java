package ycpk.sculkjaw.client.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.registry.ModEffects;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class SculkophobiaCameraOverlaysMixin {
    @Unique
    private boolean hadSculkophobiaLastFrame = false;
    @Unique
    private boolean isFadingIn = false;
    @Unique
    private float fadeInProgress = 0.0F;
    @Unique
    private int fadeInTicks = 0;
    @Shadow @Final private Minecraft minecraft;

    public SculkophobiaCameraOverlaysMixin() {

    }

    @Inject(method = {"renderCameraOverlays"}, at = {@At("TAIL")})
    private void renderSculkophobiaCameraOverlays(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        LocalPlayer localPlayer = this.minecraft.player;
        ResourceLocation SCULKOPHOBIA_OUTLINE_LOCATION = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/misc/sculkophobia_outline.png");
        boolean hasSculkophobiaNow = localPlayer.hasEffect(ModEffects.SCULKOPHOBIA_EFFECT);
        boolean effectJustGained = !hadSculkophobiaLastFrame && hasSculkophobiaNow;

        if(hasSculkophobiaNow) {
            int currentDuration = localPlayer.getEffect(ModEffects.SCULKOPHOBIA_EFFECT).getDuration();
            if (effectJustGained) {
                isFadingIn = true;
                fadeInProgress = 0.0F;
                fadeInTicks = 0;
            }
            if (isFadingIn) {
                fadeInTicks++;
                float fadeInDurationTicks = 60.0F;

                if (fadeInTicks <= fadeInDurationTicks) {
                    fadeInProgress = fadeInTicks / fadeInDurationTicks;
                    fadeInProgress = easeInQuad(fadeInProgress);
                } else {
                    fadeInProgress = 1.0F;
                    isFadingIn = false;
                }
            }
            float seconds = currentDuration / 20.0F;
            float fadeOutStartSeconds = 10.0F;
            float fadeOutDurationSeconds = 10.0F;
            float fadeOutProgress;
            if(seconds > fadeOutStartSeconds) {
                fadeOutProgress = 1.0F;
            }
            else {
                fadeOutProgress = Math.max(0.0F, Math.min(1.0F, seconds / fadeOutDurationSeconds));
                fadeOutProgress = easeOutQuad(fadeOutProgress);
            }
            float finalAlpha = fadeInProgress * fadeOutProgress;
            this.renderSculkophobiaOverlay(guiGraphics, SCULKOPHOBIA_OUTLINE_LOCATION, finalAlpha);
        }
        else {
            fadeInProgress = 0.0F;
            isFadingIn = false;
            fadeInTicks = 0;
        }
        hadSculkophobiaLastFrame = hasSculkophobiaNow;
    }

    @Unique
    private void renderSculkophobiaOverlay(GuiGraphics guiGraphics, ResourceLocation resourceLocation, float f) {
        int i = ARGB.white(f);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourceLocation, 0, 0, 0.0F, 0.0F,
                guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight(), i);
    }

    @Unique
    private float easeInQuad(float x) {
        return x * x;
    }

    @Unique
    private float easeOutQuad(float x) {
        return 1.0F - (1.0F - x) * (1.0F - x);
    }

}
