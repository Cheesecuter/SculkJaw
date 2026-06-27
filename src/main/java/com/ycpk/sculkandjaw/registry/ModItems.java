package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import net.minecraft.world.item.BlockItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static void registerModItems(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Items for Mod " + SculkAndJaw.MOD_ID);
        MOD_ITEMS.register(modEventBus);
    }

    public static final DeferredRegister.Items MOD_ITEMS = DeferredRegister.createItems(SculkAndJaw.MOD_ID);
    public static final DeferredItem<BlockItem> SCULK_JAW = MOD_ITEMS.registerSimpleBlockItem("sculk_jaw", ModBlocks.SCULK_JAW);
}
