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
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.registry.ModEffects;

import java.lang.reflect.Method;

@Environment(EnvType.CLIENT)
@Mixin(value = Gui.class)
public class SculkophobiaHeartMixin {
    @Shadow @Final private RandomSource random;
    @Unique
    int sculkophobiaHearts = 0;
    @Unique
    private static final ResourceLocation sculkophobiaFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_full.png");
    @Unique
    private static final ResourceLocation sculkophobiaFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_full_blinking.png");
    @Unique
    private static final ResourceLocation sculkophobiaFullHeartHardcore = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_hardcore_full.png");
    @Unique
    private static final ResourceLocation sculkophobiaFullHeartHardcoreBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_hardcore_full_blinking.png");

    @Unique
    private static final ResourceLocation container = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/container.png");
    @Unique
    private static final ResourceLocation containerBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/container_blinking.png");
    @Unique
    private static final ResourceLocation containerHardcore = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/container_hardcore.png");
    @Unique
    private static final ResourceLocation containerHardcoreBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/container_hardcore_blinking.png");

    @Unique
    private static final ResourceLocation normalFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/full.png");
    @Unique
    private static final ResourceLocation normalFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/full_blinking.png");
    @Unique
    private static final ResourceLocation normalHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/blinking.png");
    @Unique
    private static final ResourceLocation normalHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/half_blinking.png");
    @Unique
    private static final ResourceLocation normalHardcoreFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/hardcore_full.png");
    @Unique
    private static final ResourceLocation normalHardcoreFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation normalHardcoreHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/hardcore_half.png");
    @Unique
    private static final ResourceLocation normalHardcoreHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/hardcore_half_blinking.png");

    @Unique
    private static final ResourceLocation posionedFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_full.png");
    @Unique
    private static final ResourceLocation posionedFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_full_blinking.png");
    @Unique
    private static final ResourceLocation posionedHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_half.png");
    @Unique
    private static final ResourceLocation posionedHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_half_blinking.png");
    @Unique
    private static final ResourceLocation posionedHardcoreFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_hardcore_full.png");
    @Unique
    private static final ResourceLocation posionedHardcoreFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation posionedHardcoreHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_hardcore_half.png");
    @Unique
    private static final ResourceLocation posionedHardcoreHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_hardcore_half_blinking.png");

    @Unique
    private static final ResourceLocation witheredFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_full.png");
    @Unique
    private static final ResourceLocation witheredFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_full_blinking.png");
    @Unique
    private static final ResourceLocation witheredHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_half.png");
    @Unique
    private static final ResourceLocation witheredHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_half_blinking.png");
    @Unique
    private static final ResourceLocation witheredHardcoreFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_hardcore_full.png");
    @Unique
    private static final ResourceLocation witheredHardcoreFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation witheredHardcoreHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_hardcore_half.png");
    @Unique
    private static final ResourceLocation witheredHardcoreHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_hardcore_half_blinking.png");

    @Unique
    private static final ResourceLocation absorbingFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_full.png");
    @Unique
    private static final ResourceLocation absorbingFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_full_blinking.png");
    @Unique
    private static final ResourceLocation absorbingHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_half.png");
    @Unique
    private static final ResourceLocation absorbingHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_half_blinking.png");
    @Unique
    private static final ResourceLocation absorbingHardcoreFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_hardcore_full.png");
    @Unique
    private static final ResourceLocation absorbingHardcoreFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation absorbingHardcoreHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_hardcore_half.png");
    @Unique
    private static final ResourceLocation absorbingHardcoreHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_hardcore_half_blinking.png");

    @Unique
    private static final ResourceLocation frozenFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_full.png");
    @Unique
    private static final ResourceLocation frozenFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_full_blinking.png");
    @Unique
    private static final ResourceLocation frozenHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_half.png");
    @Unique
    private static final ResourceLocation frozenHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_half_blinking.png");
    @Unique
    private static final ResourceLocation frozenHardcoreFullHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_hardcore_full.png");
    @Unique
    private static final ResourceLocation frozenHardcoreFullHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation frozenHardcoreHalfHeart = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_hardcore_half.png");
    @Unique
    private static final ResourceLocation frozenHardcoreHalfHeartBlinking = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_hardcore_half_blinking.png");

    private static Class<?> HEART_TYPE_ENUM = null;
    private static Method GET_SPRITE_METHOD = null;
    private static Method FOR_PLAYER_METHOD = null;
    private static Class<?> GUI_CLASS = null;
    private static Method RENDER_HEART_METHOD = null;


    public SculkophobiaHeartMixin() {

    }

    /*@Redirect(
            method = "renderHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;" +
                            "renderHeart(" +
                            "Lnet/minecraft/client/gui/GuiGraphics;" +
                            "Lnet/minecraft/client/gui/Gui$HeartType;" +
                            "IIZZZ)V"
            )
    )
    private void redirectRenderHeart(
            Gui instance,
            GuiGraphics guiGraphics,
            @Coerce Object heartType,
            int x, int y,
            boolean hardcore,
            boolean blinking,
            boolean blinking
    ) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null
                && "NORMAL".equals(heartType.toString())
                && player.hasEffect(ModEffects.SCULKOPHOBIA_EFFECT)
                && shouldRenderSculkophobiaHeart(player, x)) {
            renderSculkophobiaHeart(guiGraphics, x, y, blinking);
            return;
        }

    }

    @Unique
    private boolean shouldRenderSculkophobiaHeart(Player player, int heartX) {
        int amplifier = player.getEffect(ModEffects.SCULKOPHOBIA_EFFECT).getAmplifier();
        int customHearts = amplifier + 1;
        int baseX = getHeartBaseX();
        int column = (heartX - baseX) / 8;
        return column >= 10 - customHearts;
    }

    @Unique
    private int getHeartBaseX() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 91;
    }

    @Unique
    private void renderSculkophobiaHeart(GuiGraphics guiGraphics, int x, int y, boolean hardcore, boolean blinking, boolean blinking) {
        ResourceLocation sprite = blinking ? sculkophobiaFullHeartBlinking : sculkophobiaFullHeart;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprite, x, y, 0.0F, 0.0F, 9, 9, 9, 9);
    }*/

    @Unique
    private ResourceLocation getSprite(boolean hardcore, boolean blinking, boolean half, int heartType) {
        switch (heartType) {
            case 0:
                if (!hardcore) {
                    return blinking ? containerBlinking : container;
                } else {
                    return blinking ? containerHardcoreBlinking : containerHardcore;
                }
            case 1:
                if (!hardcore) {
                    if (half) {
                        return blinking ? normalHalfHeartBlinking : normalHalfHeart;
                    } else {
                        return blinking ? normalFullHeartBlinking : normalFullHeart;
                    }
                } else if (half) {
                    return blinking ? normalHardcoreHalfHeartBlinking : normalHardcoreHalfHeart;
                } else {
                    return blinking ? normalHardcoreFullHeartBlinking : normalHardcoreFullHeart;
                }
            case 2:
                if (!hardcore) {
                    if (half) {
                        return blinking ? posionedHalfHeartBlinking : posionedHalfHeart;
                    } else {
                        return blinking ? posionedFullHeartBlinking : posionedFullHeart;
                    }
                } else if (half) {
                    return blinking ? posionedHardcoreHalfHeartBlinking : posionedHardcoreHalfHeart;
                } else {
                    return blinking ? posionedHardcoreFullHeartBlinking : posionedHardcoreFullHeart;
                }
            case 3:
                if (!hardcore) {
                    if (half) {
                        return blinking ? witheredHalfHeartBlinking : witheredHalfHeart;
                    } else {
                        return blinking ? witheredFullHeartBlinking : witheredFullHeart;
                    }
                } else if (half) {
                    return blinking ? witheredHardcoreHalfHeartBlinking : witheredHardcoreHalfHeart;
                } else {
                    return blinking ? witheredHardcoreFullHeartBlinking : witheredHardcoreFullHeart;
                }
            case 4:
                if (!hardcore) {
                    if (half) {
                        return blinking ? absorbingHalfHeartBlinking : absorbingHalfHeart;
                    } else {
                        return blinking ? absorbingFullHeartBlinking : absorbingFullHeart;
                    }
                } else if (half) {
                    return blinking ? absorbingHardcoreHalfHeartBlinking : absorbingHardcoreHalfHeart;
                } else {
                    return blinking ? absorbingHardcoreFullHeartBlinking : absorbingHardcoreFullHeart;
                }
            case 5:
                if (!hardcore) {
                    if (half) {
                        return blinking ? frozenHalfHeartBlinking : frozenHalfHeart;
                    } else {
                        return blinking ? frozenFullHeartBlinking : frozenFullHeart;
                    }
                } else if (half) {
                    return blinking ? frozenHardcoreHalfHeartBlinking : frozenHardcoreHalfHeart;
                } else {
                    return blinking ? frozenHardcoreFullHeartBlinking : frozenHardcoreFullHeart;
                }
            case 6:
                if (!hardcore) {
                    return blinking ? sculkophobiaFullHeartBlinking : sculkophobiaFullHeart;
                } else {
                    return blinking ? sculkophobiaFullHeartHardcoreBlinking : sculkophobiaFullHeartHardcore;
                }
            default:
                return normalFullHeart;
        }
    }

    @Unique
    private void renderHeart(GuiGraphics guiGraphics, int x, int y, boolean hardcore,  boolean blinking, boolean half, int heartType) {
        ResourceLocation sprite = getSprite(hardcore, blinking, half, heartType);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, sprite, x, y, 0.0F, 0.0F, 9, 9, 9, 9);
    }

    @Inject(method = {"renderHearts"}, at = {@At("HEAD")})
    private void renderSculkophobiaHearts(GuiGraphics guiGraphics, Player player,
                                          int x, int y, int lines, int regeneratingHeartIndex,
                                          float maxHealth, int lastHealth, int health,
                                          int absorption, boolean blinking, CallbackInfo ci) {
        if(!(player instanceof LocalPlayer) || !player.hasEffect(ModEffects.SCULKOPHOBIA_EFFECT)) {
            return;
        }
        int heartType = 1;
        if(player.hasEffect(MobEffects.POISON)) {
            heartType = 2;
        } else if(player.hasEffect(MobEffects.WITHER)) {
            heartType = 3;
        } else if(player.isFullyFrozen()) {
            heartType = 5;
        } else {
            heartType = 1;
        }
        int amplifier = player.getEffect(ModEffects.SCULKOPHOBIA_EFFECT).getAmplifier();
        sculkophobiaHearts = amplifier + 1;
        /*if(sculkophobiaHearts <= 0) {
            return;
        }*/

        boolean bl2 = player.level().getLevelData().isHardcore();
        int p = Mth.ceil((double)maxHealth / 2.0);
        int q = Mth.ceil((double)absorption / 2.0);
        int r = p * 2 + sculkophobiaHearts;

        for(int s = p + q + sculkophobiaHearts - 1; s >= 0; --s) {
            int t = s / 10;
            int u = s % 10;
            int v = x + u * 8;
            int w = y - t * lines;
            if (lastHealth + absorption <= 4) {
                w += this.random.nextInt(2);
            }

            if (s < p && s == regeneratingHeartIndex) {
                w -= 2;
            }

            int s2 = s * 2;
            boolean bl3 = s >= p;
            boolean bl5;
            if(sculkophobiaHearts >= 1) {
                heartType = 6;
                v += this.random.nextInt(2);
                w += this.random.nextInt(2);
                if (blinking) {
                    bl5 = s2 + 1 == health;
                    renderHeart(guiGraphics, v, w, bl2, blinking, false, 0);
                    this.renderHeart(guiGraphics, v, w, bl2, true, bl5, heartType);
                } else {
                    bl5 = s2 + 1 == lastHealth;
                    renderHeart(guiGraphics, v, w, bl2, blinking, false, 0);
                    this.renderHeart(guiGraphics, v, w, bl2, false, bl5, heartType);
                }
                sculkophobiaHearts--;
                continue;
            }
            renderHeart(guiGraphics, v, w, bl2, blinking, false, 0);
            if (bl3) {
                int o2 = s2 - r;
                if (o2 < absorption) {
                    boolean bl4 = y + 1 == absorption;
                    this.renderHeart(guiGraphics, v, w, bl2, false, bl4, heartType == 3 ? heartType : 4);
                }
            }
            if (blinking && s2 < health) {
                bl5 = s2 + 1 == health;
                this.renderHeart(guiGraphics, v, w, bl2, true, bl5, heartType);
            }

            if (s2 < lastHealth) {
                bl5 = s2 + 1 == lastHealth;
                this.renderHeart(guiGraphics, v, w, bl2, false, bl5, heartType);
            }
        }

        /*int p = Mth.ceil((double) maxHealth / 2.0);
        int q = Mth.ceil((double) absorption / 2.0);
        int r = p * 2;

        for(int s = p + q - 1; s >= 0; --s) {
            int t = s / 10;
            int u = s % 10;
            int v = x + u * 8;
            int w = y - t * lines;
            int s2 = s * 2;
            boolean bl3 = s >= p;

        }
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
        }*/
    }

    @ModifyConstant(method = {"renderHearts"}, constant = {@Constant(expandZeroConditions = {Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO})})
    public int skipHeartRender(int constant) {
        if(constant >= 20 - this.sculkophobiaHearts && constant < 20) {
            return -1;
        }
        return constant;
    }

    private static Object getGuiRenderHeart(GuiGraphics guiGraphics, Object heartType, int i, int j, boolean bl, boolean bl2, boolean bl3) {
        try{
            if(GUI_CLASS == null) {
                GUI_CLASS = Class.forName("net.minecraft.client.gui.Gui");
                RENDER_HEART_METHOD = GUI_CLASS.getDeclaredMethod("renderHeart", GuiGraphics.class, Object.class, int.class, int.class, boolean.class, boolean.class, boolean.class);
                RENDER_HEART_METHOD.setAccessible(true);
            }
            return RENDER_HEART_METHOD.invoke(null, guiGraphics, heartType, i, j, bl, bl2, bl3);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getHeartTypeGetSprite(boolean bl, boolean bl2, boolean bl3) {
        try{
            if(HEART_TYPE_ENUM == null) {
                HEART_TYPE_ENUM = Class.forName("net.minecraft.client.gui.Gui$HeartType");
                GET_SPRITE_METHOD = HEART_TYPE_ENUM.getDeclaredMethod("getSprite", boolean.class, boolean.class, boolean.class);
                GET_SPRITE_METHOD.setAccessible(true);
            }
            return GET_SPRITE_METHOD.invoke(null, bl, bl2, bl3);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "this.full";
        }
    }

    private static Object getHeartTypeForPlayer(Player player) {
        try{
            if(HEART_TYPE_ENUM == null) {
                HEART_TYPE_ENUM = Class.forName("net.minecraft.client.gui.Gui$HeartType");
                FOR_PLAYER_METHOD = HEART_TYPE_ENUM.getDeclaredMethod("forPlayer", Player.class);
                FOR_PLAYER_METHOD.setAccessible(true);
            }
            return FOR_PLAYER_METHOD.invoke(null, player);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "NORMAL";
        }
    }
}

