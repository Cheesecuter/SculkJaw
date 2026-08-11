package ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.blockentities.SculkTransporterBlockEntity;

import java.util.Map;

public class SculkTransporterBlock extends BaseEntityBlock {
    public static final MapCodec<SculkTransporterBlock> CODEC = simpleCodec(SculkTransporterBlock::new);
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
    public static final VoxelShape COLLISION_SHAPE = Shapes.or(
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0),
            Block.box(1.0, 1.0, 2.0, 15.0, 15.0, 14.0),
            Block.box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0)
    );
    public static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(COLLISION_SHAPE);

    public SculkTransporterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst()
                .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SculkTransporterBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getClickedFace());
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType type) {
        return false;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape) SHAPES.get(((Direction) blockState.getValue(FACING)));
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return (VoxelShape) SHAPES.get(((Direction) blockState.getValue(FACING)));
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState) {
        return (VoxelShape) SHAPES.get(((Direction) blockState.getValue(FACING)));
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return (VoxelShape) SHAPES.get(((Direction) blockState.getValue(FACING)));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape) SHAPES.get(((Direction) blockState.getValue(FACING)));
    }

    @Override
    protected void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        if (bl) {
            this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(5));
        }
    }
}
