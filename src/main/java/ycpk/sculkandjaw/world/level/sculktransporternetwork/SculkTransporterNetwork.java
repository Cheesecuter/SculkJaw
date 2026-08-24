package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import ycpk.sculkandjaw.blocks.blockentities.TunedSculkJawBlockEntity;
import ycpk.sculkandjaw.world.level.sculktransporternetwork.nodes.SculkTransporterNode;
import ycpk.sculkandjaw.world.level.sculktransporternetwork.nodes.SculkTransporterNodeType;

import java.util.*;

public class SculkTransporterNetwork {
    private final Set<BlockPos> nodes = new HashSet<>();
    private final Set<BlockPos> inputs = new HashSet<>();
    private final Set<BlockPos> outputs = new HashSet<>();
    private final Map<BlockPos, SculkTransporterNode> nodeMap = new HashMap<>();
    private final Map<BlockPos, BlockPos> nextHopMap = new HashMap<>();
    private final Map<BlockPos, Integer> outputPriorities = new HashMap<>();
    private boolean dirty = true;

    public SculkTransporterNetwork() {
    }

    public void clear() {
        nodes.clear();
        inputs.clear();
        outputs.clear();
        nodeMap.clear();
        nextHopMap.clear();
        outputPriorities.clear();
        dirty = true;
    }

    public void addNode(SculkTransporterNode node) {
        BlockPos blockPos = node.getPos();
        nodes.add(blockPos);
        nodeMap.put(blockPos, node);
        if (node.getType() == SculkTransporterNodeType.INPUT) {
            inputs.add(blockPos);
        }
        else if (node.getType() == SculkTransporterNodeType.OUTPUT) {
            outputs.add(blockPos);
            outputPriorities.putIfAbsent(blockPos, 0);
        }
        dirty = true;
    }

    public void addOutput(BlockPos blockPos, int priority) {
        outputs.add(blockPos);
        outputPriorities.put(blockPos, priority);
        dirty = true;
    }

    public boolean contains(BlockPos blockPos) {
        return nodes.contains(blockPos);
    }

    public Set<BlockPos> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public Set<BlockPos> getInputs() {
        return Collections.unmodifiableSet(inputs);
    }

    public Set<BlockPos> getOutputs() {
        return Collections.unmodifiableSet(outputs);
    }

    public void markDirty() {
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public Collection<SculkTransporterNode> getNodeValues() {
        return Collections.unmodifiableCollection(nodeMap.values());
    }

    public List<SculkTransporterNode> getConnectedNodes(SculkTransporterNode node) {
        if (node == null) {
            return Collections.emptyList();
        }
        List<SculkTransporterNode> result = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (!node.hasConnection(direction)) {
                continue;
            }
            BlockPos neighborPos = node.getPos().relative(direction);
            SculkTransporterNode neighbor = nodeMap.get(neighborPos);
            if (neighbor != null && neighbor.hasConnection(direction.getOpposite())) {
                result.add(neighbor);
            }
        }
        return result;
    }

    public Optional<BlockPos> getNextHop(Level level, BlockPos currentPos, ItemStack itemStack) {
        //rebuildRouting(level);
        List<BlockPos> nextHops = getNextHops(level, currentPos, itemStack);
        return nextHops.isEmpty() ? Optional.empty() : Optional.of(nextHops.getFirst());
    }

    public Optional<BlockPos> getNextHop(BlockPos currentPos) {
        return Optional.ofNullable(nextHopMap.get(currentPos));
    }

    public List<BlockPos> getNextHops(Level level, BlockPos currentPos, ItemStack itemStack) {
        rebuildRouting(level);
        SculkTransporterNode source = nodeMap.get(currentPos);
        if (source == null || itemStack.isEmpty()) {
            return Collections.emptyList();
        }
        Map<BlockPos, RouteCandidate> candidates = new HashMap<>();
        Queue<RouteSearchNode> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(new RouteSearchNode(source.getPos(), null, 0));
        visited.add(source.getPos());
        while (!queue.isEmpty()) {
            RouteSearchNode current = queue.remove();
            SculkTransporterNode currentNode = nodeMap.get(current.blockPos());
            if (currentNode == null) {
                continue;
            }
            if (!current.blockPos().equals(currentPos) && currentNode.getType() == SculkTransporterNodeType.OUTPUT) {
                if (canOutputAccept(level, current.blockPos(), itemStack)) {
                    candidates.putIfAbsent(current.blockPos(), new RouteCandidate(
                            current.firstStep(),
                            current.distance(),
                            outputPriorities.getOrDefault(current.blockPos(), 0))
                    );
                }
                continue;
            }
            if (!current.blockPos().equals(currentPos) && currentNode.getType() == SculkTransporterNodeType.INPUT) {
                continue;
            }
            for (SculkTransporterNode neighbor : getConnectedNodes(currentNode)) {
                if (visited.add(neighbor.getPos())) {
                    BlockPos firstStep = current.firstStep() == null ? neighbor.getPos() : current.firstStep();
                    queue.add(new RouteSearchNode(neighbor.getPos(), firstStep, current.distance() + 1));
                }
            }
        }
        List<RouteCandidate> sorted = new ArrayList<>(candidates.values());
        sorted.sort(
                Comparator.comparingInt(RouteCandidate::priority).reversed()
                        .thenComparingInt(RouteCandidate::distance)
                        .thenComparingInt(candidate -> candidate.firstStep().getX())
                        .thenComparingInt(candidate -> candidate.firstStep().getY())
                        .thenComparingInt(candidate -> candidate.firstStep().getZ())
        );
        Set<BlockPos> firstSteps = new LinkedHashSet<>();
        for (RouteCandidate candidate : sorted) {
            if (candidate.firstStep() != null) {
                firstSteps.add(candidate.firstStep());
            }
        }
        return new ArrayList<>(firstSteps);
    }

    private static boolean canOutputAccept(Level level, BlockPos blockPos, ItemStack itemStack) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        return blockEntity instanceof TunedSculkJawBlockEntity tunedSculkJaw && tunedSculkJaw.acceptsItem(itemStack);
    }

    public void rebuildRouting(Level level) {
        if (!dirty && !nextHopMap.isEmpty()) {
            return;
        }
        nextHopMap.clear();
        if (outputs.isEmpty()) {
            dirty = false;
            return;
        }
        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        List<BlockPos> sortedOutputs = new ArrayList<>(outputs);
        sortedOutputs.sort(
                Comparator.comparingInt((BlockPos pos) -> outputPriorities.getOrDefault(pos, 0)).reversed()
                        .thenComparingInt(BlockPos::getX)
                        .thenComparingInt(BlockPos::getY)
                        .thenComparingInt(BlockPos::getZ)
        );
        for (BlockPos output : sortedOutputs) {
            if (visited.add(output)) {
                queue.add(output);
            }
        }
        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.remove();
            SculkTransporterNode currentNode = nodeMap.get(currentPos);
            if (currentNode == null) {
                continue;
            }
            if (currentNode.getType() == SculkTransporterNodeType.INPUT) {
                continue;
            }
            for (SculkTransporterNode neighbor : getConnectedNodes(currentNode)) {
                if (visited.add(neighbor.getPos())) {
                    nextHopMap.put(neighbor.getPos(), currentPos);
                    queue.add(neighbor.getPos());
                }
            }
        }
        dirty = false;
    }

    private void rebuildRoutingIfNeeded(Level level) {
        if (dirty) {
            rebuildRouting(level);
        }
    }

    private record RouteSearchNode(BlockPos blockPos, BlockPos firstStep, int distance) {
    }

    private record RouteCandidate(BlockPos firstStep, int distance, int priority) {
    }
}

