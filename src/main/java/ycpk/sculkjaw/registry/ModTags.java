package ycpk.sculkjaw.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import ycpk.sculkjaw.Sculkjaw;

public class ModTags {
    public static final TagKey<EntityType<?>> IMMUNE_TO_SCULK_JAW =
            TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "immune_to_sculk_jaw"));

    public static void registerModTags(){
        Sculkjaw.LOGGER.info("Registering Tags for Mod " + Sculkjaw.MOD_ID);
    }

}
