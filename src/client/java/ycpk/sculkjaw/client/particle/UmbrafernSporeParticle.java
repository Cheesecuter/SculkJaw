package ycpk.sculkjaw.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class UmbrafernSporeParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    UmbrafernSporeParticle(ClientLevel clientLevel, double d, double e, double f, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, spriteSet.first());
        this.xd *= 0.3;
        this.yd *= 0.1;
        this.zd *= 0.3;
        this.gravity = 0.01F;
        this.quadSize *= Math.abs(this.random.nextFloat() - 0.5F) * 1.2F + 0.7F;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8));
        this.sprites = spriteSet;
        //this.setFadeColor(15916745);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public int getLightColor(float f) {
        int i = super.getLightColor(f);
        int k = i >> 16 & 255;
        return 240 | k << 16;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.yd += 0.01F;
            this.quadSize *= 0.95F;
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, RandomSource randomSource) {
            return new UmbrafernSporeParticle(clientLevel, d, e, f, this.sprites);
        }
    }
}
