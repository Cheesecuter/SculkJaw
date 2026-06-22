package ycpk.sculkandjaw.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;

public class SculkAcidBubbleParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final RandomSource random;

    SculkAcidBubbleParticle(ClientLevel clientLevel,
                            double d, double e, double f,
                            double g, double h, double i,
                            SpriteSet spriteSet) {
        super(clientLevel, d, e, f, g, h, i);
        this.random = RandomSource.create();
        this.sprites = spriteSet;
        this.scale(1.3F);
        this.quadSize = 0.15F;
        this.lifetime = this.random.nextInt(50) + 80;
        this.gravity = 3.0E-6F;
        this.xd = g;
        this.yd = h + (double)(this.random.nextFloat() / 20.0F);
        this.zd = i;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        //super.tick();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime) {
            this.xd += (double)(this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1));
            this.zd += (double)(this.random.nextFloat() / 5000.0F * (float)(this.random.nextBoolean() ? 1 : -1));
            this.yd -= (double)this.gravity;
            this.gravity += 0.0001F;
            this.move(this.xd, this.yd, this.zd);
            if (this.onGround) {
                this.remove();
                this.level.addParticle(ModParticleTypes.SCULK_ACID_BUBBLE_POP_PARTICLE, this.x, this.y, this.z, 0.0, 0.0, 0.0);
            }

        } else {
            this.remove();
            this.level.addParticle(ModParticleTypes.SCULK_ACID_BUBBLE_POP_PARTICLE, this.x, this.y, this.z, 0.0, 0.0, 0.0);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {this.sprites = spriteSet;}

        @Override
        public Particle createParticle(SimpleParticleType particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            return new SculkAcidBubbleParticle(clientLevel, d, e, f, g, h, i, this.sprites);
        }
    }
}
