package ycpk.sculkandjaw.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.modblocks.*;
import ycpk.sculkandjaw.core.cauldron.ModCauldronInteraction;
import ycpk.sculkandjaw.core.sculk_jaw.SculkJawInteraction;
import ycpk.sculkandjaw.level.material.ModFluids;

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


    static {
        SCULK_JAW = Blocks.register(ResourceKey.create(
                        Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_jaw")),
                new SculkJawBlock(
                        SculkJawInteraction.SCULK_ACID,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK)
                                .strength(3.0F, 3.0F)
                                .forceSolidOn()
                                .noOcclusion()
                )
        );
        CONCENTRATED_SCULK = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "concentrated_sculk")),
                new ConcentratedSculkBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK)
                                .sound(SoundType.SCULK_CATALYST)
                                .strength(3.0F, 3.0F)
                )
        );
        SCULK_ACID = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid")),
                new ModLiquidBlock(
                        ModFluids.SCULK_ACID,
                        BlockBehaviour.Properties.of()
                                .mapColor(MapColor.COLOR_CYAN)
                                .replaceable()
                                .noCollission()
                                .strength(100.0F)
                                .pushReaction(PushReaction.DESTROY)
                                .noLootTable()
                                .liquid()
                                .sound(SoundType.EMPTY)
                )
        );
        SCULK_ACID_CAULDRON = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid_cauldron")),
                new SculkAcidCauldronBlock(
                        ModCauldronInteraction.SCULK_ACID,
                        BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAULDRON)
                )
        );
        SCULK_JELLY = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_jelly")),
                new SculkJelly(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).friction(0.9F).sound(SoundType.SLIME_BLOCK).noOcclusion()));
        ACIDOPHILIC_CORDYCEPS = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "acidophilic_cordyceps")),
                new ModDoubleBedPlantBlock(
                        BlockBehaviour.Properties.of()
                                .mapColor(MapColor.COLOR_BLACK)
                                .noCollission()
                                .instabreak()
                                .sound(SoundType.CROP)
                                .offsetType(BlockBehaviour.OffsetType.XZ)
                                .ignitedByLava()
                                .pushReaction(PushReaction.DESTROY)
                )
        );
        UMBRAFERN  = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "umbrafern")),
                new UmbraFern(
                        BlockBehaviour.Properties.of()
                                .mapColor(MapColor.COLOR_BLACK)
                                .replaceable()
                                .noCollission()
                                .instabreak()
                                .sound(SoundType.GRASS)
                                .offsetType(BlockBehaviour.OffsetType.XYZ)
                                .ignitedByLava()
                                .pushReaction(PushReaction.DESTROY)
                )
        );
        LARGE_UMBRAFERN = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "large_umbrafern")),
                new LargeUmbraFern(
                        BlockBehaviour.Properties.of()
                                .mapColor(MapColor.COLOR_BLACK)
                                .replaceable()
                                .noCollission()
                                .instabreak()
                                .sound(SoundType.GRASS)
                                .offsetType(BlockBehaviour.OffsetType.XZ)
                                .ignitedByLava()
                                .pushReaction(PushReaction.DESTROY)
                )
        );
        POTTED_UMBRAFERN = Blocks.register(ResourceKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "potted_umbrafern")),
                new FlowerPotBlock(
                        UMBRAFERN,
                        BlockBehaviour.Properties.of()
                                .instabreak()
                                .noOcclusion()
                                .pushReaction(PushReaction.DESTROY)
                )
        );
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, name));
    }
}
