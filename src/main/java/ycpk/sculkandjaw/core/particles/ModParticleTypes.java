package ycpk.sculkandjaw.core.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModParticleTypes {
    public static void registerModParticleTypes(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Particle Types for Mod " + SculkAndJaw.MOD_ID);
        MOD_PARTICLE_TYPES.register(modEventBus);
    }

    public static final DeferredRegister<ParticleType<?>> MOD_PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SculkAndJaw.MOD_ID);
    public static final RegistryObject<SimpleParticleType> SCULKOPHOBIA = MOD_PARTICLE_TYPES.register(
            "sculkophobia",
            () -> new SimpleParticleType(false)
    );
    public static final RegistryObject<SimpleParticleType> UMBRAFERN_SPORE = MOD_PARTICLE_TYPES.register(
            "umbrafern_spore",
            () -> new SimpleParticleType(false)
    );
    public static final RegistryObject<SimpleParticleType> DRIPPING_SCULK_ACID = MOD_PARTICLE_TYPES.register(
            "dripping_sculk_acid",
            () -> new SimpleParticleType(false)
    );
    public static final RegistryObject<SimpleParticleType> FALLING_SCULK_ACID = MOD_PARTICLE_TYPES.register(
            "falling_sculk_acid",
            () -> new SimpleParticleType(false)
    );
    public static final RegistryObject<SimpleParticleType> LANDING_SCULK_ACID = MOD_PARTICLE_TYPES.register(
            "landing_sculk_acid",
            () -> new SimpleParticleType(false)
    );
    public static final RegistryObject<SimpleParticleType> SCULK_ACID_BUBBLE_PARTICLE = MOD_PARTICLE_TYPES.register(
            "sculk_acid_bubble",
            () -> new SimpleParticleType(false)
    );
    public static final RegistryObject<SimpleParticleType> SCULK_ACID_BUBBLE_POP_PARTICLE = MOD_PARTICLE_TYPES.register(
            "sculk_acid_bubble_pop",
            () -> new SimpleParticleType(false)
    );
}
