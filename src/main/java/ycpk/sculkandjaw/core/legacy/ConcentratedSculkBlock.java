package ycpk.sculkandjaw.core.legacy;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.modblocks.SculkAggregator;

public class ConcentratedSculkBlock extends BaseEntityBlock {
    public static final MapCodec<ConcentratedSculkBlock> CODEC = simpleCodec(ConcentratedSculkBlock::new);
    public static final BooleanProperty COMBINED_WITH_SCULK_JAW = SculkAggregator.COMBINED_WITH_SCULK_JAW;
    public static final BooleanProperty COMBINED_WITH_SCULK_CATALYST = SculkAggregator.COMBINED_WITH_SCULK_CATALYST;
    public static final BooleanProperty ACID_FILLED = SculkAggregator.ACID_FILLED;

    public ConcentratedSculkBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst()
                .setValue(COMBINED_WITH_SCULK_JAW, false)
                .setValue(COMBINED_WITH_SCULK_CATALYST, false)
                .setValue(ACID_FILLED, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ConcentratedSculkBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{COMBINED_WITH_SCULK_JAW, COMBINED_WITH_SCULK_CATALYST, ACID_FILLED});
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return null;
    }
}
