package ycpk.sculkjaw.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class SculkophobiaEffect extends MobEffect {
    public SculkophobiaEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int i) {
        super.addAttributeModifiers(attributeMap, i);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int i) {
        return super.applyEffectTick(serverLevel, livingEntity, i);
    }
}
