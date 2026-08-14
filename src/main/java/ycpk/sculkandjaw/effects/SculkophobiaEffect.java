package ycpk.sculkandjaw.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class SculkophobiaEffect extends MobEffect {
    public SculkophobiaEffect(MobEffectCategory mobEffectCategory, int i, ParticleOptions particleOptions) {
        super(mobEffectCategory, i, particleOptions);
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int i) {
        super.addAttributeModifiers(attributeMap, i);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int i) {
        float currentHealth = livingEntity.getHealth();
        float maxHealth = livingEntity.getMaxHealth();
        if (currentHealth > maxHealth) {
            livingEntity.setHealth(Mth.absMax(maxHealth, Mth.abs(currentHealth - (i + 1) * 2)));
        }
        return super.applyEffectTick(serverLevel, livingEntity, i);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        return true;
    }
}
