package ycpk.sculkjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.entity.LivingEntity;
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
import ycpk.sculkjaw.registry.ModEffects;

public class SculkAcidCauldronBlock extends AbstractModCauldronBlock {
    public static final MapCodec<SculkAcidCauldronBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
       return instance.group(CauldronInteraction.CODEC.fieldOf("interactions").forGetter((modLayeredCauldronBlock) -> {
           return modLayeredCauldronBlock.interactions;
       }), propertiesCodec()).apply(instance, SculkAcidCauldronBlock::new);
    });
    public static final IntegerProperty LEVEL;
    private static final VoxelShape[] FILLED_SHAPES;

    public SculkAcidCauldronBlock(CauldronInteraction.InteractionMap interactionMap, BlockBehaviour.Properties properties) {
        super(properties, interactionMap);
        this.registerDefaultState((BlockState) ((BlockState) this.getStateDefinition().any()).setValue(LEVEL, 1));
    }

    public MapCodec<SculkAcidCauldronBlock> codec() {return CODEC;}

    protected double getContentHeight(BlockState blockState) {
        return getPixelContentHeight((Integer) blockState.getValue(LEVEL)) / 16.0;
    }

    public boolean isFull(BlockState blockState) {
        return (Integer) blockState.getValue(LEVEL) == 3;
    }

    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return FILLED_SHAPES[(Integer) blockState.getValue(LEVEL) - 1];
    }
    protected void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl) {
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
                mobEffectInstance = new MobEffectInstance(ModEffects.ACID_ETCHING, 20, amplifier, false, false, true);
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

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{LEVEL});
    }

    private static double getPixelContentHeight(int i) {
        return 6.0 + (double) i * 3.0;
    }

    static {
        LEVEL = BlockStateProperties.LEVEL_CAULDRON;
        FILLED_SHAPES = (VoxelShape[]) Util.make(() -> {
            return Block.boxes(2, (i) -> {
                return Shapes.or(AbstractModCauldronBlock.SHAPE, Block.column(12.0, 4.0, getPixelContentHeight(i + 1)));
            });
        });
    }
}
