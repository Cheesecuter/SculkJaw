package ycpk.sculkjaw.level.storage.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import ycpk.sculkjaw.Sculkjaw;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ModBuiltInLootTables {
    private static final Set<ResourceKey<LootTable>> LOCATIONS = new HashSet<>();
    private static final Set<ResourceKey<LootTable>> IMMUTABLE_LOCATIONS;
    public static final ResourceKey<LootTable> SCULK_JAW_COMBINATION;
    public static final ResourceKey<LootTable> CONCENTRATED_SCULK_COMBINATION;

    public static void registerModBuiltInLootTables() {
        Sculkjaw.LOGGER.info("Registering Built In Loot Tables for Mod " + Sculkjaw.MOD_ID);
    }

    private static ResourceKey<LootTable> register(String identifier) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, identifier)));
    }

    private static ResourceKey<LootTable> register(ResourceKey<LootTable> resourceKey) {
        if(LOCATIONS.add(resourceKey)) {
            return resourceKey;
        }
        else {
            throw new IllegalArgumentException(String.valueOf(resourceKey.location()) + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceKey<LootTable>> all() {return IMMUTABLE_LOCATIONS;}

    static {
        IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);
        SCULK_JAW_COMBINATION = register("block_combination/sculk_jaw");
        CONCENTRATED_SCULK_COMBINATION = register("block_combination/concentrated_sculk");
    }
}
