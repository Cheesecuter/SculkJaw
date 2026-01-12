package ycpk.sculkjaw.core.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ModSimpleParticleType extends ParticleType<ModSimpleParticleType> implements ParticleOptions {
    private final MapCodec<ModSimpleParticleType> codec = MapCodec.unit(this::getType);
    private final StreamCodec<RegistryFriendlyByteBuf, ModSimpleParticleType> streamCodec = StreamCodec.unit(this);

    protected ModSimpleParticleType(boolean bl) {
        super(bl);
    }

    public ModSimpleParticleType getType() {
        return this;
    }

    public MapCodec<ModSimpleParticleType> codec() {
        return this.codec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, ModSimpleParticleType> streamCodec() {
        return this.streamCodec;
    }
}
