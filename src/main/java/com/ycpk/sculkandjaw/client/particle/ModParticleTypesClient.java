package com.ycpk.sculkandjaw.client.particle;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.core.particles.ModParticleTypes;
import net.minecraft.client.particle.SpellParticle;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public class ModParticleTypesClient {
    @SubscribeEvent
    public static void registerModParticleTypesClient(RegisterParticleProvidersEvent event) {
        SculkAndJaw.LOGGER.info("Registering Client Particle Types for Mod " + SculkAndJaw.MOD_ID);
        event.registerSpriteSet(ModParticleTypes.SCULKOPHOBIA.get(), SpellParticle.Provider::new);
        event.registerSpriteSet(ModParticleTypes.UMBRAFERN_SPORE.get(), UmbrafernSporeParticle.Provider::new);
        event.registerSpriteSet(ModParticleTypes.DRIPPING_SCULK_ACID.get(), DrippingSculkAcidParticle.SculkAcidHangProvider::new);
        event.registerSpriteSet(ModParticleTypes.FALLING_SCULK_ACID.get(), DrippingSculkAcidParticle.SculkAcidFallProvider::new);
        event.registerSpriteSet(ModParticleTypes.LANDING_SCULK_ACID.get(), DrippingSculkAcidParticle.SculkAcidLandProvider::new);
        event.registerSpriteSet(ModParticleTypes.SCULK_ACID_BUBBLE_PARTICLE.get(), SculkAcidBubbleParticle.Provider::new);
        event.registerSpriteSet(ModParticleTypes.SCULK_ACID_BUBBLE_POP_PARTICLE.get(), SculkAcidBubblePopParticle.Provider::new);
    }
}
