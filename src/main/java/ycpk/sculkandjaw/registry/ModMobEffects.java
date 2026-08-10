package ycpk.sculkandjaw.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.effects.AcidEtchingEffect;
import ycpk.sculkandjaw.effects.SculkophobiaEffect;

public class ModMobEffects {
    public static void registerModEffects(IEventBus modEventBus){
        SculkAndJaw.LOGGER.info("Registering Effects for Mod " + SculkAndJaw.MOD_ID);
        MOD_MOB_EFFECTS.register(modEventBus);
    }

    public static final DeferredRegister<MobEffect> MOD_MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, SculkAndJaw.MOD_ID);
    public static final RegistryObject<MobEffect> SCULKOPHOBIA = MOD_MOB_EFFECTS.register(
            "sculkophobia",
            () -> new SculkophobiaEffect(MobEffectCategory.HARMFUL, 213328)
                    .addAttributeModifier(Attributes.MAX_HEALTH,
                            "3d9f7bdb-1257-47c8-8d2e-a3f45aaaf627",
                            -2.0,
                            AttributeModifier.Operation.ADDITION
                    )
    );
    public static final RegistryObject<MobEffect> ACID_ETCHING = MOD_MOB_EFFECTS.register(
            "acid_etching",
            () -> new AcidEtchingEffect(MobEffectCategory.HARMFUL, 213328)
                    .addAttributeModifier(Attributes.ARMOR,
                            "c7f0afec-8527-45aa-af71-97b25998be46",
                            -2.0,
                            AttributeModifier.Operation.ADDITION
                    )
    );
}
