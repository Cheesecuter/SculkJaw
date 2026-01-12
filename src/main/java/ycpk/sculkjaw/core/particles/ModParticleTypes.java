package ycpk.sculkjaw.core.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkjaw.Sculkjaw;

import java.util.function.Function;

public class ModParticleTypes {
    public static void registerModParticleTypes(){
        Sculkjaw.LOGGER.info("Registering Particle Types for Mod " + Sculkjaw.MOD_ID);
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "sculkophobia"), SCULKOPHOBIA);
    }
    public static final Codec<ParticleOptions> CODEC = BuiltInRegistries.PARTICLE_TYPE.byNameCodec().dispatch("type", ParticleOptions::getType, ParticleType::codec);
    public static final StreamCodec<RegistryFriendlyByteBuf, ParticleOptions> STREAM_CODEC = ByteBufCodecs.registry(Registries.PARTICLE_TYPE).dispatch(ParticleOptions::getType, ParticleType::streamCodec);

    //public static final ModSimpleParticleType SCULKOPHOBIA = register("sculkophobia", false);
    public static final SimpleParticleType SCULKOPHOBIA = FabricParticleTypes.simple();

    public ModParticleTypes() {

    }

    private static ModSimpleParticleType register(String identifier, boolean bl) {
        return (ModSimpleParticleType) Registry.register(BuiltInRegistries.PARTICLE_TYPE, identifier, new ModSimpleParticleType(bl));
    }

    private static <T extends ParticleOptions> ParticleType<T> register(String identifier, boolean bl, final Function<ParticleType<T>, MapCodec<T>> function, final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> function2) {
        return (ParticleType) Registry.register(BuiltInRegistries.PARTICLE_TYPE, identifier, new ParticleType<T>(bl) {
            public MapCodec<T> codec() {return (MapCodec) function.apply(this);}
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return (StreamCodec) function2.apply(this);
            }
        });
    }
}
