package ycpk.sculkandjaw.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ycpk.sculkandjaw.registry.ModBlockEntities;

public class SculkTeleporterBlockEntity extends BlockEntity {
    public SculkTeleporterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SCULK_TELEPORTER_BLOCK_ENTITY, blockPos, blockState);
    }
}
