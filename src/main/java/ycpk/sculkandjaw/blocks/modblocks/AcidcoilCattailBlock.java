package ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ycpk.sculkandjaw.level.storage.loot.ModBuiltInLootTables;
import ycpk.sculkandjaw.registry.ModBlocks;

import java.util.function.Function;

public class AcidcoilCattailBlock extends VegetationBlock implements BonemealableBlock, SegmentableBlock {
    public static final MapCodec<AcidcoilCattailBlock> CODEC = simpleCodec(AcidcoilCattailBlock::new);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AMOUNT = BlockStateProperties.FLOWER_AMOUNT;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
    private final Function<BlockState, VoxelShape> shapes;

    public AcidcoilCattailBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                (BlockState) (
                        (BlockState) (
                                (BlockState) (
                                        (BlockState) (
                                                (BlockState) this.getStateDefinition().any()
                                        ).setValue(FACING, Direction.NORTH)
                                ).setValue(AMOUNT, 1)
                        ).setValue(HALF, DoubleBlockHalf.LOWER)
                ).setValue(AGE, 0)
        );
        this.shapes = this.makeShapes();
    }

    @Override
    public MapCodec<AcidcoilCattailBlock> codec() {
        return CODEC;
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, AMOUNT, HALF, AGE});
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        Level level = blockPlaceContext.getLevel();
        return blockPos.getY() < level.getMaxY() && level.getBlockState(blockPos.above()).canBeReplaced(blockPlaceContext) ? this.getStateForPlacement(blockPlaceContext, this, AMOUNT, FACING).setValue(HALF, DoubleBlockHalf.LOWER).setValue(AGE, 0) : null;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue(FACING, rotation.rotate((Direction)blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation((Direction)blockState.getValue(FACING)));
    }

    @Override
    public BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf)blockState.getValue(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP) && (!blockState2.is(this) || blockState2.getValue(HALF) == doubleBlockHalf)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canSurvive(levelReader, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
        }
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return this.canBeReplaced(blockState, blockPlaceContext, AMOUNT) ? true : super.canBeReplaced(blockState, blockPlaceContext);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape)this.shapes.apply(blockState);
    }

    @Override
    public double getShapeHeight() {
        return 16.0;
    }

    @Override
    public IntegerProperty getSegmentAmountProperty() {
        return AMOUNT;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        BlockPos blockPos2 = blockPos.above();
        level.setBlock(blockPos2, copyWaterloggedFrom(level, blockPos2, (BlockState)this.defaultBlockState().setValue(FACING, blockState.getValue(FACING)).setValue(AMOUNT, blockState.getValue(AMOUNT)).setValue(HALF, DoubleBlockHalf.UPPER)), 3);
    }

    @Override
    public boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.is(Blocks.SCULK) || blockState.is(Blocks.SCULK_CATALYST) || blockState.is(ModBlocks.SCULK_AGGREGATOR);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        if (blockState.getValue(HALF) != DoubleBlockHalf.UPPER) {
            BlockPos blockPos2 = blockPos.below();
            return super.canSurvive(blockState, levelReader, blockPos) && this.mayPlaceOn(levelReader.getBlockState(blockPos2), levelReader, blockPos2);
        } else {
            BlockState blockState2 = levelReader.getBlockState(blockPos.below());
            return blockState2.is(this) && blockState2.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    public static void placeAt(LevelAccessor levelAccessor, BlockState blockState, BlockPos blockPos, int i) {
        BlockPos blockPos2 = blockPos.above();
        levelAccessor.setBlock(blockPos, copyWaterloggedFrom(levelAccessor, blockPos, (BlockState)blockState.setValue(HALF, DoubleBlockHalf.LOWER)), i);
        levelAccessor.setBlock(blockPos2, copyWaterloggedFrom(levelAccessor, blockPos2, (BlockState)blockState.setValue(HALF, DoubleBlockHalf.UPPER)), i);
    }

    public static BlockState copyWaterloggedFrom(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? (BlockState)blockState.setValue(BlockStateProperties.WATERLOGGED, levelReader.isWaterAt(blockPos)) : blockState;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (!level.isClientSide()) {
            if (player.preventsBlockDrops()) {
                preventDropFromBottomPart(level, blockPos, blockState, player);
            } else {
                dropResources(blockState, level, blockPos, (BlockEntity)null, player, player.getMainHandItem());
            }
        }

        return super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, Blocks.AIR.defaultBlockState(), blockEntity, itemStack);
    }

    protected static void preventDropFromBottomPart(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf) blockState.getValue(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
            BlockPos blockPos2 = blockPos.below();
            BlockState blockState2 = level.getBlockState(blockPos2);
            if (blockState2.is(blockState.getBlock()) && blockState2.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState3 = blockState2.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level.setBlock(blockPos2, blockState3, 35);
                level.levelEvent(player, 2001, blockPos2, Block.getId(blockState2));
            }
        }

    }

    @Override
    public long getSeed(BlockState blockState, BlockPos blockPos) {
        return Mth.getSeed(blockPos.getX(), blockPos.below(blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), blockPos.getZ());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        int i = (Integer)blockState.getValue(AMOUNT);
        int age = (Integer) blockState.getValue(AGE);
        if (i < 4) {
            if (blockState.getValue(HALF).equals(DoubleBlockHalf.LOWER)) {
                age = serverLevel.getBlockState(blockPos.above()).getValue(AGE);
                serverLevel.setBlock(blockPos, (BlockState) blockState.setValue(AMOUNT, i + 1), 2);
                serverLevel.setBlock(blockPos.above(), (BlockState) blockState.setValue(AMOUNT, i + 1).setValue(HALF, DoubleBlockHalf.UPPER).setValue(AGE, age), 2);
            }
            else {
                if (age == i) {
                    popResource(serverLevel, blockPos, new ItemStack(this));
                }
                else {
                    serverLevel.setBlock(blockPos, (BlockState) blockState.setValue(AGE, Math.min(++age, i)), 2);
                }
            }
        }
        else {
            if (blockState.getValue(HALF).equals(DoubleBlockHalf.LOWER)) {
                popResource(serverLevel, blockPos, new ItemStack(this));
            }
            else {
                if (age == 4) {
                    popResource(serverLevel, blockPos, new ItemStack(this));
                }
                else {
                    serverLevel.setBlock(blockPos, (BlockState) blockState.setValue(AGE, Math.min(++age, i)), 2);
                }
            }
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if ((Integer) blockState.getValue(AGE) > 0) {
            if (level instanceof ServerLevel serverLevel) {
                int age = (Integer) blockState.getValue(AGE);
                if (blockState.getValue(HALF).equals(DoubleBlockHalf.UPPER)) {
                    Block.dropFromBlockInteractLootTable(serverLevel, ModBuiltInLootTables.HARVEST_ACIDCOIL_CATTAIL, blockState, level.getBlockEntity(blockPos), (ItemStack)null, player, (serverLevelx, itemStack) -> {
                        Block.popResource(serverLevelx, blockPos, itemStack);
                    });
                    serverLevel.playSound((Entity)null, blockPos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + serverLevel.random.nextFloat() * 0.4F);
                    BlockState blockState1 = (BlockState) blockState.setValue(AGE, --age);
                    serverLevel.setBlock(blockPos, blockState1, 2);
                    serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState1));
                }
            }
            return InteractionResult.SUCCESS;
        }
        else {
            return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult);
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState blockState) {
        return (Integer) blockState.getValue(AGE) < 4 && blockState.getValue(HALF).equals(DoubleBlockHalf.UPPER);
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        int i = (Integer) blockState.getValue(AGE);
        if (i < 4 && randomSource.nextInt(6) == 0) {
            BlockState blockState2 = (BlockState) blockState.setValue(AGE, i + 1);
            serverLevel.setBlock(blockPos, blockState2, Block.UPDATE_CLIENTS);
            serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState2));
        }
    }

    private Function<BlockState, VoxelShape> makeShapes() {
        return this.getShapeForEachState(this.getShapeCalculator(FACING, AMOUNT));
    }
}
