package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModBlockEntities;

import java.util.*;

public final class SculkTransporterTargets {
    public static void registerSculkTransporterTargets() {
        SculkAndJaw.LOGGER.info("Registering Sculk Transporter Targets for Mod " + SculkAndJaw.MOD_ID);
        register(
                ModBlockEntities.SCULK_TRANSPORTER_BLOCK_ENTITY,
                ((level, blockPos, direction, blockEntity) ->
                        new ContainerSculkTransportTarget((Container) blockEntity))
        );
        register(ModBlockEntities.TUNED_SCULK_JAW_BLOCK_ENTITY,
                (((level, blockPos, direction, blockEntity) ->
                        new ContainerSculkTransportTarget((Container) blockEntity)))
        );
    }

    private static final Map<BlockEntityType<?>, TargetProvider> TARGETS = new HashMap<>();

    private SculkTransporterTargets() {
    }

    public static <T extends BlockEntity> void register(BlockEntityType<T> blockEntityType,TargetProvider provider) {
        TARGETS.put(blockEntityType, provider);
    }

    public static Optional<SculkTransporterTarget> findTarget(Level level, BlockPos blockPos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity == null) {
            return Optional.empty();
        }
        TargetProvider provider = TARGETS.get(blockEntity.getType());
        if (provider == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(provider.create(level, blockPos, direction, blockEntity));
    }
}
