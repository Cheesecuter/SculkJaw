package ycpk.sculkjaw.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.worldgen.features.SculkJawPatchFeature;

import java.util.List;

public abstract class ModFeatures<FC extends FeatureConfiguration>{
    public static void registerModFeatures(){
        Sculkjaw.LOGGER.info("Registering Features for Mod " + Sculkjaw.MOD_ID);
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.DEEP_DARK),
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "sculk_jaw_patch_placed"))
        );
    }

    public static final Feature<SculkPatchConfiguration> SCULK_JAW_PATCH_FEATURE = register("sculk_jaw_patch", new SculkJawPatchFeature(SculkPatchConfiguration.CODEC));
    public static final ConfiguredFeature<SculkPatchConfiguration, ?> SCULK_JAW_PATCH_FEATURE_CONFIGURED =
            new ConfiguredFeature<>(SCULK_JAW_PATCH_FEATURE,
                    new SculkPatchConfiguration(
                            10, 32, 64, 0, 1, UniformInt.of(1, 3), 0.5F));
    public static PlacedFeature SCULK_JAW_FEATURE_PLACED = new PlacedFeature(
            Holder.direct(SCULK_JAW_PATCH_FEATURE_CONFIGURED), List.of(BiomeFilter.biome())
    );

    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(String identifier, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, identifier), feature);
    }
}
