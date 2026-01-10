package ycpk.sculkjaw.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import ycpk.sculkjaw.Sculkjaw;

public final class ModFluidTags {
    public static final TagKey<Fluid> SCULK_ACID = create("sculk_acid");

    public static void registerModFluidTags(){
        Sculkjaw.LOGGER.info("Registering Fluid Tags for Mod " + Sculkjaw.MOD_ID);
    }

    private static TagKey<Fluid> create(String identifier) {
        return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, identifier));
    }
}
