package ycpk.sculkandjaw.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModCreativeModeTabs {
    public static void registerModCreativeModeTabs(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering CreativeMode Tabs for Mod " + SculkAndJaw.MOD_ID);
        MOD_CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static final DeferredRegister<CreativeModeTab> MOD_CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SculkAndJaw.MOD_ID);
    public static final RegistryObject<CreativeModeTab> SCULK_AND_JAW_TAB = MOD_CREATIVE_MODE_TABS.register(
            "sculk_and_jaw_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.sculk_and_jaw.sculk_and_jaw_tab"))
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> ModItems.SCULK_JAW.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.SCULK_JAW.get());
                                output.accept(ModItems.SCULK_AGGREGATOR.get());
                                output.accept(ModItems.SCULK_JELLY.get());
                                output.accept(ModItems.ACIDCOIL_CATTAIL.get());
                                output.accept(ModItems.UMBRAFERN.get());
                                output.accept(ModItems.LARGE_UMBRAFERN.get());
                                output.accept(ModItems.SCULK_ACID_BUCKET.get());
                            }).build()
            );

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.getEntries().putAfter(Items.SCULK_SENSOR.getDefaultInstance(), ModItems.SCULK_JAW.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(ModItems.SCULK_JAW.get().getDefaultInstance(), ModItems.SCULK_AGGREGATOR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(ModItems.SCULK_AGGREGATOR.get().getDefaultInstance(), ModItems.SCULK_JELLY.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(ModItems.SCULK_JELLY.get().getDefaultInstance(), ModItems.ACIDCOIL_CATTAIL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(ModItems.ACIDCOIL_CATTAIL.get().getDefaultInstance(), ModItems.UMBRAFERN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(ModItems.UMBRAFERN.get().getDefaultInstance(), ModItems.LARGE_UMBRAFERN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.getEntries().putAfter(Items.MILK_BUCKET.getDefaultInstance(), ModItems.SCULK_ACID_BUCKET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
