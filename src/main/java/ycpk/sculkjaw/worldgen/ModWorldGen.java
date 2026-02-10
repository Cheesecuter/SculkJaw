package ycpk.sculkjaw.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.worldgen.features.ModFeatureUtils;
import ycpk.sculkjaw.worldgen.placements.ModPlacementUtils;


public class ModWorldGen {
    public static void registerModWorldGen(){
        Sculkjaw.LOGGER.info("Registering World Generations for Mod " + Sculkjaw.MOD_ID);

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.LAKES,
                ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "sculk_acid_lake"))
        );
    }

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModFeatureUtils::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacementUtils::bootstrap);
}
