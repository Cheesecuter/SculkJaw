package ycpk.sculkandjaw.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;
import ycpk.sculkandjaw.effects.AcidEtchingEffect;
import ycpk.sculkandjaw.effects.AntacidResonanceEffect;
import ycpk.sculkandjaw.effects.SculkophobiaEffect;

public class ModMobEffects {
    public static void registerModEffects(){
        SculkAndJaw.LOGGER.info("Registering Effects for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final Holder<MobEffect> SCULKOPHOBIA_EFFECT = register(
            "sculkophobia",
            (new SculkophobiaEffect(MobEffectCategory.HARMFUL, 213328, ModParticleTypes.SCULKOPHOBIA))
                    .addAttributeModifier(
                            Attributes.MAX_HEALTH,
                            Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "effect.sculkophobia"),
                            -2.0,
                            AttributeModifier.Operation.ADD_VALUE
                    )
    );
    public static final Holder<MobEffect> ACID_ETCHING_EFFECT = register(
            "acid_etching",
            (new AcidEtchingEffect(MobEffectCategory.HARMFUL, 213328))
                    .addAttributeModifier(
                            Attributes.ARMOR,
                            Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID,"effect.acid_etching"),
                            -2.0,
                            AttributeModifier.Operation.ADD_VALUE
                    )
    );
    public static final Holder<MobEffect> ANTACID_RESONANCE_EFFECT = register(
            "antacid_resonance",
            (new AntacidResonanceEffect(MobEffectCategory.BENEFICIAL, 213328, ModParticleTypes.SCULKOPHOBIA))
    );

    private static Holder<MobEffect> register(String identifier, MobEffect mobEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID ,identifier), mobEffect);
    }
}
