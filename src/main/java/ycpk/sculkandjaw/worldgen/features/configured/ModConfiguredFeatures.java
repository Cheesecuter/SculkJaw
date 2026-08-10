package ycpk.sculkandjaw.worldgen.features.configured;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModBlocks;
import ycpk.sculkandjaw.registry.ModFeatures;
import ycpk.sculkandjaw.worldgen.features.ModFeatureUtils;
import ycpk.sculkandjaw.worldgen.features.SculkAcidLakeFeature;

public class ModConfiguredFeatures {
    public static void registerModConfiguredFeatures() {
        SculkAndJaw.LOGGER.info("Registering Configured Features for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_ACID_LAKE = register("sculk_acid_lake");

    public static ResourceKey<ConfiguredFeature<?, ?>> register(String identifier) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, identifier));
    }

    public ModConfiguredFeatures() {

    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> featureBootstrapContext) {
        ModFeatureUtils.register(
                featureBootstrapContext,
                SCULK_ACID_LAKE,
                ModFeatures.SCULK_ACID_LAKE.get(),
                new SculkAcidLakeFeature.Configuration(
                        BlockStateProvider.simple(ModBlocks.SCULK_ACID.get().defaultBlockState()),
                        BlockStateProvider.simple(Blocks.SCULK.defaultBlockState())
                )
        );
    }
}