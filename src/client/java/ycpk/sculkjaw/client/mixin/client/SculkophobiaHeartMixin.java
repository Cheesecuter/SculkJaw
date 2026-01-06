package ycpk.sculkjaw.client.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkjaw.Sculkjaw;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class SculkophobiaHeartMixin {
    @Shadow @Final private RandomSource random;
    @Unique
    float sculkophobiaHearts = 0.0F;
    @Unique
    private static final ResourceLocation fullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_full.png");
    @Unique
    private static final ResourceLocation fullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_full_blinking.png");
    @Unique
    private static final ResourceLocation fullHeartHardcore = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_hardcore_full.png");
    @Unique
    private static final ResourceLocation fullHeartHardcoreBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation container = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/container.png");
    @Unique
    private static final ResourceLocation containerBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/container_blinking.png");
    @Unique
    private static final ResourceLocation containerHardcore = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/container_hardcore.png");
    @Unique
    private static final ResourceLocation containerHardcoreBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/container_hardcore_blinking.png");

    public SculkophobiaHeartMixin() {

    }

    @Inject(method = {"renderHearts"}, at = {@At("TAIL")})
    private void renderSculkophobiaHearts(GuiGraphics guiGraphics, Player player,
                                          int x, int y, int lines, int regeneratingHeartIndex,
                                          float maxHealth, int lastHealth, int health,
                                          int absorption, boolean half, CallbackInfo ci) {
        if(!(player instanceof LocalPlayer)) {
            return;
        }
        sculkophobiaHearts = (20 - maxHealth) / 2;
        if(sculkophobiaHearts <= 0) {
            return;
        }
        int p = Mth.ceil((double) maxHealth / 2.0);
        int q = Mth.ceil((double) absorption / 2.0);
        int s = p + q - 1;
        int s2 = s * 2;
        boolean bl = (s2 + 1) == lastHealth;

        for(int i = 0; i < this.sculkophobiaHearts; ++i) {
            int row = i / 10;
            int column = i % 10;
            int x2 = x + (9 - column) * 8;
            int y2 = y - row * 10;
            x2 += this.random.nextInt(2);
            y2 += this.random.nextInt(2);
            if(player.level().getLevelData().isHardcore()) {
                if(bl) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, containerHardcoreBlinking, x2, y2, 0.0F, 0.0F, 9, 9, 9, 9);
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, fullHeartHardcoreBlinking, x2, y2, 0.0F, 0.0F, 9, 9, 9, 9);
                }
                else {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, containerHardcore, x2, y2, 0.0F, 0.0F, 9, 9, 9, 9);
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, fullHeartHardcore, x2, y2, 0.0F, 0.0F, 9, 9, 9, 9);
                }
            }
            else {
                if(bl) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, containerBlinking, x2, y2, 0.0F, 0.0F, 9, 9, 9, 9);
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, fullHeartBlinking, x2, y2, 0.0F, 0.0F, 9, 9, 9, 9);
                }
                else {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, container, x2, y2, 0.0F, 0.0F, 9, 9, 9, 9);
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, fullHeart, x2, y2, 0.0F, 0.0F, 9, 9, 9, 9);
                }
            }
        }
    }

    @ModifyConstant(method = {"renderHearts"}, constant = {@Constant(expandZeroConditions = {Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO})})
    public int skipHeartRender(int constant) {
        if(constant >= 20 - this.sculkophobiaHearts && constant < 20) {
            return -1;
        }
        return constant;
    }
}
