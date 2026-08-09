package ycpk.sculkandjaw.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
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
    public static final Block SCULK_AGGREGATOR;
    public static final Block SCULK_ACID;
    public static final Block SCULK_ACID_CAULDRON;
    public static final Block SCULK_JELLY;
    public static final Block ACIDCOIL_CATTAIL;
    public static final Block UMBRAFERN;
    public static final Block LARGE_UMBRAFERN;
    public static final Block POTTED_UMBRAFERN;

    public static Block register(String identifier, Block block) {
        return (Block) Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(SculkAndJaw.MOD_ID, identifier), block);
    }

    static {
        SCULK_JAW = register(
                "sculk_jaw",
                new SculkJaw(
                        SculkJawInteraction.SCULK_ACID,
                        BlockBehaviour.Properties.copy(Blocks.SCULK)
                                .strength(3.0F, 3.0F)
                                .forceSolidOn()
                                .noOcclusion()
                )
        );
        SCULK_AGGREGATOR = register(
                "sculk_aggregator",
                new SculkAggregator(
                        BlockBehaviour.Properties.copy(Blocks.SCULK)
                                .sound(SoundType.SCULK_CATALYST)
                                .strength(3.0F, 3.0F)
                )
        );
        SCULK_ACID = register(
                "sculk_acid",
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
        SCULK_ACID_CAULDRON = register(
                "sculk_acid_cauldron",
                new SculkAcidCauldronBlock(
                        ModCauldronInteraction.SCULK_ACID,
                        BlockBehaviour.Properties.copy(Blocks.CAULDRON)
                )
        );
        SCULK_JELLY = register(
                "sculk_jelly",
                new SculkJelly(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).friction(0.9F).sound(SoundType.SLIME_BLOCK).noOcclusion()));
        ACIDCOIL_CATTAIL = register(
                "acidcoil_cattail",
                new AcidcoilCattail(
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
        UMBRAFERN  = register(
                "umbrafern",
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
        LARGE_UMBRAFERN = register(
                "large_umbrafern",
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
        POTTED_UMBRAFERN = register(
                "potted_umbrafern",
                new FlowerPotBlock(
                        UMBRAFERN,
                        BlockBehaviour.Properties.of()
                                .instabreak()
                                .noOcclusion()
                                .pushReaction(PushReaction.DESTROY)
                )
        );
    }
}
