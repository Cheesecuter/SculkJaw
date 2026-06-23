package ycpk.sculkandjaw.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import ycpk.sculkandjaw.SculkAndJaw;

public final class ModFluidTags {
    public static void registerModFluidTags(){
        SculkAndJaw.LOGGER.info("Registering Fluid Tags for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final TagKey<Fluid> SCULK_ACID = create("sculk_acid");

    private static TagKey<Fluid> create(String identifier) {
        return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, identifier));
    }
}
