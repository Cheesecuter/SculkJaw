package ycpk.sculkandjaw.blocks.modblocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.blockentities.SculkTransporterBlockEntity;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.registry.ModBlocks;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class SculkTransporterBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<SculkTransporterBlock> CODEC = simpleCodec(SculkTransporterBlock::new);
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(
            Maps.newEnumMap(
                    Map.of(
                            Direction.NORTH, NORTH,
                            Direction.EAST, EAST,
                            Direction.SOUTH, SOUTH,
                            Direction.WEST, WEST,
                            Direction.UP, UP,
                            Direction.DOWN, DOWN
                    )
            )
    );
    private final Function<BlockState, VoxelShape> shapes;

    public SculkTransporterBlock(Properties properties) {
        super(properties);
        this.shapes = this.makeShapes(12.0F);
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(WATERLOGGED, false)
        );
    }

    protected RenderShape getRenderShape(BlockState blockState) {
        //return RenderShape.INVISIBLE;
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SculkTransporterBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, ModBlockEntities.SCULK_TRANSPORTER_BLOCK_ENTITY, SculkTransporterBlockEntity::serverTick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return getStateWithConnections(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos(), this.defaultBlockState())
                .setValue(WATERLOGGED, blockPlaceContext.getLevel().getFluidState(blockPlaceContext.getClickedPos()).getType() == Fluids.WATER);
    }

    private static BlockState getStateWithConnections(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        BlockState blockState2 = blockGetter.getBlockState(blockPos.below());
        BlockState blockState3 = blockGetter.getBlockState(blockPos.above());
        BlockState blockState4 = blockGetter.getBlockState(blockPos.north());
        BlockState blockState5 = blockGetter.getBlockState(blockPos.east());
        BlockState blockState6 = blockGetter.getBlockState(blockPos.south());
        BlockState blockState7 = blockGetter.getBlockState(blockPos.west());
        Block sculk_transporter = ModBlocks.SCULK_TRANSPORTER;
        Block tuned_sculk_jaw = ModBlocks.TUNED_SCULK_JAW;
        Block sculk = Blocks.SCULK;
        return (BlockState)(
                (BlockState)(
                        (BlockState)(
                                (BlockState)(
                                        (BlockState)(
                                                (BlockState) blockState.trySetValue(DOWN, blockState2.is(sculk_transporter) || blockState2.is(tuned_sculk_jaw) || blockState2.is(sculk))
                                        ).trySetValue(UP, blockState3.is(sculk_transporter) || blockState3.is(tuned_sculk_jaw) || blockState3.is(sculk))
                                ).trySetValue(NORTH, blockState4.is(sculk_transporter) || blockState4.is(tuned_sculk_jaw) || blockState4.is(sculk))
                        ).trySetValue(EAST, blockState5.is(sculk_transporter) || blockState5.is(tuned_sculk_jaw) || blockState5.is(sculk))
                ).trySetValue(SOUTH, blockState6.is(sculk_transporter) || blockState6.is(tuned_sculk_jaw) || blockState6.is(sculk))
        ).trySetValue(WEST, blockState7.is(sculk_transporter) || blockState7.is(tuned_sculk_jaw) || blockState7.is(sculk));
    }

    protected FluidState getFluidState(BlockState blockState) {
        return (Boolean) blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType type) {
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape) this.shapes.apply(blockState);
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return (VoxelShape) this.shapes.apply(blockState);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState) {
        return (VoxelShape) this.shapes.apply(blockState);
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return (VoxelShape) this.shapes.apply(blockState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape) this.shapes.apply(blockState);
    }

    @Override
    protected void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        if (bl) {
            this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(5));
        }
    }

    @Override
    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        boolean bl = blockState2.is(ModBlocks.SCULK_TRANSPORTER) || blockState2.is(ModBlocks.TUNED_SCULK_JAW) || blockState2.is(Blocks.SCULK);
        return (BlockState)blockState.setValue((Property)PROPERTY_BY_DIRECTION.get(direction), bl);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity var7 = level.getBlockEntity(blockPos);
            if (var7 instanceof SculkTransporterBlockEntity) {
                SculkTransporterBlockEntity sculkTransporterBlockEntity = (SculkTransporterBlockEntity)var7;
                player.openMenu(sculkTransporterBlockEntity);
            }
        }

        return InteractionResult.SUCCESS;
    }

    private Function<BlockState, VoxelShape> makeShapes(float f) {
        VoxelShape voxelShape = Block.cube((double) f);
        Map<Direction, VoxelShape> map = Shapes.rotateAll(Block.boxZ((double) f, 0.0, 8.0));
        return this.getShapeForEachState((blockState) -> {
            VoxelShape voxelShape2 = voxelShape;
            Iterator var = PROPERTY_BY_DIRECTION.entrySet().iterator();
            while (var.hasNext()) {
                Map.Entry<Direction, BooleanProperty> entry = (Map.Entry) var.next();
                if ((Boolean) blockState.getValue((Property) entry.getValue())) {
                    voxelShape2 = Shapes.or((VoxelShape) map.get(entry.getKey()), voxelShape2);
                }
            }
            return voxelShape2;
        });
    }
}
