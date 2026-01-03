package ycpk.sculkjaw.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.effects.SculkophobiaEffect;

public class ModEffects {
    public static void registerModEffects(){
        Sculkjaw.LOGGER.info("Registering Effects for Mod " + Sculkjaw.MOD_ID);
    }

    public static final Holder<MobEffect> SCULKOPHOBIA_EFFECT = register("sculkophobia",
            (new SculkophobiaEffect(MobEffectCategory.HARMFUL, 213328)).addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.withDefaultNamespace("effect.sculkophobia"), -2.0, AttributeModifier.Operation.ADD_VALUE));

    private static Holder<MobEffect> register(String identifier, MobEffect mobEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID ,identifier), mobEffect);
    }
}
