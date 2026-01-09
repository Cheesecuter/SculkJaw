package ycpk.sculkjaw.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.level.material.ModFluids;

public class ModItems {
    public static final Item SCULK_JAW = Items.registerBlock(ModBlocks.SCULK_JAW);
    public static final Item CONCENTRATED_SCULK = Items.registerBlock(ModBlocks.CONCENTRATED_SCULK);
    public static final Item SCULK_ACID_BUCKET = Items.registerItem(ResourceKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "sculk_acid_bucket")),
            (properties -> {
                return new BucketItem(ModFluids.SCULK_ACID, properties);
            }), (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1));

    private static void addItemsToNatureBlocksTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.SCULK_SENSOR, SCULK_JAW);
        entries.addAfter(ModItems.SCULK_JAW, CONCENTRATED_SCULK);
    }

    private static void addItemsToToolsAndUtilitiesTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.MILK_BUCKET, SCULK_ACID_BUCKET);
    }

    public static void registerModItems(){
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(ModItems::addItemsToNatureBlocksTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(ModItems::addItemsToToolsAndUtilitiesTabItemGroup);
        Sculkjaw.LOGGER.info("Registering Items for Mod " + Sculkjaw.MOD_ID);
    }
}
