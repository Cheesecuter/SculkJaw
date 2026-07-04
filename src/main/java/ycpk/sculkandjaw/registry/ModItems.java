package ycpk.sculkandjaw.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.level.material.ModFluids;

public class ModItems {
    public static void registerModItems(){
        SculkAndJaw.LOGGER.info("Registering Items for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final Item SCULK_JAW = Items.registerBlock(ModBlocks.SCULK_JAW);
    public static final Item CONCENTRATED_SCULK = Items.registerBlock(ModBlocks.CONCENTRATED_SCULK);
    public static final Item SCULK_JELLY = Items.registerBlock(ModBlocks.SCULK_JELLY);
    public static final Item SCULK_ACID_BUCKET = Items.registerItem(ResourceKey.create(Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid_bucket")),
            new BucketItem(ModFluids.SCULK_ACID, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final Item ACIDOPHILIC_CORDYCEPS = Items.registerBlock(ModBlocks.ACIDOPHILIC_CORDYCEPS);
    public static final Item UMBRAFERN = Items.registerBlock(ModBlocks.UMBRAFERN);
    public static final Item LARGE_UMBRAFERN = Items.registerBlock(ModBlocks.LARGE_UMBRAFERN);
}
