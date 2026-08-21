package ycpk.sculkandjaw.world.item.component;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.RemoveStatusEffectsConsumeEffect;
import ycpk.sculkandjaw.registry.ModMobEffects;

public class ModConsumables {
    public ModConsumables() {
    }

    public static final Consumable ANTACID_DROPLET = Consumable.builder()
            .consumeSeconds(1.6f)
            .animation(ItemUseAnimation.DRINK)
            .sound(SoundEvents.GENERIC_DRINK)
            .hasConsumeParticles(false)
            .onConsume(new RemoveStatusEffectsConsumeEffect(ModMobEffects.ACID_ETCHING_EFFECT))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ModMobEffects.ACID_RESISTANCE_EFFECT, 400, 1)))
            .build();
}
