package ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.blockentities.TunedSculkJawBlockEntity;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.registry.ModItems;
import ycpk.sculkandjaw.world.level.block.state.properties.ModBlockStateProperties;
import ycpk.sculkandjaw.world.level.block.state.properties.SculkJawBiteState;
import ycpk.sculkandjaw.world.level.block.state.properties.TunedSculkJawIOState;
import ycpk.sculkandjaw.world.level.sculktransporternetwork.SculkTransporterNetworkManagerProvider;

import java.util.Map;

public class TunedSculkJawBlock extends BaseEntityBlock {
    public static final MapCodec<TunedSculkJawBlock> CODEC = simpleCodec(TunedSculkJawBlock::new);
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
    public static final EnumProperty<SculkJawBiteState> BITE_STATE = ModBlockStateProperties.BITE_STATE;
    public static final EnumProperty<TunedSculkJawIOState> IO_STATE = ModBlockStateProperties.IO_STATE;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
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
                .setValue(IO_STATE, TunedSculkJawIOState.INPUT)
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
        return new TunedSculkJawBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide()
                ? null
                : createTickerHelper(blockEntityType, ModBlockEntities.TUNED_SCULK_JAW_BLOCK_ENTITY, TunedSculkJawBlockEntity::serverTick);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(blockState, level, blockPos, oldState, movedByPiston);
        if (level instanceof ServerLevel serverLevel) {
            SculkTransporterNetworkManagerProvider.get(serverLevel).markDirty();
            if (oldState.getBlock() != blockState.getBlock()) {
                changeIOState(blockState, serverLevel, blockPos);
            }
        }
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, boolean bl) {
        Containers.updateNeighboursAfterDestroy(blockState, serverLevel, blockPos);
        SculkTransporterNetworkManagerProvider.get(serverLevel).markDirty();
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean bl) {
        if (level instanceof ServerLevel serverLevel) {
            changeIOState(blockState, serverLevel, blockPos);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, BITE_STATE, IO_STATE, POWERED});
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
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof TunedSculkJawBlockEntity && level instanceof ServerLevel serverLevel) {
            if (serverLevel.getBlockState(blockPos).getValue(IO_STATE).equals(TunedSculkJawIOState.INPUT)) {
                serverLevel.getBlockEntity(blockPos, ModBlockEntities.TUNED_SCULK_JAW_BLOCK_ENTITY).ifPresent(tunedSculkJawBlockEntity -> {
                    tunedSculkJawBlockEntity.entityInside(serverLevel, blockPos, blockState, entity);
                });
            }
        }
    }

    @Override
    protected void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        if (bl) {
            this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(5));
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    public InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof TunedSculkJawBlockEntity tunedSculkJawBlockEntity) {
            ItemStack itemStack2 = player.getItemInHand(interactionHand).copyWithCount(1);
            ItemStack itemStack3 = tunedSculkJawBlockEntity.getFilterItem();
            if (!itemStack3.isEmpty()) {
                if (ItemStack.isSameItemSameComponents(itemStack2, itemStack3)) {
                    if (itemStack.getCount() < itemStack.getMaxStackSize()) {
                        tunedSculkJawBlockEntity.setFilterItem(ItemStack.EMPTY);
                        itemStack.grow(1);
                        return InteractionResult.SUCCESS;
                    }
                }
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
            else {
                if (itemStack2.getItem().equals(ModItems.SCULK_AND_JAW_DEBUG_ITEM)) {
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                }
                if (!itemStack2.isEmpty()) {
                    tunedSculkJawBlockEntity.setFilterItem(itemStack2);
                    itemStack.consume(1, player);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof TunedSculkJawBlockEntity tunedSculkJawBlockEntity) {
            ItemStack itemStack = player.getInventory().getSelectedItem();
            ItemStack itemStack2 = tunedSculkJawBlockEntity.getFilterItem();
            if (itemStack.getItem().equals(ModItems.SCULK_AND_JAW_DEBUG_ITEM)) {
                player.openMenu(tunedSculkJawBlockEntity);
                return InteractionResult.SUCCESS;
            }
            if (!itemStack2.isEmpty()) {
                if (ItemStack.isSameItemSameComponents(itemStack, itemStack2)) {
                    if (itemStack.getCount() < itemStack.getMaxStackSize()) {
                        tunedSculkJawBlockEntity.setFilterItem(ItemStack.EMPTY);
                        itemStack.grow(1);
                        return InteractionResult.SUCCESS;
                    }
                }
                else if (itemStack.isEmpty()) {
                    tunedSculkJawBlockEntity.setFilterItem(ItemStack.EMPTY);
                    player.getInventory().setItem(player.getInventory().getSelectedSlot(), itemStack2);
                    player.getInventory().setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private void changeIOState(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos) {
        boolean bl = serverLevel.hasNeighborSignal(blockPos);
        if (bl != (Boolean) blockState.getValue(POWERED)) {
            BlockState blockState2 = blockState;
            if (!(Boolean) blockState.getValue(POWERED)) {
                blockState2 = (BlockState) blockState2.cycle(IO_STATE);
            }
            serverLevel.setBlock(blockPos, (BlockState) blockState2.setValue(POWERED, bl), 3);
        }
    }
}
