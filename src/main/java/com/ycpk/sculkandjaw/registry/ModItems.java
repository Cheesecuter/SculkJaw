package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.level.material.ModFluids;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static void registerModItems(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Items for Mod " + SculkAndJaw.MOD_ID);
        MOD_ITEMS.register(modEventBus);
    }

    public static final DeferredRegister.Items MOD_ITEMS = DeferredRegister.createItems(SculkAndJaw.MOD_ID);
    public static final DeferredItem<BlockItem> SCULK_JAW = MOD_ITEMS.registerSimpleBlockItem("sculk_jaw", ModBlocks.SCULK_JAW);
    public static final DeferredItem<BlockItem> CONCENTRATED_SCULK = MOD_ITEMS.registerSimpleBlockItem("concentrated_sculk", ModBlocks.CONCENTRATED_SCULK);
    public static final DeferredItem<BlockItem> SCULK_JELLY = MOD_ITEMS.registerSimpleBlockItem("sculk_jelly", ModBlocks.SCULK_JELLY);
    public static final DeferredHolder<Item, BucketItem> SCULK_ACID_BUCKET = MOD_ITEMS.register(
            "sculk_acid_bucket",
            () -> new BucketItem(
                    ModFluids.SCULK_ACID.value(),
                    (new Item.Properties())
                            .craftRemainder(Items.BUCKET)
                            .stacksTo(1)
            )
    );
    public static final DeferredItem<BlockItem> ACIDOPHILIC_CORDYCEPS = MOD_ITEMS.registerSimpleBlockItem("acidophilic_cordyceps", ModBlocks.ACIDOPHILIC_CORDYCEPS);
    public static final DeferredItem<BlockItem> UMBRAFERN = MOD_ITEMS.registerSimpleBlockItem("umbrafern", ModBlocks.UMBRAFERN);
    public static final DeferredItem<BlockItem> LARGE_UMBRAFERN = MOD_ITEMS.registerSimpleBlockItem("large_umbrafern", ModBlocks.LARGE_UMBRAFERN);
}
