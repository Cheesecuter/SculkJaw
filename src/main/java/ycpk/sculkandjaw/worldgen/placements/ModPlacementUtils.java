package ycpk.sculkandjaw.worldgen.placements;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModPlacementUtils {
    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        ModMiscOverworldPlacements.bootstrap(context);
    }

    public static ResourceKey<PlacedFeature> createKey(String identifier) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(SculkAndJaw.MOD_ID, identifier));
    }
}
