package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.worldgen.features.SculkAcidLakeFeature;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static void registerModFeatures(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Features for Mod " + SculkAndJaw.MOD_ID);
        MOD_FEATURES.register(modEventBus);
    }

    public static final DeferredRegister<Feature<?>> MOD_FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, SculkAndJaw.MOD_ID);

    public static final DeferredHolder<Feature<?>, SculkAcidLakeFeature> SCULK_ACID_LAKE = MOD_FEATURES.register(
            "sculk_acid_lake",
            () -> new SculkAcidLakeFeature(
                    SculkAcidLakeFeature.Configuration.CODEC
            )
    );
}
