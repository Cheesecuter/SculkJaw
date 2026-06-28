package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.blocks.blockentities.ConcentratedSculkEntity;
import com.ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static void registerModBlockEntities(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Block Entities for Mod " + SculkAndJaw.MOD_ID);
        MOD_BLOCK_ENTITIES.register(modEventBus);
    }

    public static final DeferredRegister<BlockEntityType<?>> MOD_BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SculkAndJaw.MOD_ID);

    public static final Supplier<BlockEntityType<SculkJawBlockEntity>> SCULK_JAW_BLOCK_ENTITY = MOD_BLOCK_ENTITIES.register(
            "sculk_jaw",
            () -> BlockEntityType.Builder.of(
                    SculkJawBlockEntity::new,
                    ModBlocks.SCULK_JAW.get()
            )
                    .build(null)
    );
    public static final Supplier<BlockEntityType<ConcentratedSculkEntity>> CONCENTRATED_SCULK_ENTITY = MOD_BLOCK_ENTITIES.register(
            "concentrated_sculk",
            () -> BlockEntityType.Builder.of(
                    ConcentratedSculkEntity::new,
                    ModBlocks.CONCENTRATED_SCULK.get()
            )
                    .build(null)
    );
}
