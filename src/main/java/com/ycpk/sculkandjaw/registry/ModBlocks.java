package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.blocks.modblocks.*;
import com.ycpk.sculkandjaw.core.cauldron.ModCauldronInteratction;
import com.ycpk.sculkandjaw.core.sculk_jaw.SculkJawInteraction;
import com.ycpk.sculkandjaw.level.material.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
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
                        SculkJawInteraction.SCULK_ACID,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK)
                                .strength(3.0F, 3.0F)
                                .forceSolidOn()
                                .noOcclusion()
                                .lightLevel((blockStatex) -> {
                                    return blockStatex.getValue(SculkJawBlock.ACID_FILLED) ? 7 : 0;
                                })
                )
        );
        CONCENTRATED_SCULK = MOD_BLOCKS.register(
                "concentrated_sculk",
                () -> new ConcentratedSculkBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK)
                                .sound(SoundType.SCULK_CATALYST)
                                .strength(3.0F, 3.0F)
                                .lightLevel((blockStatex) -> {
                                    return blockStatex.getValue(ConcentratedSculkBlock.ACID_FILLED) ? 7 : 0;
                                })
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
                                .lightLevel((blockStatex) -> {
                                    return 7;
                                })
                )
        );
        SCULK_ACID_CAULDRON = MOD_BLOCKS.register(
                "sculk_acid_cauldron",
                () -> new SculkAcidCauldronBlock(
                        ModCauldronInteratction.SCULK_ACID,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON)
                                .lightLevel((blockStatex) -> {
                                    return blockStatex.getValue(SculkAcidCauldronBlock.LEVEL) * 3 - 1;
                                })
                )
        );
        SCULK_JELLY = MOD_BLOCKS.register(
                "sculk_jelly",
                () -> new SculkJelly(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK)
                                .mapColor(MapColor.COLOR_CYAN)
                                .friction(0.9F)
                                .sound(SoundType.SLIME_BLOCK)
                                .noOcclusion()
                                .lightLevel((blockStatex) -> {
                                    return 3;
                                })
                )
        );
        ACIDOPHILIC_CORDYCEPS = MOD_BLOCKS.register(
                "acidophilic_cordyceps",
                () -> new ModDoubleBedPlantBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_PETALS)
                                .mapColor(MapColor.COLOR_BLACK)
                                .noCollission()
                                .instabreak()
                                .sound(SoundType.CROP)
                                .offsetType(BlockBehaviour.OffsetType.XZ)
                                .ignitedByLava()
                                .pushReaction(PushReaction.DESTROY)
                                .lightLevel((blockStatex) -> {
                                    return blockStatex.getValue(ModDoubleBedPlantBlock.AMOUNT) * 4 - 1;
                                })
                                .emissiveRendering(ModBlocks::always)
                )
        );
        UMBRAFERN = MOD_BLOCKS.register(
                "umbrafern",
                () -> new UmbraFern(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.FERN)
                                .mapColor(MapColor.COLOR_BLACK)
                                .replaceable().noCollission()
                                .instabreak()
                                .sound(SoundType.GRASS)
                                .offsetType(BlockBehaviour.OffsetType.XYZ)
                                .ignitedByLava()
                                .pushReaction(PushReaction.DESTROY)
                                .lightLevel((blockStatex) -> {
                                    return 5;
                                })
                                .emissiveRendering(ModBlocks::always)
                )
        );
        LARGE_UMBRAFERN = MOD_BLOCKS.register(
                "large_umbrafern",
                () -> new LargeUmbraFern(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.LARGE_FERN)
                                .mapColor(MapColor.COLOR_BLACK)
                                .replaceable()
                                .noCollission()
                                .instabreak()
                                .sound(SoundType.GRASS)
                                .offsetType(BlockBehaviour.OffsetType.XZ)
                                .ignitedByLava()
                                .pushReaction(PushReaction.DESTROY)
                                .lightLevel((blockStatex) -> {
                                    return 9;
                                })
                                .emissiveRendering(ModBlocks::always)
                )
        );
        POTTED_UMBRAFERN = MOD_BLOCKS.register(
                "potted_umbrafern",
                () -> new FlowerPotBlock(
                        UMBRAFERN.value(),
                        BlockBehaviour.Properties.of()
                                .instabreak()
                                .noOcclusion()
                                .pushReaction(PushReaction.DESTROY)
                                .lightLevel((blockStatex) -> {
                                    return 4;
                                })
                                .emissiveRendering(ModBlocks::always)
                )
        );
    }

    public static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    public static Boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entity) {
        return true;
    }
}
