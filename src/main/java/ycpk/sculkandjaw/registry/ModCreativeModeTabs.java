package ycpk.sculkandjaw.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModCreativeModeTabs {
    public static void registerModCreativeModeTabs() {
        SculkAndJaw.LOGGER.info("Registering CreativeMode Tabs for Mod " + SculkAndJaw.MOD_ID);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SCULK_AND_JAW_TAB_KEY, SCULK_AND_JAW_TAB);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(ModCreativeModeTabs::addItemsToNatureBlocksTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(ModCreativeModeTabs::addItemsToToolsAndUtilitiesTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(ModCreativeModeTabs::addItemsToFoodAndDrinksTabItemGroup);

    }

    public static final ResourceKey<CreativeModeTab> SCULK_AND_JAW_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(SculkAndJaw.MOD_ID, "sculk_and_jaw_creative_tab"));
    public static final CreativeModeTab SCULK_AND_JAW_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.SCULK_JAW))
            .title(Component.translatable("itemGroup.sculk_and_jaw.sculk_and_jaw_tab"))
            .displayItems((params, output) -> {
                output.accept(ModItems.SCULK_JAW);
                output.accept(ModItems.SCULK_AGGREGATOR);
                output.accept(ModItems.SCULK_JELLY);
                output.accept(ModItems.ACIDCOIL_CATTAIL);
                output.accept(ModItems.UMBRAFERN);
                output.accept(ModItems.LARGE_UMBRAFERN);
                output.accept(ModItems.SCULK_ACID_BUCKET);
            })
            .build();

    private static void addItemsToNatureBlocksTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.SCULK_SENSOR, ModItems.SCULK_JAW);
        entries.addAfter(ModItems.SCULK_JAW, ModItems.SCULK_AGGREGATOR);
        entries.addAfter(ModItems.SCULK_AGGREGATOR, ModItems.SCULK_JELLY);
        entries.addAfter(ModItems.SCULK_JELLY, ModItems.ACIDCOIL_CATTAIL);
        entries.addAfter(ModItems.ACIDCOIL_CATTAIL, ModItems.UMBRAFERN);
        entries.addAfter(ModItems.UMBRAFERN, ModItems.LARGE_UMBRAFERN);
    }

    private static void addItemsToToolsAndUtilitiesTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.MILK_BUCKET, ModItems.SCULK_ACID_BUCKET);
    }

    private static void addItemsToFoodAndDrinksTabItemGroup(FabricItemGroupEntries entries) {
    }
}
