package ycpk.sculkandjaw.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.blockentities.*;

public class ModBlockEntities {
    public static void registerModBlockEntities() {
        SculkAndJaw.LOGGER.info("Registering Block Entities for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final BlockEntityType<SculkJawBlockEntity> SCULK_JAW_BLOCK_ENTITY =
            register("sculk_jaw", SculkJawBlockEntity::new, ModBlocks.SCULK_JAW);
    public static final BlockEntityType<SculkAggregatorBlockEntity> SCULK_AGGREGATOR_BLOCK_ENTITY =
            register("sculk_aggregator", SculkAggregatorBlockEntity::new, ModBlocks.SCULK_AGGREGATOR);
    public static final BlockEntityType<TunedSculkJawBlockEntity> TUNED_SCULK_JAW_BLOCK_ENTITY =
            register("tuned_sculk_jaw", TunedSculkJawBlockEntity::new, ModBlocks.TUNED_SCULK_JAW);
    public static final BlockEntityType<SculkTransporterBlockEntity> SCULK_TRANSPORTER_BLOCK_ENTITY =
            register("sculk_transporter", SculkTransporterBlockEntity::new, ModBlocks.SCULK_TRANSPORTER);
    public static final BlockEntityType<SculkTeleporterBlockEntity> SCULK_TELEPORTER_BLOCK_ENTITY =
            register("sculk_teleporter", SculkTeleporterBlockEntity::new, ModBlocks.SCULK_TELEPORTER);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String identifier,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks) {
        Identifier id = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, identifier);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
