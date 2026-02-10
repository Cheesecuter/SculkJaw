package ycpk.sculkjaw.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SculkAcidBubblePopParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    SculkAcidBubblePopParticle(ClientLevel clientLevel,
                               double d, double e, double f,
                               double g, double h, double i,
                               SpriteSet spriteSet) {
        super(clientLevel, d, e, f, g, h, i, spriteSet.first());
        this.friction = 0.96F;
        this.sprites = spriteSet;
        this.scale(1.3F);
        this.quadSize = 0.15F;
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                       double d, double e, double f,
                                       double g, double h, double i,
                                       RandomSource randomSource) {
            SculkAcidBubblePopParticle sculkAcidBubblePopParticle = new SculkAcidBubblePopParticle(clientLevel, d, e, f, g, h, i, this.sprites);
            sculkAcidBubblePopParticle.setAlpha(1.0F);
            sculkAcidBubblePopParticle.setParticleSpeed(g, h, i);
            sculkAcidBubblePopParticle.setLifetime(randomSource.nextInt(4) + 6);
            return sculkAcidBubblePopParticle;
        }

        public SpriteSet sprite() {
            return this.sprites;
        }
    }
}
