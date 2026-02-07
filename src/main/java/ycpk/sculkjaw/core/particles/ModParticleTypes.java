package ycpk.sculkjaw.core.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkjaw.Sculkjaw;

public class ModParticleTypes {
    public static final SimpleParticleType SCULKOPHOBIA = register("sculkophobia", FabricParticleTypes.simple(false));
    public static final SimpleParticleType UMBRAFERN_SPORE = register("umbrafern_spore", FabricParticleTypes.simple(false));
    public static final SimpleParticleType DRIPPING_SCULK_ACID = register("dripping_sculk_acid", FabricParticleTypes.simple(false));
    public static final SimpleParticleType FALLING_SCULK_ACID = register("falling_sculk_acid", FabricParticleTypes.simple(false));
    public static final SimpleParticleType LANDING_SCULK_ACID = register("landing_sculk_acid", FabricParticleTypes.simple(false));

    public static void registerModParticleTypes() {
        Sculkjaw.LOGGER.info("Registering Particle Types for Mod " + Sculkjaw.MOD_ID);
    }

    private static SimpleParticleType register(String id, SimpleParticleType type) {
        ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, id);
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, identifier, type);
    }
}
