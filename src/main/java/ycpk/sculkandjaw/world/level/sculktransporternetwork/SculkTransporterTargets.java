package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import ycpk.sculkandjaw.SculkAndJaw;

import java.util.*;

public final class SculkTransporterTargets {
    public static void registerSculkTransporterTargets() {
        SculkAndJaw.LOGGER.info("Registering Sculk Transporter Targets for Mod " + SculkAndJaw.MOD_ID);
        registerDefault();
    }

    private static final List<TargetProvider> PROVIDERS = new ArrayList<>();

    private SculkTransporterTargets() {
    }

    public static void register(TargetProvider provider) {
        PROVIDERS.add(provider);
    }

    public static Optional<SculkTransporterTarget> findTarget(Level level, BlockPos blockPos, Direction direction) {
        for (TargetProvider provider : PROVIDERS) {
            Optional<SculkTransporterTarget> target = provider.createTarget(level, blockPos, direction);
            if (target.isPresent()) {
                return target;
            }
        }
        return Optional.empty();
    }

    public static void registerDefault() {
        register((level, blockPos, direction) -> {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof Container container) {
                return Optional.of(new ContainerSculkTransportTarget(container, blockPos, direction));
            }
            return Optional.empty();
        });
    }
}
