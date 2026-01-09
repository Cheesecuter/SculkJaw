package ycpk.sculkjaw.blocks.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;

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
