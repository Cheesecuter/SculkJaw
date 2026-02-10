package ycpk.sculkjaw.worldgen.placements;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import ycpk.sculkjaw.Sculkjaw;

public class ModPlacementUtils {
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        ModMiscOverworldPlacements.bootstrap(context);
    }

    public static ResourceKey<PlacedFeature> createKey(String identifier) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, identifier));
    }
}
