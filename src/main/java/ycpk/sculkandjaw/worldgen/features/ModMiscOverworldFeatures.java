package ycpk.sculkandjaw.worldgen.features;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import ycpk.sculkandjaw.registry.ModBlocks;
import ycpk.sculkandjaw.registry.ModFeatures;

public class ModMiscOverworldFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_ACID_LAKE = ModFeatureUtils.createKey("sculk_acid_lake");

    public ModMiscOverworldFeatures() {

    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> bootstrapContext) {
        ModFeatureUtils.register(bootstrapContext, SCULK_ACID_LAKE, ModFeatures.SCULK_ACID_LAKE.get(),
                new SculkAcidLakeFeature.Configuration(BlockStateProvider.simple(ModBlocks.SCULK_ACID.get().defaultBlockState()), BlockStateProvider.simple(Blocks.SCULK.defaultBlockState())));
    }
}
