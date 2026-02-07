package ycpk.sculkjaw.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import ycpk.sculkjaw.core.particles.ModParticleTypes;
import ycpk.sculkjaw.level.material.ModFluids;

public class DrippingSculkAcidParticle extends SingleQuadParticle {
    private final Fluid type;
    protected boolean isGlowing;
    DrippingSculkAcidParticle(ClientLevel clientLevel,
                              double d, double e, double f,
                              Fluid fluid, TextureAtlasSprite textureAtlasSprite) {
        super(clientLevel, d, e, f, textureAtlasSprite);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.type = fluid;
    }

    protected Fluid getType() {
        return this.type;
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    @Override
    public int getLightColor(float f) {
        return this.isGlowing ? 240 : super.getLightColor(f);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (!this.removed) {
            this.yd -= (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.postMoveUpdate();
            if (!this.removed) {
                this.xd *= 0.9800000190734863;
                this.yd *= 0.9800000190734863;
                this.zd *= 0.9800000190734863;
                if (this.type != Fluids.EMPTY) {
                    BlockPos blockPos = BlockPos.containing(this.x, this.y, this.z);
                    FluidState fluidState = this.level.getFluidState(blockPos);
                    if (fluidState.getType() == this.type && this.y < (double)((float)blockPos.getY() + fluidState.getHeight(this.level, blockPos))) {
                        this.remove();
                    }

                }
            }
        }
    }

    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }

    }

    protected void postMoveUpdate() {
    }

    @Environment(EnvType.CLIENT)
    public static class SculkAcidLandProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public SculkAcidLandProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                       double d, double e, double f,
                                       double g, double h, double i,
                                       RandomSource randomSource) {
            DrippingSculkAcidParticle drippingSculkAcidParticle = new DripLandParticle(clientLevel, d, e, f, ModFluids.SCULK_ACID, this.sprite.get(randomSource));
            ((DrippingSculkAcidParticle) drippingSculkAcidParticle).setColor(0.16F, 0.59F, 0.51F);
            return drippingSculkAcidParticle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class SculkAcidFallProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public SculkAcidFallProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                       double d, double e, double f,
                                       double g, double h, double i,
                                       RandomSource randomSource) {
            DrippingSculkAcidParticle drippingSculkAcidParticle = new FallAndLandParticle(clientLevel, d, e, f, ModFluids.SCULK_ACID, ModParticleTypes.LANDING_SCULK_ACID, this.sprite.get(randomSource));
            ((DrippingSculkAcidParticle) drippingSculkAcidParticle).setColor(0.16F, 0.59F, 0.51F);
            return drippingSculkAcidParticle;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class SculkAcidHangProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public SculkAcidHangProvider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                       double d, double e, double f,
                                       double g, double h, double i,
                                       RandomSource randomSource) {
            DrippingSculkAcidParticle drippingSculkAcidParticle = new DripHangParticle(clientLevel, d, e, f, ModFluids.SCULK_ACID, ModParticleTypes.DRIPPING_SCULK_ACID, this.sprite.get(randomSource));
            ((DrippingSculkAcidParticle) drippingSculkAcidParticle).setColor(0.16F, 0.59F, 0.51F);
            return drippingSculkAcidParticle;
        }
    }

    @Environment(EnvType.CLIENT)
    private static class DripLandParticle extends DrippingSculkAcidParticle {
        DripLandParticle(ClientLevel clientLevel, double d, double e, double f, Fluid fluid, TextureAtlasSprite textureAtlasSprite) {
            super(clientLevel, d, e, f, fluid, textureAtlasSprite);
            this.lifetime = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        }
    }

    @Environment(EnvType.CLIENT)
    private static class FallingParticle extends DrippingSculkAcidParticle {
        FallingParticle(ClientLevel clientLevel, double d, double e, double f, Fluid fluid, TextureAtlasSprite textureAtlasSprite) {
            this(clientLevel, d, e, f, fluid, (int) (64.0 / (Math.random() * 0.8 + 0.2)), textureAtlasSprite);
        }

        FallingParticle(ClientLevel clientLevel, double d, double e, double f, Fluid fluid, int i, TextureAtlasSprite textureAtlasSprite) {
            super(clientLevel, d, e, f, fluid, textureAtlasSprite);
            this.lifetime = i;
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private static class FallAndLandParticle extends FallingParticle {
        protected final ParticleOptions landParticle;

        FallAndLandParticle(ClientLevel clientLevel, double d, double e, double f, Fluid fluid, ParticleOptions particleOptions, TextureAtlasSprite textureAtlasSprite) {
            super(clientLevel, d, e, f, fluid, textureAtlasSprite);
            this.landParticle = particleOptions;
        }

        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    private static class DripHangParticle extends DrippingSculkAcidParticle {
        private final ParticleOptions fallingParticle;

        DripHangParticle(ClientLevel clientLevel, double d, double e, double f,
                         Fluid fluid, ParticleOptions particleOptions, TextureAtlasSprite textureAtlasSprite) {
            super(clientLevel, d, e, f, fluid, textureAtlasSprite);
            this.fallingParticle = particleOptions;
            this.gravity *= 0.02F;
            this.lifetime = 40;
        }

        protected void preMoveUpdate() {
            if (this.lifetime-- <= 0) {
                this.remove();
                this.level.addParticle(ModParticleTypes.FALLING_SCULK_ACID, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }
        }

        protected void postMoveUpdate() {
            this.xd *= 0.02;
            this.yd *= 0.02;
            this.zd *= 0.02;
        }
    }
}
