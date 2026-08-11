package ycpk.sculkandjaw.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ycpk.sculkandjaw.registry.ModBlockEntities;

public class SculkTransporterBlockEntity extends BlockEntity {
    public SculkTransporterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SCULK_TRANSPORTER_BLOCK_ENTITY, blockPos, blockState);
    }
}
