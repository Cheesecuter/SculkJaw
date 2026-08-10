package ycpk.sculkandjaw.blocks.modblocks;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public abstract class AbstractModCauldronBlock extends Block {
    private static final VoxelShape SHAPE_INSIDE = column(12.0, 4.0, 16.0);
    protected static final VoxelShape SHAPE = (VoxelShape) Util.make(() -> {
        return Shapes.join(Shapes.block(), Shapes.or(column(16.0, 8.0, 0.0, 3.0), new VoxelShape[]{column(8.0, 16.0, 0.0, 3.0), column(12.0, 0.0, 3.0), SHAPE_INSIDE}), BooleanOp.ONLY_FIRST);
    });
    protected final Map<Item, CauldronInteraction> interactions;

    public AbstractModCauldronBlock(BlockBehaviour.Properties properties, Map<Item, CauldronInteraction> interactionMap) {
        super(properties);
        this.interactions = interactionMap;
    }

    protected double getContentHeight(BlockState blockState) {
        return 0.0;
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        CauldronInteraction cauldronInteraction = (CauldronInteraction) this.interactions.get(itemStack.getItem());
        return cauldronInteraction.interact(blockState, level, blockPos, player, interactionHand, itemStack);
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return SHAPE_INSIDE;
    }

    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    public abstract boolean isFull(BlockState blockState);

    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        BlockPos blockPos2 = PointedDripstoneBlock.findStalactiteTipAboveCauldron(serverLevel, blockPos);
        if (blockPos2 != null) {
            Fluid fluid = PointedDripstoneBlock.getCauldronFillFluidType(serverLevel, blockPos2);
            if (fluid != Fluids.EMPTY && this.canReceiveStalactiteDrip(fluid)) {
                this.receiveStalactiteDrip(blockState, serverLevel, blockPos, fluid);
            }

        }
    }

    protected boolean canReceiveStalactiteDrip(Fluid fluid) {
        return false;
    }

    protected void receiveStalactiteDrip(BlockState blockState, Level level, BlockPos blockPos, Fluid fluid) {
    }

    private static VoxelShape column(double d, double e, double f) {
        return column(d, d, e, f);
    }

    private static VoxelShape column(double d, double e, double f, double g) {
        double h = d / 2.0;
        double i = e / 2.0;
        return Block.box(8.0 - h, f, 8.0 - i, 8.0 + h, g, 8.0 +  i);
    }
}