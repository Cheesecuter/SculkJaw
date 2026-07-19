package ycpk.sculkandjaw.level.storage.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import ycpk.sculkandjaw.SculkAndJaw;

import java.util.HashSet;
import java.util.Set;

public class ModBuiltInLootTables {
    public static void registerModBuiltInLootTables() {
        SculkAndJaw.LOGGER.info("Registering Built In Loot Tables for Mod " + SculkAndJaw.MOD_ID);
    }

    private static final Set<ResourceKey<LootTable>> LOCATIONS = new HashSet<>();
    public static final ResourceKey<LootTable> HARVEST_ACIDCOIL_CATTAIL = register("harvest/acidcoil_cattail");

    private static ResourceKey<LootTable> register(String identifier) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, identifier)));
    }

    private static ResourceKey<LootTable> register(ResourceKey<LootTable> resourceKey) {
        if(LOCATIONS.add(resourceKey)) {
            return resourceKey;
        }
        else {
            throw new IllegalArgumentException(String.valueOf(resourceKey.identifier()) + " is already a registered built-in loot table");
        }
    }
}
