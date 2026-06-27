package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static void registerModBlocks(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Blocks for Mod " + SculkAndJaw.MOD_ID);
        MOD_BLOCKS.register(modEventBus);
    }

    public static final DeferredRegister.Blocks MOD_BLOCKS = DeferredRegister.createBlocks(SculkAndJaw.MOD_ID);
    public static final DeferredBlock<Block> SCULK_JAW;

    static {
        SCULK_JAW = MOD_BLOCKS.registerSimpleBlock("sculk_jaw", BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK).strength(3.0F, 3.0F).forceSolidOn().noOcclusion());
    }
}
