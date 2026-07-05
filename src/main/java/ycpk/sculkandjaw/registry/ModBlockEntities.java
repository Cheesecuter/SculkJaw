package ycpk.sculkandjaw.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.blockentities.ConcentratedSculkEntity;
import ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;

public class ModBlockEntities {
    public static void registerModBlockEntities() {
        SculkAndJaw.LOGGER.info("Registering Block Entities for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final BlockEntityType<SculkJawBlockEntity> SCULK_JAW_BLOCK_ENTITY =
            register("sculk_jaw", SculkJawBlockEntity::new, ModBlocks.SCULK_JAW);
    public static final BlockEntityType<ConcentratedSculkEntity> CONCENTRATED_SCULK_ENTITY =
            register("concentrated_sculk", ConcentratedSculkEntity::new, ModBlocks.CONCENTRATED_SCULK);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String identifier,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks) {
        Identifier id = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, identifier);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
