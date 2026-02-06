package ycpk.sculkjaw.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SpellParticle;
import ycpk.sculkjaw.core.particles.ModParticleTypes;

@Environment(EnvType.CLIENT)
public class ModParticleTypesClient {
    public static void registerModParticleTypesClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SCULKOPHOBIA, SpellParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.UMBRAFERN_SPORE, UmbrafernSporeParticle.Provider::new);
    }
}
