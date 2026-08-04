package ycpk.sculkandjaw.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class SculkAcidBubblePopParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    SculkAcidBubblePopParticle(ClientLevel clientLevel,
                               double d, double e, double f,
                               double g, double h, double i,
                               SpriteSet spriteSet) {
        super(clientLevel, d, e, f, g, h, i);
        this.friction = 0.96F;
        this.sprites = spriteSet;
        this.scale(1.3F);
        this.quadSize = 0.15F;
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private RandomSource random;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
            this.random = RandomSource.create();
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            SculkAcidBubblePopParticle sculkAcidBubblePopParticle = new SculkAcidBubblePopParticle(clientLevel, d, e, f, g, h, i, this.sprites);
            sculkAcidBubblePopParticle.setAlpha(1.0F);
            sculkAcidBubblePopParticle.setParticleSpeed(g, h, i);
            sculkAcidBubblePopParticle.setLifetime(this.random.nextInt(4) + 6);
            return sculkAcidBubblePopParticle;
        }
    }
}
