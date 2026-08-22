package ycpk.sculkandjaw.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.DeathProtection;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.level.material.ModFluids;
import ycpk.sculkandjaw.world.food.ModFoods;
import ycpk.sculkandjaw.world.item.DebugTool;
import ycpk.sculkandjaw.world.item.SpatialAnchorItem;
import ycpk.sculkandjaw.world.item.component.ModConsumables;

public class ModItems {
    public static void registerModItems(){
        SculkAndJaw.LOGGER.info("Registering Items for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final Item SCULK_JAW = Items.registerBlock(ModBlocks.SCULK_JAW);
    public static final Item SCULK_AGGREGATOR = Items.registerBlock(ModBlocks.SCULK_AGGREGATOR);
    public static final Item TUNED_SCULK_JAW = Items.registerBlock(ModBlocks.TUNED_SCULK_JAW);
    public static final Item SCULK_TRANSPORTER = Items.registerBlock(ModBlocks.SCULK_TRANSPORTER);
    public static final Item SCULK_TELEPORTER = Items.registerBlock(ModBlocks.SCULK_TELEPORTER);
    public static final Item SCULK_JELLY = Items.registerBlock(ModBlocks.SCULK_JELLY);
    public static final Item ACIDCOIL_CATTAIL = Items.registerBlock(ModBlocks.ACIDCOIL_CATTAIL);
    public static final Item UMBRAFERN = Items.registerBlock(ModBlocks.UMBRAFERN);
    public static final Item LARGE_UMBRAFERN = Items.registerBlock(ModBlocks.LARGE_UMBRAFERN);
    public static final Item SCULK_ACID_BUCKET = Items.registerItem(
            ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid_bucket")),
            (properties -> {
                return new BucketItem(ModFluids.SCULK_ACID, properties);
            }),
            (new Item.Properties())
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1)
    );
    public static final Item ANTACID_DROPLET = Items.registerItem(
            ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "antacid_droplet")),
            Item::new,
            (new Item.Properties())
                    .food(ModFoods.ANTACID_DROPLET, ModConsumables.ANTACID_DROPLET)
    );
    public static final Item SPATIAL_ANCHOR = Items.registerItem(
            ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "spatial_anchor")),
            SpatialAnchorItem::new,
            (new Item.Properties())
                    .stacksTo(1)
                    .durability(101)
                    .rarity(Rarity.UNCOMMON)
                    .repairable(Items.ECHO_SHARD)
    );
    public static final Item SCULK_AND_JAW_DEBUG_ITEM = Items.registerItem(
            ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_and_jaw_debug_tool")),
            DebugTool::new,
            (new Item.Properties())
                    .stacksTo(1)
                    .rarity(Rarity.EPIC)
                    .component(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
    );
}
