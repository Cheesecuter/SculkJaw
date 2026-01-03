package ycpk.sculkjaw.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import ycpk.sculkjaw.Sculkjaw;

public interface ModEnchantmentTags {
    TagKey<Enchantment> COMBINED_SCULK_JAW_DROPPING = create("combined_sculk_jaw_dropping");

    private static TagKey<Enchantment> create(String path) {
        return TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, path));
    }
}
