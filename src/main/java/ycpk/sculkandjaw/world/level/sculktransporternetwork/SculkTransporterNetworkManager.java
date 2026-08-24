package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import ycpk.sculkandjaw.blocks.blockentities.SculkTransporterBlockEntity;
import ycpk.sculkandjaw.blocks.blockentities.TunedSculkJawBlockEntity;
import ycpk.sculkandjaw.blocks.modblocks.TunedSculkJawBlock;
import ycpk.sculkandjaw.world.level.block.state.properties.TunedSculkJawIOState;
import ycpk.sculkandjaw.world.level.sculktransporternetwork.nodes.SculkTransporterNode;
import ycpk.sculkandjaw.world.level.sculktransporternetwork.nodes.SculkTransporterNodeType;

import java.util.*;

public class SculkTransporterNetworkManager {
    private final ServerLevel serverLevel;
    private final Map<BlockPos, SculkTransporterNetwork> nodeNetworks = new HashMap<>();
    private boolean dirty = true;

    public SculkTransporterNetworkManager(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
    }

    public void markDirty() {
        dirty = true;
    }

    public Optional<SculkTransporterNetwork> getNetwork(BlockPos blockPos) {
        if (dirty) {
            nodeNetworks.clear();
            dirty = false;
        }
        SculkTransporterNetwork network = nodeNetworks.get(blockPos);
        if (network == null && isTransportNode(blockPos)) {
            network = rebuild(blockPos);
        }
        return Optional.ofNullable(network);
    }

    public void rebuildIfNeeded() {
        if (dirty) {
            nodeNetworks.clear();
            dirty = false;
        }
    }

    public SculkTransporterNetwork rebuild(BlockPos origin) {
        SculkTransporterNetwork existing = nodeNetworks.get(origin);
        if (existing != null) {
            removeNetwork(existing);
        }
        SculkTransporterNetwork network = new SculkTransporterNetwork();
        if (!isTransportNode(origin)) {
            return network;
        }
        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(origin.immutable());
        visited.add(origin.immutable());
        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.remove();
            SculkTransporterNode node = createNode(currentPos);
            if (node == null) {
                continue;
            }
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(direction);
                if (!isTransportNode(neighborPos) || !canConnect(currentPos, direction) || !canConnect(neighborPos, direction.getOpposite())) {
                    continue;
                }
                node.addConnection(direction);
                if (visited.add(neighborPos)) {
                    queue.add(neighborPos);
                }
            }
            network.addNode(node);
        }
        network.rebuildRouting(serverLevel);
        for (SculkTransporterNode node : network.getNodeValues()) {
            nodeNetworks.put(node.getPos(), network);
        }
        return network;
    }

    private void removeNetwork(SculkTransporterNetwork network) {
        nodeNetworks.values().removeIf(value -> value == network);
    }

    private SculkTransporterNode createNode(BlockPos blockPos) {
        BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
        if (blockEntity instanceof SculkTransporterBlockEntity transporter) {
            return new SculkTransporterNode(transporter.getBlockPos(), SculkTransporterNodeType.TRANSPORTER);
        }
        if (blockEntity instanceof TunedSculkJawBlockEntity tunedSculkJaw) {
            TunedSculkJawIOState ioState = tunedSculkJaw.getBlockState().getValue(TunedSculkJawBlock.IO_STATE);
            return new SculkTransporterNode(
                    tunedSculkJaw.getBlockPos(),
                    ioState == TunedSculkJawIOState.INPUT ? SculkTransporterNodeType.INPUT : SculkTransporterNodeType.OUTPUT
            );
        }
        return null;
    }

    private boolean isTransportNode(BlockPos blockPos) {
        BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
        return blockEntity instanceof SculkTransporterBlockEntity || blockEntity instanceof TunedSculkJawBlockEntity;
    }

    private boolean canConnect(BlockPos blockPos, Direction direction) {
        BlockEntity blockEntity = serverLevel.getBlockEntity(blockPos);
        BlockEntity neighbor = serverLevel.getBlockEntity(blockPos.relative(direction));
        if (blockEntity instanceof TunedSculkJawBlockEntity tunedSculkJaw) {
            return neighbor instanceof SculkTransporterBlockEntity && direction != tunedSculkJaw.getBlockState().getValue(TunedSculkJawBlock.FACING);
        }
        if (blockEntity instanceof SculkTransporterBlockEntity) {
            return neighbor instanceof SculkTransporterBlockEntity || neighbor instanceof TunedSculkJawBlockEntity;
        }
        return false;
    }
}
