package ycpk.sculkandjaw.client.particle;

import net.minecraft.client.particle.SpellParticle;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;

public class ModParticleTypesClient {
    @SubscribeEvent
    public static void registerModParticleTypesClient(RegisterParticleProvidersEvent event) {
        SculkAndJaw.LOGGER.info("Registering Client Particle Types for Mod " + SculkAndJaw.MOD_ID);
        event.registerSpriteSet(ModParticleTypes.SCULKOPHOBIA.get(), SpellParticle.Provider::new);
        event.registerSpriteSet(ModParticleTypes.UMBRAFERN_SPORE.get(), UmbraFernSporeParticle.Provider::new);
        event.registerSpriteSet(ModParticleTypes.DRIPPING_SCULK_ACID.get(), DrippingSculkAcidParticle.SculkAcidHangProvider::new);
        event.registerSpriteSet(ModParticleTypes.FALLING_SCULK_ACID.get(), DrippingSculkAcidParticle.SculkAcidFallProvider::new);
        event.registerSpriteSet(ModParticleTypes.LANDING_SCULK_ACID.get(), DrippingSculkAcidParticle.SculkAcidLandProvider::new);
        event.registerSpriteSet(ModParticleTypes.SCULK_ACID_BUBBLE_PARTICLE.get(), SculkAcidBubbleParticle.Provider::new);
        event.registerSpriteSet(ModParticleTypes.SCULK_ACID_BUBBLE_POP_PARTICLE.get(), SculkAcidBubblePopParticle.Provider::new);
    }
}
