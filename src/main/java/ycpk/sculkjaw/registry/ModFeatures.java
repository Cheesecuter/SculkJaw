package ycpk.sculkjaw.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.worldgen.features.SculkAcidLakeFeature;

public abstract class ModFeatures<FC extends FeatureConfiguration>{
    public static void registerModFeatures(){
        Sculkjaw.LOGGER.info("Registering Features for Mod " + Sculkjaw.MOD_ID);
    }

    public static final Feature<SculkAcidLakeFeature.Configuration> SCULK_ACID_LAKE = register("sculk_acid_lake", new SculkAcidLakeFeature(SculkAcidLakeFeature.Configuration.CODEC));

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String identifier, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, identifier), feature);
    }
}
