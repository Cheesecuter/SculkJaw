package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface TargetProvider {
    SculkTransporterTarget create(Level level, BlockPos blockPos, Direction direction, BlockEntity blockEntity);
}
