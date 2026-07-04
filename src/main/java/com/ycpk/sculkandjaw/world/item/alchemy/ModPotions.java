package com.ycpk.sculkandjaw.world.item.alchemy;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.registry.ModMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {
    public static void registerModPotions(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Potions for Mod " + SculkAndJaw.MOD_ID);
        MOD_POTIONS.register(modEventBus);
    }

    public static final DeferredRegister<Potion> MOD_POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, SculkAndJaw.MOD_ID);

    public static final Holder<Potion> ACID_ETCHING = MOD_POTIONS.register(
            "acid_etching",
            () -> new Potion(
                    "acid_etching",
                    new MobEffectInstance[]{new MobEffectInstance(ModMobEffects.ACID_ETCHING, 3600)}
            )
    );
    public static final Holder<Potion> LONG_ACID_ETCHING = MOD_POTIONS.register(
            "long_acid_etching",
            () -> new Potion(
                    "acid_etching",
                    new MobEffectInstance[]{new MobEffectInstance(ModMobEffects.ACID_ETCHING, 9600)}
            )
    );
    public static final Holder<Potion> STRONG_ACID_ETCHING = MOD_POTIONS.register(
            "strong_acid_etching",
            () -> new Potion(
                    "acid_etching",
                    new MobEffectInstance[]{new MobEffectInstance(ModMobEffects.ACID_ETCHING, 1800, 1)}
            )
    );
}
