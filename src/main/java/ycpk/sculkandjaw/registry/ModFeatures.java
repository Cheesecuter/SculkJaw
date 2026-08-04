package ycpk.sculkandjaw.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.worldgen.features.SculkAcidLakeFeature;

public class ModFeatures {
    public static void registerModFeatures(){
        SculkAndJaw.LOGGER.info("Registering Features for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final Feature<SculkAcidLakeFeature.Configuration> SCULK_ACID_LAKE = register("sculk_acid_lake", new SculkAcidLakeFeature(SculkAcidLakeFeature.Configuration.CODEC));

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String identifier, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(SculkAndJaw.MOD_ID, identifier), feature);
    }
}
