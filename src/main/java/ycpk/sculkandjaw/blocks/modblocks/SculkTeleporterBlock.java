package ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.blockentities.SculkTeleporterBlockEntity;
import ycpk.sculkandjaw.registry.ModBlocks;

public class SculkTeleporterBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<SculkTeleporterBlock> CODEC = simpleCodec(SculkTeleporterBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final VoxelShape COLLISION_SHAPE = Shapes.or(
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(3.0, 8.0, 3.0, 13.0, 16.0, 13.0)
    );

    public SculkTeleporterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst()
                .setValue(WATERLOGGED, false)
                .setValue(POWERED, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SculkTeleporterBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{WATERLOGGED, POWERED});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Level level = blockPlaceContext.getLevel();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        return (BlockState) this.defaultBlockState()
                .setValue(WATERLOGGED, level.getFluidState(blockPos).getType() == Fluids.WATER)
                .setValue(POWERED, level.hasNeighborSignal(blockPos));
    }

    protected FluidState getFluidState(BlockState blockState) {
        return (Boolean) blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape) COLLISION_SHAPE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return (VoxelShape) COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState) {
        return (VoxelShape) COLLISION_SHAPE;
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return (VoxelShape) COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape) COLLISION_SHAPE;
    }

    @Override
    protected void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        if (bl) {
            this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(5));
        }
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(blockState, level, blockPos, block, orientation, movedByPiston);
        if (!level.isClientSide()) {
            boolean powered = level.hasNeighborSignal(blockPos);
            if (powered != blockState.getValue(POWERED)) {
                level.setBlock(blockPos, blockState.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        super.stepOn(level, blockPos, blockState, entity);
        if (!blockState.getValue(POWERED) || level.isClientSide() || entity.isSpectator() || entity.isOnPortalCooldown()) {
            return;
        }
        if (!(level instanceof ServerLevel sourceLevel)) {
            return;
        }
        if (!(sourceLevel.getBlockEntity(blockPos) instanceof SculkTeleporterBlockEntity teleporter)) {
            return;
        }

        BlockPos destinationPos = teleporter.getDestinationPos();
        var destinationDimension = teleporter.getDestinationDimension();
        if (destinationPos == null || destinationDimension == null) {
            return;
        }

        ServerLevel destinationLevel = sourceLevel.getServer().getLevel(destinationDimension);
        if (destinationLevel == null || !destinationLevel.getBlockState(destinationPos).is(ModBlocks.SCULK_TELEPORTER)) {
            return;
        }

        Vec3 destinationCenter = destinationPos.above().getCenter();
        Entity teleportedEntity = entity.teleport(new TeleportTransition(
                destinationLevel,
                destinationCenter,
                entity.getDeltaMovement(),
                entity.getYRot(),
                entity.getXRot(),
                TeleportTransition.DO_NOTHING
        ));
        destinationLevel.playSound((Entity)null, destinationPos.getX(), destinationPos.getY(), destinationPos.getZ(), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
        if (teleportedEntity != null) {
            teleportedEntity.setPortalCooldown(20);
        }
    }
}
