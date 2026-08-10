package ycpk.sculkandjaw.blocks.modblocks;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ycpk.sculkandjaw.registry.ModMobEffects;

import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class SculkAcidCauldronBlock extends AbstractModCauldronBlock {
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    private static final VoxelShape[] FILLED_SHAPES = (VoxelShape[]) Util.make(() -> {
        return boxes(2, (i) -> {
            return Shapes.or(AbstractModCauldronBlock.SHAPE, column(12.0, 4.0, getPixelContentHeight(i + 1)));
        });
    });

    public SculkAcidCauldronBlock(Map<Item, CauldronInteraction> interactionMap, BlockBehaviour.Properties properties) {
        super(properties, interactionMap);
        this.registerDefaultState(
                (BlockState) (
                        (BlockState) this.getStateDefinition().any()
                ).setValue(LEVEL, 1)
        );
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{LEVEL});
    }

    protected double getContentHeight(BlockState blockState) {
        return getPixelContentHeight((Integer) blockState.getValue(LEVEL)) / 16.0;
    }

    public boolean isFull(BlockState blockState) {
        return (Integer) blockState.getValue(LEVEL) == 3;
    }

    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return FILLED_SHAPES[(Integer) blockState.getValue(LEVEL) - 1];
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        int amplifier = 0;
        if(blockState.getValue(LEVEL) == 2) {
            amplifier = 1;
        }
        else if(blockState.getValue(LEVEL) == 3) {
            amplifier = 2;
        }
        if(level instanceof ServerLevel serverLevel) {
            if(entity instanceof LivingEntity livingEntity) {
                MobEffectInstance mobEffectInstance = null;
                mobEffectInstance = new MobEffectInstance(ModMobEffects.ACID_ETCHING, 20, amplifier, false, false, true);
                livingEntity.addEffect(mobEffectInstance);
            }
        }
    }

    public static void lowerFillLevel(BlockState blockState, Level level, BlockPos blockPos) {
        int i = (Integer) blockState.getValue(LEVEL) - 1;
        BlockState blockState2 = i == 0 ? Blocks.CAULDRON.defaultBlockState() : (BlockState) blockState.setValue(LEVEL, i);
        level.setBlockAndUpdate(blockPos, blockState2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState2));
    }

    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
        return (Integer) blockState.getValue(LEVEL);
    }

    private static double getPixelContentHeight(int i) {
        return 6.0 + (double) i * 3.0;
    }

    private static VoxelShape column(double d, double e, double f) {
        return column(d, d, e, f);
    }

    private static VoxelShape column(double d, double e, double f, double g) {
        double h = d / 2.0;
        double i = e / 2.0;
        return Block.box(8.0 - h, f, 8.0 - i, 8.0 + h, g, 8.0 +  i);
    }

    private static VoxelShape[] boxes(int i, IntFunction<VoxelShape> intFunction) {
        return (VoxelShape[]) IntStream.rangeClosed(0, i).mapToObj(intFunction).toArray((ix) -> {
            return new VoxelShape[ix];
        });
    }
}
