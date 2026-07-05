package ycpk.sculkandjaw.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.monster.warden.Warden;
import ycpk.sculkandjaw.registry.ModDamageTypes;

public class AcidEtchingEffect extends MobEffect {
    public AcidEtchingEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void addAttributeModifiers(AttributeMap attributeMap, int i) {
        super.addAttributeModifiers(attributeMap, i);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity livingEntity, int i) {
        if (livingEntity instanceof Warden warden) {
            return false;
        }
        livingEntity.hurtServer(serverLevel, livingEntity.damageSources().source(ModDamageTypes.SCULK_ACID), 1.0F);
        if(livingEntity instanceof ServerPlayer serverPlayer) {

        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        return i % 20 == 0;
    }
}
