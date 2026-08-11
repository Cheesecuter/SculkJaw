package ycpk.sculkandjaw.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import ycpk.sculkandjaw.SculkAndJaw;

public class AcidEtchingEffect extends MobEffect {
    public AcidEtchingEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        if (livingEntity instanceof Warden warden) {
            return;
        }
        livingEntity.hurt(SculkAndJaw.getDamageSources(livingEntity.level()).sculkAcid(), 1.0F);
        if(livingEntity instanceof ServerPlayer serverPlayer) {

        }
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return i % 20 == 0;
    }
}
