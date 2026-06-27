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
    }
}
