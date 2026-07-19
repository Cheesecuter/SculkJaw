package ycpk.sculkandjaw.core.legacy;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.modblocks.AcidcoilReed;

public class AcidophilicCordyceps extends Block {
    public static final MapCodec<AcidophilicCordyceps> CODEC = simpleCodec(AcidophilicCordyceps::new);
    public static final EnumProperty<DoubleBlockHalf> HALF = AcidcoilReed.HALF;
    public static final EnumProperty<Direction> FACING = AcidcoilReed.FACING;
    public static final IntegerProperty AMOUNT = AcidcoilReed.AMOUNT;

    public AcidophilicCordyceps(Properties properties) {
        super(properties);
        this.registerDefaultState(
                (BlockState) (
                        (BlockState) (
                                (BlockState) (
                                        (BlockState) this.getStateDefinition().any()
                                ).setValue(FACING, Direction.NORTH)
                        ).setValue(AMOUNT, 1)
                ).setValue(HALF, DoubleBlockHalf.LOWER)
        );
    }

    @Override
    public MapCodec<AcidophilicCordyceps> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, AMOUNT, HALF});
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return null;
    }
}
