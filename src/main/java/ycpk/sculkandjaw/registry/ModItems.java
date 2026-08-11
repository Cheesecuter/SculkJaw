package ycpk.sculkandjaw.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.level.material.ModFluids;

public class ModItems {
    public static void registerModItems(IEventBus modEventBus){
        SculkAndJaw.LOGGER.info("Registering Items for Mod " + SculkAndJaw.MOD_ID);
        MOD_ITEMS.register(modEventBus);
    }

    public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SculkAndJaw.MOD_ID);
    public static final RegistryObject<Item> SCULK_JAW = MOD_ITEMS.register(
            "sculk_jaw",
            () -> new BlockItem((Block) ModBlocks.SCULK_JAW.get(), new Item.Properties())
    );
    public static final RegistryObject<Item> SCULK_AGGREGATOR = MOD_ITEMS.register(
            "sculk_aggregator",
            () -> new BlockItem((Block) ModBlocks.SCULK_AGGREGATOR.get(), new Item.Properties())
    );
    public static final RegistryObject<Item> SCULK_JELLY = MOD_ITEMS.register(
            "sculk_jelly",
            () -> new BlockItem((Block) ModBlocks.SCULK_JELLY.get(), new Item.Properties())
    );
    public static final RegistryObject<Item> SCULK_ACID_BUCKET = MOD_ITEMS.register(
            "sculk_acid_bucket",
            () -> new BucketItem(ModFluids.SCULK_ACID, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1))
    );
    public static final RegistryObject<Item> ACIDCOIL_CATTAIL = MOD_ITEMS.register(
            "acidcoil_cattail",
            () -> new BlockItem((Block) ModBlocks.ACIDCOIL_CATTAIL.get(), new Item.Properties())
    );
    public static final RegistryObject<Item> UMBRAFERN = MOD_ITEMS.register(
            "umbrafern",
            () -> new BlockItem((Block) ModBlocks.UMBRAFERN.get(), new Item.Properties())
    );
    public static final RegistryObject<Item> LARGE_UMBRAFERN = MOD_ITEMS.register(
            "large_umbrafern",
            () -> new BlockItem((Block) ModBlocks.LARGE_UMBRAFERN.get(), new Item.Properties())
    );
}
