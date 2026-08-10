package ycpk.sculkandjaw.registry;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.worldgen.features.SculkAcidLakeFeature;

public class ModFeatures {
    public static void registerModFeatures(IEventBus modEventBus){
        SculkAndJaw.LOGGER.info("Registering Features for Mod " + SculkAndJaw.MOD_ID);
        MOD_FEATURES.register(modEventBus);
    }

    public static final DeferredRegister<Feature<?>> MOD_FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, SculkAndJaw.MOD_ID);
    public static final RegistryObject<Feature<SculkAcidLakeFeature.Configuration>> SCULK_ACID_LAKE = MOD_FEATURES.register(
            "sculk_acid_lake",
            () -> new SculkAcidLakeFeature(SculkAcidLakeFeature.Configuration.CODEC)
    );
}
