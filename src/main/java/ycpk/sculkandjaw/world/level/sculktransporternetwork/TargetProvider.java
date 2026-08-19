package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.Optional;

@FunctionalInterface
public interface TargetProvider {
    Optional<SculkTransporterTarget> createTarget(Level level, BlockPos blockPos, Direction direction);
}
