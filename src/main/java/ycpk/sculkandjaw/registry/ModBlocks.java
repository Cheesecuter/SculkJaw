package ycpk.sculkandjaw.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.modblocks.*;
import ycpk.sculkandjaw.core.cauldron.ModCauldronInteraction;
import ycpk.sculkandjaw.core.sculk_jaw.SculkJawInteraction;
import ycpk.sculkandjaw.level.material.ModFluids;

import java.util.function.Function;

public class ModBlocks {

    public static void registerModBlocks() {
        SculkAndJaw.LOGGER.info("Registering Blocks for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final Block SCULK_JAW;
    public static final Block CONCENTRATED_SCULK;
    public static final Block SCULK_ACID;
    public static final Block SCULK_ACID_CAULDRON;
    public static final Block SCULK_JELLY;
    public static final Block ACIDOPHILIC_CORDYCEPS;
    public static final Block UMBRAFERN;
    public static final Block LARGE_UMBRAFERN;
    public static final Block POTTED_UMBRAFERN;


    static{
        SCULK_JAW = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_jaw")
                ),
                (properties) -> {
            return new SculkJawBlock(SculkJawInteraction.SCULK_ACID, properties);
                },
                BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK)
                        .strength(3.0F, 3.0F)
                        .forceSolidOn()
                        .noOcclusion()
                        .lightLevel((blockStatex) -> {
                            return blockStatex.getValue(SculkJawBlock.ACID_FILLED) ? 7 : 0;
                        })
        );
        CONCENTRATED_SCULK = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "concentrated_sculk")
                ),
                ConcentratedSculkBlock::new,
                BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK)
                        .sound(SoundType.SCULK_CATALYST)
                        .strength(3.0F, 3.0F)
                        .lightLevel((blockStatex) -> {
                            return blockStatex.getValue(ConcentratedSculkBlock.ACID_FILLED) ? 7 : 0;
                        })
        );
        SCULK_ACID = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid")
                ),
                (properties) -> {
            return new ModLiquidBlock(ModFluids.SCULK_ACID, properties);
                },
                BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)
                        .lightLevel((blockStatex) -> {
                            return 7;
                        })
        );
        SCULK_ACID_CAULDRON = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid_cauldron")
                ),
                (properties) -> {
            return new SculkAcidCauldronBlock(ModCauldronInteraction.SCULK_ACID, properties);
                },
                BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON)
                        .lightLevel((blockStatex) -> {
                            return blockStatex.getValue(SculkAcidCauldronBlock.LEVEL) * 3 - 1;
                        })
        );
        SCULK_JELLY = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_jelly")
                ),
                SculkJelly::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_CYAN)
                        .friction(0.9F)
                        .sound(SoundType.SLIME_BLOCK)
                        .noOcclusion()
                        .lightLevel((blockStatex) -> {
                            return 3;
                        })
        );
        ACIDOPHILIC_CORDYCEPS = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "acidophilic_cordyceps")
                ),
                ModDoubleBedPlantBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_BLACK)
                        .noCollision()
                        .instabreak()
                        .sound(SoundType.CROP)
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .lightLevel((blockStatex) -> {
                            return blockStatex.getValue(ModDoubleBedPlantBlock.AMOUNT) * 4 - 1;
                        })
        );
        UMBRAFERN = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "umbrafern")
                ),
                UmbraFern::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_BLACK)
                        .replaceable()
                        .noCollision()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .offsetType(BlockBehaviour.OffsetType.XYZ)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .lightLevel((blockStatex) -> {
                            return 5;
                        })
        );
        LARGE_UMBRAFERN = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "large_umbrafern")
                ),
                LargeUmbraFern::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.COLOR_BLACK)
                        .replaceable()
                        .noCollision()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .ignitedByLava()
                        .pushReaction(PushReaction.DESTROY)
                        .lightLevel((blockStatex) -> {
                            return 9;
                        })
        );
        POTTED_UMBRAFERN = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "potted_umbrafern")
                ),
                (properties) -> {
            return new FlowerPotBlock(ModBlocks.UMBRAFERN, properties);
                },
                Blocks.flowerPotProperties()
                        .lightLevel((blockStatex) -> {
                            return 4;
                        })
        );

    }

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        ResourceKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.setId(blockKey));
        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, name));
    }
}
