package ycpk.sculkandjaw.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModMobEffects;

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
    @Shadow
    @Final
    private Minecraft minecraft;
    @Unique
    private Identifier SCULKOPHOBIA_TENDRIL1_LOCATION = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/misc/sculkophobia_tendril1.png");
    @Unique
    private Identifier SCULKOPHOBIA_TENDRIL2_LOCATION = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/misc/sculkophobia_tendril2.png");
    @Unique
    private Identifier SCULKOPHOBIA_TENDRIL3_LOCATION = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/misc/sculkophobia_tendril3.png");
    @Unique
    private Identifier SCULKOPHOBIA_TENDRIL4_LOCATION = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/misc/sculkophobia_tendril4.png");
    @Unique
    private Identifier SCULKOPHOBIA_OUTLINE_LOCATION = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/misc/sculkophobia_outline.png");

    public SculkophobiaCameraOverlaysMixin() {
        SculkAndJaw.LOGGER.info("SculkophobiaCameraOverlaysMixin initialized1");
        SculkAndJaw.LOGGER.info("SculkophobiaCameraOverlaysMixin initialized2");
        SculkAndJaw.LOGGER.info("SculkophobiaCameraOverlaysMixin initialized3");
        SculkAndJaw.LOGGER.info("SculkophobiaCameraOverlaysMixin initialized4");
    }

    @Inject(method = {"renderCameraOverlays"}, at = {@At("TAIL")})
    private void renderSculkophobiaCameraOverlays(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        LocalPlayer localPlayer = this.minecraft.player;
        boolean hasSculkophobiaNow = localPlayer.hasEffect(ModMobEffects.SCULKOPHOBIA_EFFECT);
        boolean effectJustGained = !hadSculkophobiaLastFrame && hasSculkophobiaNow;

        if(hasSculkophobiaNow) {
            int currentDuration = localPlayer.getEffect(ModMobEffects.SCULKOPHOBIA_EFFECT).getDuration();
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

            float m1 = (float) (localPlayer.getRandom().nextInt(0, 10) % 10 * 0.1);
            float m2 = (float) (localPlayer.getRandom().nextInt(10, 20) % 10 * 0.1);
            float m3 = (float) (localPlayer.getRandom().nextInt(20, 30) % 10 * 0.1);
            float m4 = (float) (localPlayer.getRandom().nextInt(30, 40) % 10 * 0.1);

            this.renderSculkophobiaOverlay(guiGraphics, SCULKOPHOBIA_OUTLINE_LOCATION, finalAlpha,
                    0, 0,
                    0.0F, 0.0F,
                    guiGraphics.guiWidth(), guiGraphics.guiHeight(),
                    guiGraphics.guiWidth(), guiGraphics.guiHeight());

            this.renderSculkophobiaOverlay(guiGraphics, SCULKOPHOBIA_TENDRIL1_LOCATION, finalAlpha,
                    0, 0,
                    0.0F + m1, 0.0F + m1,
                    guiGraphics.guiWidth() / 2, guiGraphics.guiHeight() / 2,
                    guiGraphics.guiWidth() / 2 + 1, guiGraphics.guiHeight() / 2 + 1);

            this.renderSculkophobiaOverlay(guiGraphics, SCULKOPHOBIA_TENDRIL2_LOCATION, finalAlpha,
                    guiGraphics.guiWidth() / 2, 0,
                    0.0F + m2, 0.0F + m2,
                    guiGraphics.guiWidth() / 2, guiGraphics.guiHeight() / 2,
                    guiGraphics.guiWidth() / 2 + 1, guiGraphics.guiHeight() / 2 + 1);

            this.renderSculkophobiaOverlay(guiGraphics, SCULKOPHOBIA_TENDRIL3_LOCATION, finalAlpha,
                    0, guiGraphics.guiHeight() / 2,
                    0.0F + m3, 0.0F + m3,
                    guiGraphics.guiWidth() / 2, guiGraphics.guiHeight() / 2,
                    guiGraphics.guiWidth() / 2 + 1, guiGraphics.guiHeight() / 2 + 1);

            this.renderSculkophobiaOverlay(guiGraphics, SCULKOPHOBIA_TENDRIL4_LOCATION, finalAlpha,
                    guiGraphics.guiWidth() / 2, guiGraphics.guiHeight() / 2,
                    0.0F + m4, 0.0F + m4,
                    guiGraphics.guiWidth() / 2, guiGraphics.guiHeight() / 2,
                    guiGraphics.guiWidth() / 2 + 1, guiGraphics.guiHeight() / 2 + 1);

        }
        else {
            fadeInProgress = 0.0F;
            isFadingIn = false;
            fadeInTicks = 0;
        }
        hadSculkophobiaLastFrame = hasSculkophobiaNow;
    }

    @Unique
    private void renderSculkophobiaOverlay(GuiGraphics guiGraphics, Identifier resourceLocation, float argb,
                                           int i, int j, float f, float g,
                                           int width1, int height1, int width2, int height2) {
        int a = ARGB.white(argb);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourceLocation, i, j, f, g,
                width1, height1, width2, height2, a);
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
