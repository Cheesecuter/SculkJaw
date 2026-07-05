package ycpk.sculkandjaw.worldgen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.worldgen.features.ModFeatureUtils;
import ycpk.sculkandjaw.worldgen.placements.ModPlacementUtils;

public class ModWorldGen {
    public static void registerModWorldGen(){
        SculkAndJaw.LOGGER.info("Registering World Generations for Mod " + SculkAndJaw.MOD_ID);

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Decoration.LAKES,
                ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid_lake"))
        );
    }

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModFeatureUtils::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacementUtils::bootstrap);
}
