package ycpk.sculkandjaw.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AntacidResonanceEffect extends MobEffect {
    public AntacidResonanceEffect(MobEffectCategory mobEffectCategory, int i, ParticleOptions particleOptions) {
        super(mobEffectCategory, i, particleOptions);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int i) {
        return super.applyEffectTick(serverLevel, livingEntity, i);
    }
}
