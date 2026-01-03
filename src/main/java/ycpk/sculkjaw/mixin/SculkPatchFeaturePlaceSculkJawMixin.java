package ycpk.sculkjaw.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SculkPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ycpk.sculkjaw.blocks.custom.SculkJawBlock;
import ycpk.sculkjaw.registry.ModBlocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(SculkPatchFeature.class)
public abstract class SculkPatchFeaturePlaceSculkJawMixin {
    @Unique
    private static final Map<Integer, Direction> FACING_MAP = new HashMap<>(Map.of(0, Direction.NORTH, 1, Direction.EAST, 2, Direction.SOUTH, 3, Direction.WEST));

    @Unique
    private boolean canSpreadFrom(LevelAccessor levelAccessor, BlockPos blockPos) {
        BlockState blockState = levelAccessor.getBlockState(blockPos);
        if (blockState.getBlock() instanceof SculkBehaviour) {
            return true;
        } else if (!blockState.isAir() && (!blockState.is(Blocks.WATER) || !blockState.getFluidState().isSource())) {
            return false;
        } else {
            Objects.requireNonNull(blockPos);
            return Direction.stream()
                    .map(direction -> blockPos.relative(direction))
                    .anyMatch((blockpos1) -> {
                        return levelAccessor.getBlockState(blockpos1).isCollisionShapeFullBlock(levelAccessor, blockpos1);
                    });
        }
    }

    @Inject(at = @At("RETURN"), method = "place")
    public void placeSculkJaw(FeaturePlaceContext<SculkPatchConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> cir) {
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();

        if(this.canSpreadFrom(worldGenLevel, blockPos)){
            RandomSource randomSource = featurePlaceContext.random();
            int sculkJawCount = 0;
            for(int i = 0; i < 30; ++i){
                if(sculkJawCount >= 3) {
                    break;
                }
                BlockPos blockPos3 = blockPos.offset(randomSource.nextInt(11) - 5, randomSource.nextInt(3) - 1, randomSource.nextInt(11) - 5);
                if(worldGenLevel.getBlockState(blockPos3).getBlock() instanceof SculkBlock && worldGenLevel.getBlockState(blockPos3.above()).isAir()){
                    worldGenLevel.setBlock(blockPos3, ModBlocks.SCULK_JAW.defaultBlockState().setValue(SculkJawBlock.FACING, FACING_MAP.get(randomSource.nextInt(4))), 3);
                    sculkJawCount++;
                }
            }
            if(sculkJawCount < 1) {
                for(int i = 0; i < 10; ++i){
                    BlockPos blockPos3 = blockPos.offset(randomSource.nextInt(3) - 1, -1, randomSource.nextInt(3) - 1);
                    if(worldGenLevel.getBlockState(blockPos3).getBlock() instanceof SculkBlock && worldGenLevel.getBlockState(blockPos3.above()).isAir()){
                        worldGenLevel.setBlock(blockPos3, ModBlocks.SCULK_JAW.defaultBlockState().setValue(SculkJawBlock.FACING, FACING_MAP.get(randomSource.nextInt(4))), 3);
                        sculkJawCount++;
                    }
                }
            }
        }
    }
}
