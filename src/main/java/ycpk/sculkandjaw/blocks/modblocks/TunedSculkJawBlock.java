package ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.blocks.blockentities.TunedSculkJawBlockEntity;
import ycpk.sculkandjaw.world.level.block.state.properties.ModBlockStateProperties;
import ycpk.sculkandjaw.world.level.block.state.properties.SculkJawBiteState;

import java.util.Map;

public class TunedSculkJawBlock extends BaseEntityBlock {
    public static final MapCodec<TunedSculkJawBlock> CODEC = simpleCodec(TunedSculkJawBlock::new);
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
    /*public static final BooleanProperty START_BITE = SculkJawBlock.START_BITE;
    public static final BooleanProperty BITE = SculkJawBlock.BITE;
    public static final BooleanProperty STOP_BITE = SculkJawBlock.STOP_BITE;*/
    public static final EnumProperty<SculkJawBiteState> BITE_STATE = ModBlockStateProperties.BITE_STATE;
    public static final VoxelShape COLLISION_SHAPE_OPEN = Shapes.join(
            Block.box(0.0, 0.0, 0.0, 16.0,  16.0, 16.0),
            Block.box(1.0, 1.0, 0.0, 15.0, 15.0, 8.0),
            BooleanOp.ONLY_FIRST
    );
    public static final VoxelShape INSIDE_COLLISION_SHAPE = Block.box(1.0, 1.0, 1.0, 15.0, 15.0, 32.0);
    public static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(COLLISION_SHAPE_OPEN);
    public static final Map<Direction, VoxelShape> INSIDE_SHAPES = Shapes.rotateAll(INSIDE_COLLISION_SHAPE);

    public TunedSculkJawBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst()
                .setValue(FACING, Direction.NORTH)
                .setValue(BITE_STATE, SculkJawBiteState.NOT_BITE)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TunedSculkJawBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, BITE_STATE});
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
        return Shapes.block();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return (VoxelShape) INSIDE_SHAPES.get(((Direction) blockState.getValue(FACING)));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape) SHAPES.get(((Direction) blockState.getValue(FACING)));
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity,
                             InsideBlockEffectApplier insideBlockEffectApplier, boolean bl) {
        BlockPos entityPos = entity.blockPosition();
        SculkAndJaw.LOGGER.info("Tuned Sculk Jaw at pos " + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ()
        + " detected " + entity.getName().getString() + " at " + entityPos.getX() + ", " + entityPos.getY() + ", " + entityPos.getZ());
    }

    @Override
    protected void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        if (bl) {
            this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(5));
        }
    }
}
