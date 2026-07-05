package ycpk.sculkandjaw.blocks.modblocks;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;

import java.util.List;

public class ModLiquidBlock extends LiquidBlock {
    protected final FlowingFluid fluid;
    private final List<FluidState> stateCache;

    public ModLiquidBlock(FlowingFluid flowingFluid, BlockBehaviour.Properties properties) {
        super(flowingFluid, properties);
        this.fluid = flowingFluid;
        this.stateCache = Lists.newArrayList();
        this.stateCache.add(flowingFluid.getSource(false));

        for(int i = 1; i < 8; ++i) {
            this.stateCache.add(flowingFluid.getFlowing(8 - i, false));
        }

        this.stateCache.add(flowingFluid.getFlowing(8, true));
        this.registerDefaultState((BlockState) ((BlockState) this.getStateDefinition().any()).setValue(LEVEL, 0));
    }
}
