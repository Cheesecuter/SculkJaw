package com.ycpk.sculkandjaw.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import java.util.function.Supplier;

public class SculkophobiaEffect extends MobEffect {
    private final Supplier<SimpleParticleType> particleOptions;

    public SculkophobiaEffect(MobEffectCategory mobEffectCategory, int color, Supplier<SimpleParticleType> particleOptions) {
        super(mobEffectCategory, color, new SimpleParticleType(false));
        this.particleOptions = particleOptions;
    }

    @Override
    public ParticleOptions createParticleOptions(MobEffectInstance effect) {
        //return (ParticleOptions)this.particleFactory.apply(effect);
        return particleOptions.get();
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(attributeMap, amplifier);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        return super.applyEffectTick(livingEntity, amplifier);
    }
}
