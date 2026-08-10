package ycpk.sculkandjaw.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.blockentities.SculkAggregatorBlockEntity;
import ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;

public class ModBlockEntities {
    public static void registerModBlockEntities(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Block Entities for Mod " + SculkAndJaw.MOD_ID);
        MOD_BLOCK_ENTITIES.register(modEventBus);
    }

    public static final DeferredRegister<BlockEntityType<?>> MOD_BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SculkAndJaw.MOD_ID);
    public static final RegistryObject<BlockEntityType<SculkJawBlockEntity>> SCULK_JAW_BLOCK_ENTITY =
            MOD_BLOCK_ENTITIES.register(
                    "sculk_jaw",
                    () -> BlockEntityType.Builder.of(
                            SculkJawBlockEntity::new,
                            ModBlocks.SCULK_JAW.get()
                    )
                            .build(null)
            );
    public static final RegistryObject<BlockEntityType<SculkAggregatorBlockEntity>> SCULK_AGGREGATOR_BLOCK_ENTITY =
            MOD_BLOCK_ENTITIES.register(
                    "sculk_aggregator",
                    () -> BlockEntityType.Builder.of(
                            SculkAggregatorBlockEntity::new,
                            ModBlocks.SCULK_AGGREGATOR.get()
                    )
                            .build(null)
            );
}
