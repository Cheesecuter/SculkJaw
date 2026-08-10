package ycpk.sculkandjaw.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class UmbraFernSporeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final RandomSource random;

    UmbraFernSporeParticle(ClientLevel clientLevel, double d, double e, double f, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, 0.0, 0.0, 0.0);
        this.random = RandomSource.create();
        this.xd *= 0.6;
        this.yd *= 0.3;
        this.zd *= 0.6;
        this.gravity = 0.01F;
        this.quadSize *= Math.abs(this.random.nextFloat() - 0.5F) * 1.2F + 0.7F;
        this.lifetime = 30 + this.random.nextInt(12);
        this.sprites = spriteSet;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
            this.xd += (this.random.nextFloat() - 0.5) * 0.05;
            this.zd += (this.random.nextFloat() - 0.5) * 0.05;
            this.quadSize *= 0.95F;
            this.setSpriteFromAge(this.sprites);
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {this.sprites = spriteSet;}

        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            return new UmbraFernSporeParticle(clientLevel, d, e, f, this.sprites);
        }
    }
}
