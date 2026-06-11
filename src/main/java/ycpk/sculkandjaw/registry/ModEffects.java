package ycpk.sculkandjaw.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;
import ycpk.sculkandjaw.effects.AcidEtchingEffect;
import ycpk.sculkandjaw.effects.SculkophobiaEffect;

public class ModEffects {
    public static void registerModEffects(){
        SculkAndJaw.LOGGER.info("Registering Effects for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final Holder<MobEffect> SCULKOPHOBIA_EFFECT = register("sculkophobia",
            (new SculkophobiaEffect(MobEffectCategory.HARMFUL, 213328, ModParticleTypes.SCULKOPHOBIA)).addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "effect.sculkophobia"), -2.0, AttributeModifier.Operation.ADD_VALUE));
    public static final Holder<MobEffect> ACID_ETCHING = register("acid_etching",
            (new AcidEtchingEffect(MobEffectCategory.HARMFUL, 213328)).addAttributeModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID,"effect.acid_etching"), -2.0, AttributeModifier.Operation.ADD_VALUE));

    private static Holder<MobEffect> register(String identifier, MobEffect mobEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID ,identifier), mobEffect);
    }
}
