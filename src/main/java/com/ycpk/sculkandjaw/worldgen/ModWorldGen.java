package com.ycpk.sculkandjaw.worldgen;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.worldgen.features.configured.ModConfiguredFeatures;
import com.ycpk.sculkandjaw.worldgen.placements.ModPlacementUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGen extends DatapackBuiltinEntriesProvider {
    public static void registerModWorldGen() {
        SculkAndJaw.LOGGER.info("Registering World Generations for Mod " + SculkAndJaw.MOD_ID);
    }

    public ModWorldGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(SculkAndJaw.MOD_ID));
    }

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacementUtils::bootstrap);
}
