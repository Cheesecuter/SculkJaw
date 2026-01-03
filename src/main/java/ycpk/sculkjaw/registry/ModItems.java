package ycpk.sculkjaw.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import ycpk.sculkjaw.Sculkjaw;

public class ModItems {
    public static final Item SCULK_JAW = Items.registerBlock(ModBlocks.SCULK_JAW);
    public static final Item CONCENTRATED_SCULK = Items.registerBlock(ModBlocks.CONCENTRATED_SCULK);

    private static void addItemsToNatureTabItemGroup(FabricItemGroupEntries entries){
        entries.addAfter(Items.SCULK_SENSOR, SCULK_JAW);
        entries.addAfter(ModItems.SCULK_JAW, CONCENTRATED_SCULK);
    }

    public static void registerModItems(){
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(ModItems::addItemsToNatureTabItemGroup);
        Sculkjaw.LOGGER.info("Registering Items for Mod " + Sculkjaw.MOD_ID);
    }
}
