package com.ycpk.sculkandjaw.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ycpk.sculkandjaw.blocks.modblocks.LargeUmbraFern;
import com.ycpk.sculkandjaw.blocks.modblocks.ModDoubleBedPlantBlock;
import com.ycpk.sculkandjaw.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.HashMap;
import java.util.Map;

public class SculkAcidLakeFeature extends Feature<SculkAcidLakeFeature.Configuration> {
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();
    private static final Map<Integer, Direction> FACING_MAP = new HashMap<>(Map.of(0, Direction.NORTH, 1, Direction.EAST, 2, Direction.SOUTH, 3, Direction.WEST));

    public SculkAcidLakeFeature(Codec<Configuration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<Configuration> featurePlaceContext) {
        BlockPos originPos = featurePlaceContext.origin();
        WorldGenLevel worldGenLevel = featurePlaceContext.level();
        if (!worldGenLevel.getBiome(originPos).is(Biomes.DEEP_DARK)) {
            return false;
        }
        if (this.isAirPlate(originPos, worldGenLevel)) {
            originPos = new BlockPos(originPos.getX(), -51, originPos.getZ());
        }
        RandomSource randomSource = featurePlaceContext.random();
        SculkAcidLakeFeature.Configuration configuration = (SculkAcidLakeFeature.Configuration) featurePlaceContext.config();
        if (originPos.getY() <= worldGenLevel.getMinBuildHeight() + 6) {
            return false;
        }
        else {
            boolean[] bls = new boolean[2048];
            int i = randomSource.nextInt(4) + 4;
            int originPosY = originPos.getY();

            for (int j = 0; j < i; ++j) {
                double d = randomSource.nextDouble() * 6.0 + 3.0;
                double e = randomSource.nextDouble() * 4.0 + 2.0;
                double f = randomSource.nextDouble() * 6.0 + 3.0;
                double g = randomSource.nextDouble() * (16.0 - d - 2.0) + 1.0 + d / 2.0;
                double h = randomSource.nextDouble() * (8.0 - e - 4.0) + 2.0 + e / 2.0;
                double k = randomSource.nextDouble() * (16.0 - f - 2.0) + 1.0 + f / 2.0;

                for (int l = 1; l < 15; ++l) {
                    for (int m = 1; m < 15; ++m) {
                        for (int n = 1; n < 7; ++n) {
                            double o = ((double)l - g) / (d / 2.0);
                            double p = ((double)n - h) / (e / 2.0);
                            double q = ((double)m - k) / (f / 2.0);
                            double r = o * o + p * p + q * q;
                            if (r < 1.0) {
                                bls[(l * 16 + m) * 8 + n] = true;
                            }
                            originPosY = (int) e;
                        }
                    }
                }
            }
            originPos = originPos.below(originPosY);

            BlockState blockState = configuration.fluid().getState(randomSource, originPos);

            int t;
            boolean v;
            int s;
            int u;
            for (s = 0; s < 16; ++s) {
                for (t = 0; t < 16; ++t) {
                    for (u = 0; u < 8; ++u) {
                        v = !bls[(s * 16 + t) * 8 + u] && (s < 15 && bls[((s + 1) * 16 + t) * 8 + u] || s > 0 && bls[((s - 1) * 16 + t) * 8 + u] || t < 15 && bls[(s * 16 + t + 1) * 8 + u] || t > 0 && bls[(s * 16 + (t - 1)) * 8 + u] || u < 7 && bls[(s * 16 + t) * 8 + u + 1] || u > 0 && bls[(s * 16 + t) * 8 + (u - 1)]);
                        if (v) {
                            BlockState blockState2 = worldGenLevel.getBlockState(originPos.offset(s, u, t));
                            if (u >= 4 && blockState2.liquid()) {
                                return false;
                            }

                            if (u < 4 && !blockState2.isSolid() && worldGenLevel.getBlockState(originPos.offset(s, u, t)) != blockState) {
                                return false;
                            }
                        }
                    }
                }
            }

            boolean bl2;
            for (s = 0; s < 16; ++s) {
                for (t = 0; t < 16; ++t) {
                    for (u = 0; u < 8; ++u) {
                        if (bls[(s * 16 + t) * 8 + u]) {
                            BlockPos blockPos2 = originPos.offset(s, u, t);
                            if (this.canReplaceBlock(worldGenLevel.getBlockState(blockPos2))) {
                                bl2 = u >= 4;
                                worldGenLevel.setBlock(blockPos2, bl2 ? AIR : blockState, 2);
                                if (bl2) {
                                    worldGenLevel.scheduleTick(blockPos2, AIR.getBlock(), 0);
                                    this.markAboveForPostProcessing(worldGenLevel, blockPos2);
                                }
                            }
                        }
                    }
                }
            }

            BlockState blockState3 = configuration.barrier().getState(randomSource, originPos);
            BlockState umbrafern = ModBlocks.UMBRAFERN.get().defaultBlockState();
            BlockState largeUmbrafern = ModBlocks.LARGE_UMBRAFERN.get().defaultBlockState();
            BlockState acidophilicCordyceps = ModBlocks.ACIDOPHILIC_CORDYCEPS.get().defaultBlockState();
            if (!blockState3.isAir()) {
                for (t = 0; t < 16; ++t) {
                    for (u = 0; u < 16; ++u) {
                        for (int w = 0; w < 8; ++w) {
                            bl2 = !bls[(t * 16 + u) * 8 + w] && (t < 15 && bls[((t + 1) * 16 + u) * 8 + w] || t > 0 && bls[((t - 1) * 16 + u) * 8 + w] || u < 15 && bls[(t * 16 + u + 1) * 8 + w] || u > 0 && bls[(t * 16 + (u - 1)) * 8 + w] || w < 7 && bls[(t * 16 + u) * 8 + w + 1] || w > 0 && bls[(t * 16 + u) * 8 + (w - 1)]);
                            if (bl2 && (w < 4)) {
                                BlockState blockState4 = worldGenLevel.getBlockState(originPos.offset(t, w, u));
                                if (blockState4.isSolid() && !blockState4.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
                                    BlockPos blockPos3 = originPos.offset(t, w, u);
                                    worldGenLevel.setBlock(blockPos3, blockState3, 2);
                                    this.markAboveForPostProcessing(worldGenLevel, blockPos3);
                                }
                            }
                            if (bl2 && (w == 3 || w == 4)) {
                                BlockState blockState4 = worldGenLevel.getBlockState(originPos.offset(t, w, u));
                                if (blockState4.is(Blocks.SCULK)) {
                                    BlockPos blockPos3 = originPos.offset(t, w, u);
                                    worldGenLevel.setBlock(blockPos3, blockState3, 2);
                                    spreadSculk(blockPos3, worldGenLevel, 2);
                                    this.markAboveForPostProcessing(worldGenLevel, blockPos3);
                                }
                            }
                            if (bl2 && (w < 4)) {
                                BlockState sculkVein = worldGenLevel.getBlockState(originPos.offset(t, (w + 1), u));
                                if (sculkVein.is(Blocks.SCULK_VEIN)) {
                                    worldGenLevel.setBlock(originPos.offset(t, (w + 1), u), AIR, 2);
                                }
                            }
                            if (bl2 && ((w >= 4 && w <= 5) || randomSource.nextInt(2) != 0)) {
                                BlockState sculk = worldGenLevel.getBlockState(originPos.offset(t, w, u));
                                BlockState caveAir = worldGenLevel.getBlockState(originPos.offset(t, (w + 1), u));
                                if (sculk.isSolid() && sculk.is(Blocks.SCULK) && caveAir.is(Blocks.CAVE_AIR)) {
                                    BlockPos blockPos4 = originPos.offset(t, (w + 1), u);
                                    worldGenLevel.setBlock(blockPos4, umbrafern, 2);
                                    this.markAboveForPostProcessing(worldGenLevel, blockPos4);
                                }
                            }
                            if (bl2 && ((w >= 4 && w <= 5) || randomSource.nextInt(2) != 0)) {
                                BlockState sculk = worldGenLevel.getBlockState(originPos.offset(t, w, u));
                                BlockState caveAir1 = worldGenLevel.getBlockState(originPos.offset(t, (w + 1), u));
                                BlockState caveAir2 = worldGenLevel.getBlockState(originPos.offset(t, (w + 2), u));
                                if (sculk.isSolid() && sculk.is(Blocks.SCULK) && caveAir1.is(Blocks.CAVE_AIR) && caveAir2.is(Blocks.CAVE_AIR)) {
                                    BlockPos blockPos4 = originPos.offset(t, (w + 1), u);
                                    BlockPos blockPos5 = originPos.offset(t, (w + 2), u);
                                    worldGenLevel.setBlock(blockPos4, largeUmbrafern.setValue(LargeUmbraFern.HALF, DoubleBlockHalf.LOWER), 2);
                                    worldGenLevel.setBlock(blockPos5, largeUmbrafern.setValue(LargeUmbraFern.HALF, DoubleBlockHalf.UPPER), 2);
                                    this.markAboveForPostProcessing(worldGenLevel, blockPos4);
                                }
                            }
                            if (bl2 && ((w >= 4 && w <= 5) || randomSource.nextInt(2) != 0)) {
                                BlockState sculk = worldGenLevel.getBlockState(originPos.offset(t, w, u));
                                BlockState caveAir1 = worldGenLevel.getBlockState(originPos.offset(t, (w + 1), u));
                                BlockState caveAir2 = worldGenLevel.getBlockState(originPos.offset(t, (w + 2), u));
                                if (sculk.isSolid() && sculk.is(Blocks.SCULK) && caveAir1.is(Blocks.CAVE_AIR) && caveAir2.is(Blocks.CAVE_AIR)) {
                                    BlockPos blockPos4 = originPos.offset(t, (w + 1), u);
                                    BlockPos blockPos5 = originPos.offset(t, (w + 2), u);
                                    int facing = randomSource.nextInt(4);
                                    int amount = randomSource.nextInt(1, 4);
                                    worldGenLevel.setBlock(blockPos4, acidophilicCordyceps.setValue(ModDoubleBedPlantBlock.HALF, DoubleBlockHalf.LOWER).setValue(ModDoubleBedPlantBlock.FACING, FACING_MAP.get(facing)).setValue(ModDoubleBedPlantBlock.AMOUNT, amount), 2);
                                    worldGenLevel.setBlock(blockPos5, acidophilicCordyceps.setValue(ModDoubleBedPlantBlock.HALF, DoubleBlockHalf.UPPER).setValue(ModDoubleBedPlantBlock.FACING, FACING_MAP.get(facing)).setValue(ModDoubleBedPlantBlock.AMOUNT, amount), 2);
                                    this.markAboveForPostProcessing(worldGenLevel, blockPos4);
                                }
                            }
                        }
                    }
                }
            }

            spreadSculk(originPos.offset(8, 1, 8), worldGenLevel, 64);

            return true;
        }
    }

    private boolean canReplaceBlock(BlockState blockState) {
        return !blockState.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

    private boolean isAirPlate(BlockPos blockPos, WorldGenLevel worldGenLevel) {
        BlockPos blockPos2 = new BlockPos(blockPos.getX(), -51, blockPos.getZ());
        boolean bl = worldGenLevel.getBlockState(blockPos).isAir() &&
                worldGenLevel.getBlockState(blockPos.relative(Direction.NORTH)).isAir() &&
                worldGenLevel.getBlockState(blockPos.relative(Direction.SOUTH)).isAir() &&
                worldGenLevel.getBlockState(blockPos.relative(Direction.EAST)).isAir() &&
                worldGenLevel.getBlockState(blockPos.relative(Direction.WEST)).isAir();
        return bl;

    }

    private boolean spreadSculk(BlockPos blockPos, WorldGenLevel worldGenLevel, int xp) {
        SculkSpreader sculkSpreader = SculkSpreader.createWorldGenSpreader();
        int spreadRounds = 1;
        for(int i = 0; i < spreadRounds; ++i) {
            for(int j = 0; j < 6; ++j) {
                sculkSpreader.addCursors(blockPos, xp);
            }
            for(int k = 0; k < xp; ++k) {
                sculkSpreader.updateCursors(worldGenLevel, blockPos, worldGenLevel.getRandom(), true);
            }
            sculkSpreader.clear();
        }
        return true;
    }

    public static record Configuration(BlockStateProvider fluid, BlockStateProvider barrier) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(BlockStateProvider.CODEC.fieldOf("fluid").forGetter(Configuration::fluid),
                    BlockStateProvider.CODEC.fieldOf("barrier").forGetter(Configuration::barrier)).apply(instance, Configuration::new);
        });

        public Configuration(BlockStateProvider fluid, BlockStateProvider barrier) {
            this.fluid = fluid;
            this.barrier = barrier;
        }

        public BlockStateProvider fluid() {return this.fluid;}

        public BlockStateProvider barrier() {return this.barrier;}
    }
}
