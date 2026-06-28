package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.tags.ModFluidTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ModTags {
    public static void registerModTags(){
        SculkAndJaw.LOGGER.info("Registering Tags for Mod " + SculkAndJaw.MOD_ID);
        ModFluidTags.registerModFluidTags();
    }

    public static final TagKey<EntityType<?>> IMMUNE_TO_SCULK_JAW =
            TagKey.create(
                    Registries.ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "immune_to_sculk_jaw")
            );
}
