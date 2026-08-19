package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.WeakHashMap;

public final class SculkTransporterNetworkManagerProvider {
    private static final Map<ServerLevel, SculkTransporterNetworkManager> MANAGERS = new WeakHashMap<>();

    private SculkTransporterNetworkManagerProvider() {
    }

    public static SculkTransporterNetworkManager get(ServerLevel serverLevel) {
        return MANAGERS.computeIfAbsent(serverLevel, SculkTransporterNetworkManager::new);
    }

    public static void remove(ServerLevel serverLevel) {
        MANAGERS.remove(serverLevel);
    }
}
