package ycpk.sculkandjaw.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.blockentities.SculkAggregatorBlockEntity;
import ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;
import ycpk.sculkandjaw.core.legacy.ConcentratedSculkBlockEntity;

public class ModBlockEntities {
    public static void registerModBlockEntities() {
        SculkAndJaw.LOGGER.info("Registering Block Entities for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final BlockEntityType<SculkJawBlockEntity> SCULK_JAW_BLOCK_ENTITY =
            register("sculk_jaw", SculkJawBlockEntity::new, ModBlocks.SCULK_JAW);
    public static final BlockEntityType<SculkAggregatorBlockEntity> SCULK_AGGREGATOR_BLOCK_ENTITY =
            register("sculk_aggregator", SculkAggregatorBlockEntity::new, ModBlocks.SCULK_AGGREGATOR);
    /*
     * Legacy Block Entities
     * */
    public static final BlockEntityType<ConcentratedSculkBlockEntity> CONCENTRATED_SCULK_BLOCK_ENTITY =
            register("concentrated_sculk", ConcentratedSculkBlockEntity::new, ModBlocks.CONCENTRATED_SCULK);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String identifier,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks) {
        Identifier id = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, identifier);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
