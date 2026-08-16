package ycpk.sculkandjaw.world.level.sculktransporternetwork.nodes;

import net.minecraft.core.BlockPos;

public class SculkTransporterNode {
    private final BlockPos blockPos;
    private final SculkTransporterNodeType type;

    public SculkTransporterNode(BlockPos blockPos, SculkTransporterNodeType type) {
        this.blockPos = blockPos;
        this.type = type;
    }

    public BlockPos getPos() {
        return this.blockPos;
    }

    public SculkTransporterNodeType getType() {
        return this.type;
    }
}
