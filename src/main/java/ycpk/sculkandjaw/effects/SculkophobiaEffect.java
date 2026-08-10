package ycpk.sculkandjaw.effects;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SculkophobiaEffect extends MobEffect {
    public SculkophobiaEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        float currentHealth = livingEntity.getHealth();
        float maxHealth = livingEntity.getMaxHealth();
        if (currentHealth > maxHealth) {
            livingEntity.setHealth(Math.max(maxHealth, Mth.abs(currentHealth - (i + 1) * 2)));
        }
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return true;
    }
}
