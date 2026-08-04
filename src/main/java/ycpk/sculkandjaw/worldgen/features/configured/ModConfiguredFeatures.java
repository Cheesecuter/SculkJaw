package ycpk.sculkandjaw.worldgen.features.configured;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModFeatures;
import ycpk.sculkandjaw.worldgen.features.SculkAcidLakeFeature;

import java.util.concurrent.CompletableFuture;

public class ModConfiguredFeatures extends FabricDynamicRegistryProvider {
    public static final ResourceKey<ConfiguredFeature<?, ?>> SCULK_ACID_LAKE = register("sculk_acid_lake");

    public static ResourceKey<ConfiguredFeature<?, ?>> register(String identifier) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(SculkAndJaw.MOD_ID, identifier));
    }

    public ModConfiguredFeatures(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider var1, Entries var2) {
        var2.addAll(var1.lookupOrThrow(Registries.CONFIGURED_FEATURE));
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> featureBootstrapContext) {
        featureBootstrapContext.register(
                SCULK_ACID_LAKE,
                new ConfiguredFeature<>(ModFeatures.SCULK_ACID_LAKE,
                        new SculkAcidLakeFeature.Configuration(BlockStateProvider.simple(Blocks.LAVA.defaultBlockState()), BlockStateProvider.simple(Blocks.STONE.defaultBlockState())))
        );
    }

    @Override
    public String getName() {
        return SculkAndJaw.MOD_ID + "_configured_features";
    }
}
