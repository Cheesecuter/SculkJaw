package ycpk.sculkandjaw.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SpellParticle;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;

@Environment(EnvType.CLIENT)
public class ModParticleTypesClient {
    public static void registerModParticleTypesClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SCULKOPHOBIA, SpellParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.UMBRAFERN_SPORE, UmbrafernSporeParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.DRIPPING_SCULK_ACID, DrippingSculkAcidParticle.SculkAcidHangProvider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.FALLING_SCULK_ACID, DrippingSculkAcidParticle.SculkAcidFallProvider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.LANDING_SCULK_ACID, DrippingSculkAcidParticle.SculkAcidLandProvider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SCULK_ACID_BUBBLE_PARTICLE, SculkAcidBubbleParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SCULK_ACID_BUBBLE_POP_PARTICLE, SculkAcidBubblePopParticle.Provider::new);
    }
}
