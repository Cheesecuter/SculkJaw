package ycpk.sculkandjaw.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.tags.ModFluidTags;

public class ModTags {
    public static final TagKey<EntityType<?>> IMMUNE_TO_SCULK_JAW =
            TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "immune_to_sculk_jaw"));

    public static void registerModTags(){
        SculkAndJaw.LOGGER.info("Registering Tags for Mod " + SculkAndJaw.MOD_ID);
        ModFluidTags.registerModFluidTags();
    }

}
