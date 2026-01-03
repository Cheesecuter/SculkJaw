package ycpk.sculkjaw.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkjaw.Sculkjaw;

public class ModParticles {
    public static final SimpleParticleType SCULK_JAW_BITE = FabricParticleTypes.simple();

    public static void registerModParticles() {
        Sculkjaw.LOGGER.info("Registering Particles for Mod " + Sculkjaw.MOD_ID);
        register("sculk_jaw_bite", SCULK_JAW_BITE);
    }

    private static SimpleParticleType register(String id, SimpleParticleType type) {
        ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, id);
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, identifier, type);
    }
}
