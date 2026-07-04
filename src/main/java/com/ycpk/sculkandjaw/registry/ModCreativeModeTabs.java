package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.world.item.alchemy.ModPotions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    public static void registerModCreativeModeTabs(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering CreativeMode Tabs for Mod " + SculkAndJaw.MOD_ID);
        MOD_CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static final DeferredRegister<CreativeModeTab> MOD_CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SculkAndJaw.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SCULK_AND_JAW_TAB = MOD_CREATIVE_MODE_TABS.register("sculk_and_jaw_creative_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.sculk_and_jaw.sculk_and_jaw_tab"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.SCULK_JAW.value().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.SCULK_JAW.value());
                output.accept(ModItems.CONCENTRATED_SCULK.value());
                output.accept(ModItems.SCULK_JELLY.value());
                output.accept(ModItems.ACIDOPHILIC_CORDYCEPS.value());
                output.accept(ModItems.UMBRAFERN.value());
                output.accept(ModItems.LARGE_UMBRAFERN.value());
                output.accept(ModItems.SCULK_ACID_BUCKET.value());
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
            }).build());

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.insertAfter(Items.SCULK_SENSOR.getDefaultInstance(), ModItems.SCULK_JAW.value().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.SCULK_JAW.value().getDefaultInstance(), ModItems.CONCENTRATED_SCULK.value().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.CONCENTRATED_SCULK.value().getDefaultInstance(), ModItems.SCULK_JELLY.value().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.SCULK_JELLY.value().getDefaultInstance(), ModItems.ACIDOPHILIC_CORDYCEPS.value().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.ACIDOPHILIC_CORDYCEPS.value().getDefaultInstance(), ModItems.UMBRAFERN.value().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.UMBRAFERN.value().getDefaultInstance(), ModItems.LARGE_UMBRAFERN.value().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.insertAfter(Items.MILK_BUCKET.getDefaultInstance(), ModItems.SCULK_ACID_BUCKET.value().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
