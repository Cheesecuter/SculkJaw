package com.ycpk.sculkandjaw.worldgen.features;

import com.ycpk.sculkandjaw.registry.ModBlocks;
import com.ycpk.sculkandjaw.registry.ModFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class ModMiscOverworldFeatures {
    public ModMiscOverworldFeatures() {

    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_ACID_LAKE = ModFeatureUtils.createKey("sculk_acid_lake");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> bootstrapContext) {
        ModFeatureUtils.register(bootstrapContext, SCULK_ACID_LAKE, ModFeatures.SCULK_ACID_LAKE.get(),
                new SculkAcidLakeFeature.Configuration(BlockStateProvider.simple(ModBlocks.SCULK_ACID.get().defaultBlockState()), BlockStateProvider.simple(Blocks.SCULK.defaultBlockState())));
    }
}
