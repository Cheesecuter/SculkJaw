package ycpk.sculkandjaw.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.world.item.alchemy.ModPotions;

public class ModCreativeModeTabs {
    public static void registerModCreativeModeTabs() {
        SculkAndJaw.LOGGER.info("Registering CreativeMode Tabs for Mod " + SculkAndJaw.MOD_ID);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, SCULK_AND_JAW_TAB_KEY, SCULK_AND_JAW_TAB);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(ModCreativeModeTabs::addItemsToNatureBlocksTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(ModCreativeModeTabs::addItemsToToolsAndUtilitiesTabItemGroup);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(ModCreativeModeTabs::addItemsToFoodAndDrinksTabItemGroup);

    }

    public static final ResourceKey<CreativeModeTab> SCULK_AND_JAW_TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_and_jaw_creative_tab"));
    public static final CreativeModeTab SCULK_AND_JAW_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.SCULK_JAW))
            .title(Component.translatable("itemGroup.sculk_and_jaw.sculk_and_jaw_tab"))
            .displayItems((params, output) -> {
                output.accept(ModItems.SCULK_JAW);
                output.accept(ModItems.SCULK_AGGREGATOR);
                output.accept(ModItems.SCULK_JELLY);
                output.accept(ModItems.ACIDOPHILIC_CORDYCEPS);
                output.accept(ModItems.UMBRAFERN);
                output.accept(ModItems.LARGE_UMBRAFERN);
                output.accept(ModItems.SCULK_ACID_BUCKET);
                ItemStack acidEtchingPotion = new ItemStack(Items.POTION);
                acidEtchingPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.ACID_ETCHING));
                output.accept(acidEtchingPotion);
                ItemStack longAcidEtchingPotion = new ItemStack(Items.POTION);
                longAcidEtchingPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.LONG_ACID_ETCHING));
                output.accept(longAcidEtchingPotion);
                ItemStack strongAcidEtchingPotion = new ItemStack(Items.POTION);
                strongAcidEtchingPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.STRONG_ACID_ETCHING));
                output.accept(strongAcidEtchingPotion);
                ItemStack acidEtchingSplashPotion = new ItemStack(Items.SPLASH_POTION);
                acidEtchingSplashPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.ACID_ETCHING));
                output.accept(acidEtchingSplashPotion);
                ItemStack longAcidEtchingSplashPotion = new ItemStack(Items.SPLASH_POTION);
                longAcidEtchingSplashPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.LONG_ACID_ETCHING));
                output.accept(longAcidEtchingSplashPotion);
                ItemStack strongAcidEtchingSplashPotion = new ItemStack(Items.SPLASH_POTION);
                strongAcidEtchingSplashPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.STRONG_ACID_ETCHING));
                output.accept(strongAcidEtchingSplashPotion);
                ItemStack acidEtchingLingeringPotion = new ItemStack(Items.LINGERING_POTION);
                acidEtchingLingeringPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.ACID_ETCHING));
                output.accept(acidEtchingLingeringPotion);
                ItemStack longAcidEtchingLingeringPotion = new ItemStack(Items.LINGERING_POTION);
                longAcidEtchingLingeringPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.LONG_ACID_ETCHING));
                output.accept(longAcidEtchingLingeringPotion);
                ItemStack strongAcidEtchingLingeringPotion = new ItemStack(Items.LINGERING_POTION);
                strongAcidEtchingLingeringPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(ModPotions.STRONG_ACID_ETCHING));
                output.accept(strongAcidEtchingLingeringPotion);
            })
            .build();

    private static void addItemsToNatureBlocksTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.SCULK_SENSOR, ModItems.SCULK_JAW);
        entries.addAfter(ModItems.SCULK_JAW, ModItems.SCULK_AGGREGATOR);
        entries.addAfter(ModItems.SCULK_AGGREGATOR, ModItems.SCULK_JELLY);
        entries.addAfter(ModItems.SCULK_JELLY, ModItems.ACIDOPHILIC_CORDYCEPS);
        entries.addAfter(ModItems.ACIDOPHILIC_CORDYCEPS, ModItems.UMBRAFERN);
        entries.addAfter(ModItems.UMBRAFERN, ModItems.LARGE_UMBRAFERN);
    }

    private static void addItemsToToolsAndUtilitiesTabItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.MILK_BUCKET, ModItems.SCULK_ACID_BUCKET);
    }

    private static void addItemsToFoodAndDrinksTabItemGroup(FabricItemGroupEntries entries) {
    }
}
