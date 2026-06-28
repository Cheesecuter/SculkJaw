package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.blocks.modblocks.ConcentratedSculkBlock;
import com.ycpk.sculkandjaw.blocks.modblocks.SculkJawBlock;
import com.ycpk.sculkandjaw.level.material.ModFluids;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static void registerModBlocks(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Blocks for Mod " + SculkAndJaw.MOD_ID);
        MOD_BLOCKS.register(modEventBus);
    }

    public static final DeferredRegister.Blocks MOD_BLOCKS = DeferredRegister.createBlocks(SculkAndJaw.MOD_ID);
    public static final DeferredBlock<SculkJawBlock> SCULK_JAW;
    public static final DeferredBlock<Block> CONCENTRATED_SCULK;
    public static final DeferredHolder<Block, LiquidBlock> SCULK_ACID;
    public static final DeferredBlock<Block> SCULK_ACID_CAULDRON;
    public static final DeferredBlock<Block> SCULK_JELLY;
    public static final DeferredBlock<Block> ACIDOPHILIC_CORDYCEPS;
    public static final DeferredBlock<Block> UMBRAFERN;
    public static final DeferredBlock<Block> LARGE_UMBRAFERN;
    public static final DeferredBlock<Block> POTTED_UMBRAFERN;

    static {
        SCULK_JAW = MOD_BLOCKS.register(
                "sculk_jaw",
                () -> new SculkJawBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK)
                                .strength(3.0F, 3.0F)
                                .forceSolidOn()
                                .noOcclusion()
                )
        );
        CONCENTRATED_SCULK = MOD_BLOCKS.register(
                "concentrated_sculk",
                () -> new ConcentratedSculkBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK)
                                .sound(SoundType.SCULK_CATALYST)
                                .strength(3.0F, 3.0F)
                )
        );
        SCULK_ACID = MOD_BLOCKS.register(
                "sculk_acid",
                () -> new LiquidBlock(
                        ModFluids.SCULK_ACID.get(),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)
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
        SCULK_ACID_CAULDRON = MOD_BLOCKS.registerSimpleBlock("sculk_acid_cauldron",
                BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON));
        SCULK_JELLY = MOD_BLOCKS.registerSimpleBlock("sculk_jelly",
                BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).mapColor(MapColor.COLOR_CYAN).friction(0.9F).sound(SoundType.SLIME_BLOCK).noOcclusion());
        ACIDOPHILIC_CORDYCEPS = MOD_BLOCKS.registerSimpleBlock("acidophilic_cordyceps",
                BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_PETALS).mapColor(MapColor.COLOR_BLACK).noCollission().instabreak().sound(SoundType.CROP).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY));
        UMBRAFERN = MOD_BLOCKS.registerSimpleBlock("umbrafern",
                BlockBehaviour.Properties.ofFullCopy(Blocks.FERN).mapColor(MapColor.COLOR_BLACK).replaceable().noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ).ignitedByLava().pushReaction(PushReaction.DESTROY));
        LARGE_UMBRAFERN = MOD_BLOCKS.registerSimpleBlock("large_umbrafern",
                BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_FERN).mapColor(MapColor.COLOR_BLACK).replaceable().noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY));
        POTTED_UMBRAFERN = MOD_BLOCKS.registerSimpleBlock("potted_umbrafern",
                BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));
    }
}
