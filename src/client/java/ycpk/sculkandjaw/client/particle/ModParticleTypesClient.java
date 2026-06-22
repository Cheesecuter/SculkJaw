package ycpk.sculkandjaw.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SpellParticle;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;

@Environment(EnvType.CLIENT)
public class ModParticleTypesClient {
    public static void registerModParticleTypesClient() {
        SculkAndJaw.LOGGER.info("Registering Client Particle Types for Mod " + SculkAndJaw.MOD_ID);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SCULKOPHOBIA, SpellParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.UMBRAFERN_SPORE, UmbrafernSporeParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SCULK_ACID_BUBBLE_PARTICLE, SculkAcidBubbleParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SCULK_ACID_BUBBLE_POP_PARTICLE, SculkAcidBubblePopParticle.Provider::new);
    }
}
