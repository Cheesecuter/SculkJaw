package ycpk.sculkjaw.client.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
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
    @Unique
    float sculkophobiaHearts = 0.0F;

    public SculkophobiaHeartMixin() {

    }

    @Inject(method = {"renderHearts"}, at = {@At("TAIL")})
    private void renderSculkophobiaHearts(GuiGraphics guiGraphics, Player player,
                                          int x, int y, int lines, int regeneratingHeartIndex,
                                          float maxHealth, int lastHealth, int health,
                                          int absorption, boolean blinking, CallbackInfo ci) {
        if(!(player instanceof LocalPlayer)) {
            return;
        }
        sculkophobiaHearts = (20 - maxHealth) / 2;
        if(sculkophobiaHearts <= 0) {
            return;
        }
        ResourceLocation textureId = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_full.png");
        for(int i = 0; i < this.sculkophobiaHearts; ++i) {
            int row = i / 10;
            int column = i % 10;
            int x2 = x + (9 - column) * 8;
            int y2 = y - row * 10;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, textureId, x2, y2, 0.0F, 0.0F, 9, 9, 9, 9);
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
