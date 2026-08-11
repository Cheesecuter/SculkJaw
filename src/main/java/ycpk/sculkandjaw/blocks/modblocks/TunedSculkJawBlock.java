package ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.blockentities.TunedSculkJawBlockEntity;
import ycpk.sculkandjaw.registry.ModTags;

public class TunedSculkJawBlock extends BaseEntityBlock {
    public static final MapCodec<TunedSculkJawBlock> CODEC = simpleCodec(TunedSculkJawBlock::new);
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
    public static final BooleanProperty START_BITE = SculkJawBlock.START_BITE;
    public static final BooleanProperty BITE = SculkJawBlock.BITE;
    public static final BooleanProperty STOP_BITE = SculkJawBlock.STOP_BITE;
    public static final VoxelShape COLLISION_SHAPE_OPEN = Shapes.join(
            Block.box(0.0, 0.0, 0.0, 16.0,  16.0, 16.0),
            Block.box(1.0, 1.0, 1.0, 15.0, 16.0, 15.0),
            BooleanOp.ONLY_FIRST
    );
    public static final VoxelShape COLLISION_SHAPE_CLOSE = Shapes.join(
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(1.0, 1.0, 1.0, 15.0, 15.0, 15.0),
            BooleanOp.ONLY_FIRST
    );
    public static final VoxelShape INSIDE_COLLISION_SHAPE = Block.box(1.0, 1.0, 1.0, 15.0, 14.0, 15.0);

    public TunedSculkJawBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst()
                .setValue(FACING, Direction.NORTH)
                .setValue(START_BITE, false)
                .setValue(BITE, false)
                .setValue(STOP_BITE, false)
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
        builder.add(new Property[]{FACING, START_BITE, BITE, STOP_BITE});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getClickedFace());
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return COLLISION_SHAPE_CLOSE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return COLLISION_SHAPE_CLOSE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return INSIDE_COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if(collisionContext instanceof EntityCollisionContext) {
            Entity entity = ((EntityCollisionContext) collisionContext).getEntity();
            if(entity != null) {
                if(entity.getType().is(ModTags.IMMUNE_TO_SCULK_JAW)){
                    return COLLISION_SHAPE_CLOSE;
                }
                else if(entity.isShiftKeyDown() && entity.distanceToSqr(blockPos.getCenter().add(0, 0.5, 0)) > 0.2){
                    return COLLISION_SHAPE_CLOSE;
                }
            }
        }
        if(blockState.getValue(START_BITE) || blockState.getValue(BITE) || blockState.getValue(STOP_BITE)) {
            return COLLISION_SHAPE_OPEN;
        }
        return COLLISION_SHAPE_CLOSE;
    }
}
