package com.ycpk.sculkandjaw.core.particles;

import com.ycpk.sculkandjaw.SculkAndJaw;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticleTypes {
    public static void registerModParticleTypes(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Particle Types for Mod " + SculkAndJaw.MOD_ID);
        MOD_PARTICLE_TYPES.register(modEventBus);
    }

    public static final DeferredRegister<ParticleType<?>> MOD_PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, SculkAndJaw.MOD_ID);
    public static final Supplier<SimpleParticleType> SCULKOPHOBIA = MOD_PARTICLE_TYPES.register(
            "sculkophobia", () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> UMBRAFERN_SPORE = MOD_PARTICLE_TYPES.register(
            "umbrafern_spore", () -> new SimpleParticleType(false)
    );
}
