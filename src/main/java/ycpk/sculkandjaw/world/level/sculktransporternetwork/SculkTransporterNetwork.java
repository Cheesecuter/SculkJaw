package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SculkTransporterNetwork {
    private final Set<BlockPos> nodes = new HashSet<>();
    private final Set<BlockPos> inputs = new HashSet<>();
    private final Set<BlockPos> outputs = new HashSet<>();
    private final Map<BlockPos, BlockPos> nextNode = new HashMap<>();
}
