package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.core.particles.ModParticleTypes;
import com.ycpk.sculkandjaw.effects.AcidEtchingEffect;
import com.ycpk.sculkandjaw.effects.SculkophobiaEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {
    public static void registerModMobEffects(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Mob Effects for Mod " + SculkAndJaw.MOD_ID);
        MOD_MOB_EFFECTS.register(modEventBus);
    }

    public static final DeferredRegister<MobEffect> MOD_MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, SculkAndJaw.MOD_ID);
    public static final Holder<MobEffect> SCULKOPHOBIA = MOD_MOB_EFFECTS.register("sculkophobia",
            () -> new SculkophobiaEffect(MobEffectCategory.HARMFUL, 213328, ModParticleTypes.SCULKOPHOBIA)
                    .addAttributeModifier(
                            Attributes.MAX_HEALTH,
                            ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "effect.sculkophobia"),
                            -2.0,
                            AttributeModifier.Operation.ADD_VALUE)
    );
    public static final Holder<MobEffect> ACID_ETCHING = MOD_MOB_EFFECTS.register("acid_etching",
            () -> new AcidEtchingEffect(MobEffectCategory.HARMFUL, 213328)
                    .addAttributeModifier(
                            Attributes.ARMOR,
                            ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "effect.acid_etching"),
                            -2.0,
                            AttributeModifier.Operation.ADD_VALUE
                    )
    );
}
