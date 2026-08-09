package ycpk.sculkandjaw.client.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModMobEffects;

@Environment(EnvType.CLIENT)
@Mixin(value = Gui.class)
public class SculkophobiaHeartMixin {
    @Shadow
    @Final
    private RandomSource random;
    @Shadow @Final private static ResourceLocation GUI_ICONS_LOCATION;
    @Unique
    int vehicleSculkophobiaHearts = 0;
    @Unique
    private static final ResourceLocation sculkophobiaFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_full.png");
    @Unique
    private static final ResourceLocation sculkophobiaFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_full_blinking.png");
    @Unique
    private static final ResourceLocation sculkophobiaFullHeartHardcore = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_hardcore_full.png");
    @Unique
    private static final ResourceLocation sculkophobiaFullHeartHardcoreBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/heart/sculkophobia_hardcore_full_blinking.png");

    @Unique
    private static final ResourceLocation container = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/container.png");
    @Unique
    private static final ResourceLocation containerBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/container_blinking.png");
    @Unique
    private static final ResourceLocation containerHardcore = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/container_hardcore.png");
    @Unique
    private static final ResourceLocation containerHardcoreBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/container_hardcore_blinking.png");

    @Unique
    private static final ResourceLocation normalFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/full.png");
    @Unique
    private static final ResourceLocation normalFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/full_blinking.png");
    @Unique
    private static final ResourceLocation normalHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/blinking.png");
    @Unique
    private static final ResourceLocation normalHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/half_blinking.png");
    @Unique
    private static final ResourceLocation normalHardcoreFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/hardcore_full.png");
    @Unique
    private static final ResourceLocation normalHardcoreFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation normalHardcoreHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/hardcore_half.png");
    @Unique
    private static final ResourceLocation normalHardcoreHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/hardcore_half_blinking.png");

    @Unique
    private static final ResourceLocation posionedFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_full.png");
    @Unique
    private static final ResourceLocation posionedFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_full_blinking.png");
    @Unique
    private static final ResourceLocation posionedHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_half.png");
    @Unique
    private static final ResourceLocation posionedHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_half_blinking.png");
    @Unique
    private static final ResourceLocation posionedHardcoreFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_hardcore_full.png");
    @Unique
    private static final ResourceLocation posionedHardcoreFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation posionedHardcoreHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_hardcore_half.png");
    @Unique
    private static final ResourceLocation posionedHardcoreHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/poisoned_hardcore_half_blinking.png");

    @Unique
    private static final ResourceLocation witheredFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_full.png");
    @Unique
    private static final ResourceLocation witheredFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_full_blinking.png");
    @Unique
    private static final ResourceLocation witheredHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_half.png");
    @Unique
    private static final ResourceLocation witheredHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_half_blinking.png");
    @Unique
    private static final ResourceLocation witheredHardcoreFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_hardcore_full.png");
    @Unique
    private static final ResourceLocation witheredHardcoreFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation witheredHardcoreHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_hardcore_half.png");
    @Unique
    private static final ResourceLocation witheredHardcoreHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/withered_hardcore_half_blinking.png");

    @Unique
    private static final ResourceLocation absorbingFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_full.png");
    @Unique
    private static final ResourceLocation absorbingFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_full_blinking.png");
    @Unique
    private static final ResourceLocation absorbingHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_half.png");
    @Unique
    private static final ResourceLocation absorbingHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_half_blinking.png");
    @Unique
    private static final ResourceLocation absorbingHardcoreFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_hardcore_full.png");
    @Unique
    private static final ResourceLocation absorbingHardcoreFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation absorbingHardcoreHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_hardcore_half.png");
    @Unique
    private static final ResourceLocation absorbingHardcoreHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/absorbing_hardcore_half_blinking.png");

    @Unique
    private static final ResourceLocation frozenFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_full.png");
    @Unique
    private static final ResourceLocation frozenFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_full_blinking.png");
    @Unique
    private static final ResourceLocation frozenHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_half.png");
    @Unique
    private static final ResourceLocation frozenHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_half_blinking.png");
    @Unique
    private static final ResourceLocation frozenHardcoreFullHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_hardcore_full.png");
    @Unique
    private static final ResourceLocation frozenHardcoreFullHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_hardcore_full_blinking.png");
    @Unique
    private static final ResourceLocation frozenHardcoreHalfHeart = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_hardcore_half.png");
    @Unique
    private static final ResourceLocation frozenHardcoreHalfHeartBlinking = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/frozen_hardcore_half_blinking.png");

    @Unique
    private static final ResourceLocation vehicleContainer = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/vehicle_container.png");
    @Unique
    private static final ResourceLocation vehicleFull = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/vehicle_full.png");
    @Unique
    private static final ResourceLocation vehicleHalf = new ResourceLocation(SculkAndJaw.MOD_ID, "textures/gui/sprites/hud/vanilla/heart/vehicle_half.png");

    @Unique
    private static final ResourceLocation armorEmptySprite = new ResourceLocation("textures/gui/sprites/hud/armor_empty.png");
    @Unique
    private static final ResourceLocation armorHalfSprite = new ResourceLocation("textures/gui/sprites/hud/armor_half.png");
    @Unique
    private static final ResourceLocation armorFullSprite = new ResourceLocation("textures/gui/sprites/hud/armor_full.png");

    public SculkophobiaHeartMixin() {

    }

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
    private void renderHeart(GuiGraphics guiGraphics, int x, int y, boolean hardcore, boolean blinking, boolean half, int heartType) {
        RenderSystem.enableBlend();
        guiGraphics.blit(getSprite(hardcore, blinking, half, heartType), x, y, 0, 0, 9, 9, 9, 9);
        RenderSystem.disableBlend();
    }

    @Inject(method = {"renderHearts"}, at = {@At("HEAD")})
    private void renderSculkophobiaHearts(GuiGraphics guiGraphics, Player player,
                                          int x, int y, int lines, int regeneratingHeartIndex,
                                          float maxHealth, int lastHealth, int health,
                                          int absorption, boolean blinking, CallbackInfo ci) {
        if(!(player instanceof LocalPlayer) || !player.hasEffect(ModMobEffects.SCULKOPHOBIA)) {
            return;
        }
        Gui gui = Minecraft.getInstance().gui;
        GuiAccessor guiAccessor = (GuiAccessor) gui;
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
        int sculkophobiaHearts = Mth.ceil(player.getEffect(ModMobEffects.SCULKOPHOBIA).getAmplifier()) + 1;
        float f = Math.max(((float)player.getAttributeValue(Attributes.MAX_HEALTH) + sculkophobiaHearts * 2), (float)Math.max(guiAccessor.getDisplayHealth(), guiAccessor.getLastHealth()));
        int o = Mth.ceil(player.getAbsorptionAmount());
        int p2 = Mth.ceil((f + (float)o) / 2.0F / 10.0F);
        lines = Math.max(10 - (p2 - 2), 3);

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
    }

    /*@Redirect(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderArmor(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIII)V"))
    private static void renderArmorRedirect(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l) {
        int m = player.getArmorValue();
        if (m > 0) {
            int n = i - (j - 1) * k - 10;
            if (player.hasEffect(ModMobEffects.SCULKOPHOBIA)) {
                Gui gui = Minecraft.getInstance().gui;
                GuiAccessor guiAccessor = (GuiAccessor) gui;
                int sculkophobiaHearts = Mth.ceil(player.getEffect(ModMobEffects.SCULKOPHOBIA).getAmplifier()) + 1;
                float f = Math.max(((float)player.getAttributeValue(Attributes.MAX_HEALTH) + sculkophobiaHearts * 2), (float)Math.max(guiAccessor.getLastHealth(), guiAccessor.getDisplayHealth()));
                int o = Mth.ceil(player.getAbsorptionAmount());
                int p = Mth.ceil((f + (float)o) / 2.0F / 10.0F);
                int q = Math.max(10 - (p - 2), 3);
                n = i - (p - 1) * q - 10;
            }

            for(int o = 0; o < 10; ++o) {
                int p = l + o * 8;
                if (o * 2 + 1 < m) {
                    guiGraphics.blitSprite(armorFullSprite, p, n, 9, 9);
                }

                if (o * 2 + 1 == m) {
                    guiGraphics.blitSprite(armorHalfSprite, p, n, 9, 9);
                }

                if (o * 2 + 1 > m) {
                    guiGraphics.blitSprite(armorEmptySprite, p, n, 9, 9);
                }
            }

        }
    }*/
}
