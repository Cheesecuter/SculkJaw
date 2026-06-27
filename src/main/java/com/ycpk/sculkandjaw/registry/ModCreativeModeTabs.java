package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
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
            .icon(() -> ModItems.SCULK_JAW.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.SCULK_JAW.get());
            }).build());

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModItems.SCULK_JAW);
        }
    }
}
