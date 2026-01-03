package ycpk.sculkjaw.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
import ycpk.sculkjaw.registry.ModBlocks;

import java.util.Objects;
import java.util.stream.Stream;

public class SculkJawPatchFeature extends Feature<SculkPatchConfiguration> {
    public SculkJawPatchFeature(Codec<SculkPatchConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<SculkPatchConfiguration> featurePlaceContext) {
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        BlockPos blockPos = featurePlaceContext.origin();
        if (!this.canSpreadFrom(worldGenLevel, blockPos)) {
            return false;
        } else {
            SculkPatchConfiguration sculkPatchConfiguration = (SculkPatchConfiguration)featurePlaceContext.config();
            RandomSource randomSource = featurePlaceContext.random();
            SculkSpreader sculkSpreader = SculkSpreader.createWorldGenSpreader();
            int i = sculkPatchConfiguration.spreadRounds() + sculkPatchConfiguration.growthRounds();

            int k;
            int l;
            for(int j = 0; j < i; ++j) {
                for(k = 0; k < sculkPatchConfiguration.chargeCount(); ++k) {
                    sculkSpreader.addCursors(blockPos, sculkPatchConfiguration.amountPerCharge());
                }

                boolean bl = j < sculkPatchConfiguration.spreadRounds();

                for(l = 0; l < sculkPatchConfiguration.spreadAttempts(); ++l) {
                    sculkSpreader.updateCursors(worldGenLevel, blockPos, randomSource, bl);
                }

                sculkSpreader.clear();
            }

            BlockPos blockPos2 = blockPos.below();
            if (randomSource.nextFloat() <= sculkPatchConfiguration.catalystChance() && worldGenLevel.getBlockState(blockPos2).isCollisionShapeFullBlock(worldGenLevel, blockPos2)) {
                worldGenLevel.setBlock(blockPos, Blocks.SCULK_CATALYST.defaultBlockState(), 3);
            }

            k = sculkPatchConfiguration.extraRareGrowths().sample(randomSource);

            for(l = 0; l < k; ++l) {
                BlockPos blockPos3 = blockPos.offset(randomSource.nextInt(5) - 2, 0, randomSource.nextInt(5) - 2);
                if (worldGenLevel.getBlockState(blockPos3).isAir() && worldGenLevel.getBlockState(blockPos3.below()).isFaceSturdy(worldGenLevel, blockPos3.below(), Direction.UP)) {
                    worldGenLevel.setBlock(blockPos3, (BlockState) ModBlocks.SCULK_JAW.defaultBlockState(), 3);
                }
            }

            return true;
        }
    }

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
}
