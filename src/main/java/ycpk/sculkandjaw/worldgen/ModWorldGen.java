package ycpk.sculkandjaw.worldgen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.worldgen.features.configured.ModConfiguredFeatures;
import ycpk.sculkandjaw.worldgen.placements.ModPlacementUtils;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModWorldGen extends DatapackBuiltinEntriesProvider {
    public static void registerModWorldGen(){
        SculkAndJaw.LOGGER.info("Registering World Generations for Mod " + SculkAndJaw.MOD_ID);
    }

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacementUtils::bootstrap);

    public ModWorldGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, Set<String> modIds) {
        super(output, registries, BUILDER, modIds);
    }
}
