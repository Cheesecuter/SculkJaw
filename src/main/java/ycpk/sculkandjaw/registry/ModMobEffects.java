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

public class ModMobEffects {
    public static void registerModEffects(){
        SculkAndJaw.LOGGER.info("Registering Effects for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final MobEffect SCULKOPHOBIA = register(11131, "sculkophobia",
            (new SculkophobiaEffect(MobEffectCategory.HARMFUL, 213328)).addAttributeModifier(Attributes.MAX_HEALTH, "3d9f7bdb-1257-47c8-8d2e-a3f45aaaf627", -2.0, AttributeModifier.Operation.ADDITION));
    public static final MobEffect ACID_ETCHING = register(11132, "acid_etching",
            (new AcidEtchingEffect(MobEffectCategory.HARMFUL, 213328)).addAttributeModifier(Attributes.ARMOR, "c7f0afec-8527-45aa-af71-97b25998be46", -2.0, AttributeModifier.Operation.ADDITION));

    private static MobEffect register(int i, String identifier, MobEffect mobEffect) {
        return (MobEffect) Registry.registerMapping(BuiltInRegistries.MOB_EFFECT, i, identifier, mobEffect);
    }
}
