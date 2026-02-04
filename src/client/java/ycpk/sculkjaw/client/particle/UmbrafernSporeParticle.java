package ycpk.sculkjaw.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class UmbrafernSporeParticle extends SingleQuadParticle {
    UmbrafernSporeParticle(ClientLevel clientLevel, double d, double e, double f, TextureAtlasSprite textureAtlasSprite) {
        super(clientLevel, d, e, f, 0.0, 0.0, 0.0, textureAtlasSprite);
        //this.gravity = 0.75F;
        this.gravity = 0.01F;
        this.friction = 0.999F;
        this.xd *= 0.3;
        this.yd *= 0.03;
        this.zd *= 0.3;
        //this.yd = (double)(this.random.nextFloat() * 0.4F + 0.05F);
        this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
    }

    public SingleQuadParticle.Layer getLayer() {
        return Layer.OPAQUE;
    }

    public int getLightColor(float f) {
        int i = super.getLightColor(f);
        int k = i >> 16 & 255;
        //return 240 | k << 16;
        return 256;
    }

    public float getQuadSize(float f) {
        float g = ((float)this.age + f) / (float)this.lifetime;
        return this.quadSize * (1.0F - g * g);
    }

    public void tick() {
        super.tick();
        if (!this.removed) {
            float f = (float)this.age / (float)this.lifetime;
            this.yd += 0.01;
            if (this.random.nextFloat() > f) {
                this.level.addParticle(ParticleTypes.SCULK_CHARGE_POP, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                       double d, double e, double f, double g, double h, double i, RandomSource randomSource) {
            UmbrafernSporeParticle umbrafernSporeParticle = new UmbrafernSporeParticle(clientLevel, d, e, f, this.sprite.get(randomSource));
            return umbrafernSporeParticle;
        }
    }
}
