package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;
import ycpk.sculkandjaw.world.level.sculktransporternetwork.nodes.SculkTransporterNode;
import ycpk.sculkandjaw.world.level.sculktransporternetwork.nodes.SculkTransporterNodeType;

import java.util.*;

public class SculkTransporterRouter {
    private final SculkTransporterNetwork network;

    public SculkTransporterRouter(SculkTransporterNetwork network) {
        this.network = network;
    }

    public SculkTransporterNode findNextNode(SculkTransporterNode source) {
        if (source == null) {
            return null;
        }
        Set<BlockPos> visited = new HashSet<>();
        Queue<RouteSearchNode> queue = new ArrayDeque<>();
        queue.add(new RouteSearchNode(source, null, null, 0));
        visited.add(source.getPos());
        while (!queue.isEmpty()) {
            RouteSearchNode current = queue.poll();
            SculkTransporterNode currentNode = current.node();
            if (currentNode != source && currentNode.getType() == SculkTransporterNodeType.OUTPUT) {
                return current.firstStep();
            }
            for (SculkTransporterNode neighbor : network.getConnectedNodes(currentNode)) {
                if (neighbor == null) {
                    continue;
                }
                BlockPos neighborPos = neighbor.getPos();
                if (visited.contains(neighborPos)) {
                    continue;
                }
                visited.add(neighborPos);
                SculkTransporterNode firstStep = current.firstStep() == null ? neighbor : current.firstStep();
                queue.add(new RouteSearchNode(neighbor, firstStep, current, current.distance() + 1));
            }
        }
        return null;
    }

    public List<SculkTransporterNode> findRoute(SculkTransporterNode source) {
        if (source == null) {
            return Collections.emptyList();
        }
        Map<BlockPos, RouteSearchNode> searched = new HashMap<>();
        Queue<RouteSearchNode> queue = new ArrayDeque<>();
        RouteSearchNode start = new RouteSearchNode(source, null, null, 0);
        queue.add(start);
        searched.put(source.getPos(), start);
        RouteSearchNode destination = null;
        while (!queue.isEmpty()) {
            RouteSearchNode current = queue.poll();
            if (current.node() != source && current.node().getType() == SculkTransporterNodeType.OUTPUT) {
                destination = current;
                break;
            }
            for (SculkTransporterNode neighbor : network.getConnectedNodes(current.node())) {
                if (neighbor == null) {
                    continue;
                }
                if (searched.containsKey(neighbor.getPos())) {
                    continue;
                }
                RouteSearchNode next = new RouteSearchNode(neighbor, null, current, current.distance() + 1);
                searched.put(neighbor.getPos(), next);
                queue.add(next);
            }
        }
        if (destination == null) {
            return Collections.emptyList();
        }
        List<SculkTransporterNode> route = new ArrayList<>();
        RouteSearchNode current = destination;
        while (current != null) {
            route.add(current.node());
            current = current.parent();
        }
        Collections.reverse(route);
        return route;
    }

    private record RouteSearchNode(SculkTransporterNode node, SculkTransporterNode firstStep, RouteSearchNode parent, int distance) {
    }
}
