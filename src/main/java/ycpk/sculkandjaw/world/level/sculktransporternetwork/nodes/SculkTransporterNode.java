package ycpk.sculkandjaw.world.level.sculktransporternetwork.nodes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.EnumSet;
import java.util.Set;

public class SculkTransporterNode {
    private final BlockPos blockPos;
    private final SculkTransporterNodeType type;
    private final EnumSet<Direction> connections = EnumSet.noneOf(Direction.class);

    public SculkTransporterNode(BlockPos blockPos, SculkTransporterNodeType type) {
        this.blockPos = blockPos.immutable();
        this.type = type;
    }

    public BlockPos getPos() {
        return this.blockPos;
    }

    public SculkTransporterNodeType getType() {
        return this.type;
    }

    public Set<Direction> getConnections() {
        return this.connections;
    }

    public void addConnection(Direction direction) {
        this.connections.add(direction);
    }

    public boolean hasConnection(Direction direction) {
        return this.connections.contains(direction);
    }

    public boolean isTransporter() {
        return this.type == SculkTransporterNodeType.TRANSPORTER;
    }

    public boolean isInput() {
        return this.type == SculkTransporterNodeType.INPUT;
    }

    public boolean isOutput() {
        return this.type == SculkTransporterNodeType.OUTPUT;
    }
}
