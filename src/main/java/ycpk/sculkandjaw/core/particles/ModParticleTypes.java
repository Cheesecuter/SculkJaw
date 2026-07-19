package ycpk.sculkandjaw.core.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModParticleTypes {
    public static void registerModParticleTypes() {
        SculkAndJaw.LOGGER.info("Registering Particle Types for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final SimpleParticleType SCULKOPHOBIA = register("sculkophobia", FabricParticleTypes.simple(false));
    public static final SimpleParticleType ANTACID_RESONANCE = register("antacid_resonance", FabricParticleTypes.simple(false));
    public static final SimpleParticleType UMBRAFERN_SPORE = register("umbrafern_spore", FabricParticleTypes.simple(false));
    public static final SimpleParticleType DRIPPING_SCULK_ACID = register("dripping_sculk_acid", FabricParticleTypes.simple(false));
    public static final SimpleParticleType FALLING_SCULK_ACID = register("falling_sculk_acid", FabricParticleTypes.simple(false));
    public static final SimpleParticleType LANDING_SCULK_ACID = register("landing_sculk_acid", FabricParticleTypes.simple(false));
    public static final SimpleParticleType SCULK_ACID_BUBBLE_PARTICLE = register("sculk_acid_bubble", FabricParticleTypes.simple(false));
    public static final SimpleParticleType SCULK_ACID_BUBBLE_POP_PARTICLE = register("sculk_acid_bubble_pop", FabricParticleTypes.simple(false));

    private static SimpleParticleType register(String id, SimpleParticleType type) {
        Identifier identifier = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, id);
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, identifier, type);
    }
}
