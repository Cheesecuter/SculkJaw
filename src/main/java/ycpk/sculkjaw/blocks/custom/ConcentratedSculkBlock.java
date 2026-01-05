package ycpk.sculkjaw.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ycpk.sculkjaw.blocks.blockentities.ConcentratedSculkEntity;
import ycpk.sculkjaw.level.storage.loot.ModBuiltInLootTables;
import ycpk.sculkjaw.registry.ModBlockEntities;
import ycpk.sculkjaw.registry.ModBlocks;
import ycpk.sculkjaw.tags.ModEnchantmentTags;

public class ConcentratedSculkBlock extends BaseEntityBlock implements SculkBehaviour {
    public static final BooleanProperty COMBINED_WITH_SCULK_JAW = BooleanProperty.create("combined_with_sculk_jaw");
    public static final BooleanProperty COMBINED_WITH_SCULK_CATALYST = BooleanProperty.create("combined_with_sculk_catalyst");
    public static final VoxelShape COLLISION_SHAPE_NOT_COMBINED = Block.box(0, 0, 0, 16, 16, 16);
    public static final VoxelShape COLLISION_SHAPE_COMBINED = Shapes.or(
            Block.box(0, 0, 0, 16, 32, 1),
            Block.box(0, 0, 0, 1, 32, 16),
            Block.box(0, 0, 15, 16, 32, 16),
            Block.box(15, 0, 0, 16, 32, 16),
            Block.box(0, 0, 0, 16, 1, 16)
    );
    public static final VoxelShape SHAPE_COMBINED = Shapes.or(
            Block.box(0, 0, 0, 16, 32, 1),
            Block.box(0, 0, 0, 1, 32, 16),
            Block.box(0, 0, 15, 16, 32, 16),
            Block.box(15, 0, 0, 16, 32, 16),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 31, 0, 16, 32, 16)
    );
    public static final VoxelShape SHAPE_NOT_COMBINED = Block.box(0, 0, 0, 16, 16, 16);

    public ConcentratedSculkBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst().setValue(COMBINED_WITH_SCULK_JAW, false).setValue(COMBINED_WITH_SCULK_CATALYST, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ConcentratedSculkBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ConcentratedSculkEntity(pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(COMBINED_WITH_SCULK_JAW) ? SHAPE_COMBINED : SHAPE_NOT_COMBINED;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return COLLISION_SHAPE_NOT_COMBINED;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return COLLISION_SHAPE_NOT_COMBINED;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(COMBINED_WITH_SCULK_JAW) ? COLLISION_SHAPE_COMBINED : COLLISION_SHAPE_NOT_COMBINED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{COMBINED_WITH_SCULK_JAW, COMBINED_WITH_SCULK_CATALYST});
    }

    @Override
    protected void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        if (bl) {
            if(blockState.getValue(COMBINED_WITH_SCULK_JAW)) {
                serverLevel.getBlockEntity(blockPos.above(),
                        ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                    int experienceReward = sculkJawBlockEntity.getExperienceReward();
                    this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(experienceReward));
                }));
                serverLevel.getBlockEntity(blockPos,
                        ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                    int experienceReward = concentratedSculkEntity.getExperienceReward();
                    this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(experienceReward));
                }));
            }
            else {
                this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(5));
            }
        }
    }

    /*@Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
        if(level instanceof ServerLevel serverLevel && blockState.getValue(COMBINED_WITH_SCULK_JAW)) {
            if(player.isCreative()) {
                serverLevel.getBlockEntity(blockPos.above(),
                        ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                            sculkJawBlockEntity.setExperienceReward(0);
                }));
                serverLevel.getBlockEntity(blockPos,
                        ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                    concentratedSculkEntity.setExperienceReward(0);
                }));
            }
            if(EnchantmentHelper.hasTag(itemStack, ModEnchantmentTags.COMBINED_SCULK_JAW_DROPPING)) {
                Direction direction = Direction.DOWN;
                dropFromBlockInteractLootTable(serverLevel, ModBuiltInLootTables.SCULK_JAW_COMBINATION, blockState, level.getBlockEntity(blockPos), itemStack, player, (serverLevelx, itemStackx) -> {
                    ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + 0.5 + (double)direction.getStepX() * 0.65, (double)blockPos.getY() + 0.1, (double)blockPos.getZ() + 0.5 + (double)direction.getStepZ() * 0.65, itemStackx);
                    itemEntity.setDeltaMovement(0.05 * (double)direction.getStepX() + level.random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction.getStepZ() + level.random.nextDouble() * 0.02);
                    level.addFreshEntity(itemEntity);
                });
                dropFromBlockInteractLootTable(serverLevel, ModBuiltInLootTables.CONCENTRATED_SCULK_COMBINATION, blockState, level.getBlockEntity(blockPos), itemStack, player, (serverLevelx, itemStackx) -> {
                    ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + 0.5 + (double)direction.getStepX() * 0.65, (double)blockPos.getY() + 0.1, (double)blockPos.getZ() + 0.5 + (double)direction.getStepZ() * 0.65, itemStackx);
                    itemEntity.setDeltaMovement(0.05 * (double)direction.getStepX() + level.random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction.getStepZ() + level.random.nextDouble() * 0.02);
                    level.addFreshEntity(itemEntity);
                });
            }
            else {
                serverLevel.getBlockEntity(blockPos,
                        ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                    int experienceReward = concentratedSculkEntity.getExperienceReward();
                    this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(experienceReward));
                }));
            }
        }
        else {
            dropResources(blockState, level, blockPos, blockEntity, player, itemStack);
        }
    }*/

    @Override
    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        if(!blockState.canSurvive(levelReader, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        else if(levelReader.getBlockState(blockPos.above()).getBlock().equals(ModBlocks.SCULK_JAW)) {
            return blockState.setValue(COMBINED_WITH_SCULK_JAW, true);
        }
        else if(levelReader.getBlockState(blockPos.above()).getBlock().equals(Blocks.SCULK_CATALYST)) {
            return blockState.setValue(COMBINED_WITH_SCULK_CATALYST, true);
        }
        else {
            super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
        }
        return blockState;
    }

    @Override
    protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return isSculkJawDestroied(levelReader, blockPos, blockState);
    }

    @Override
    public int attemptUseCharge(SculkSpreader.ChargeCursor chargeCursor, LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, SculkSpreader sculkSpreader, boolean bl) {
        int i = chargeCursor.getCharge();
        if (i != 0 && randomSource.nextInt(sculkSpreader.chargeDecayRate()) == 0) {
            BlockPos blockPos2 = chargeCursor.getPos();
            boolean bl2 = blockPos2.closerThan(blockPos, (double)sculkSpreader.noGrowthRadius());
            if (!bl2 && canPlaceGrowth(levelAccessor, blockPos2)) {
                int j = sculkSpreader.growthSpawnCost();
                if (randomSource.nextInt(j) < i) {
                    BlockPos blockPos3 = blockPos2.above();
                    BlockState blockState = Blocks.SCULK_CATALYST.defaultBlockState();
                    levelAccessor.setBlock(blockPos3, blockState, 3);
                    levelAccessor.playSound((Entity)null, blockPos2, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                return Math.max(0, i - j);
            } else {
                return randomSource.nextInt(sculkSpreader.additionalDecayRate()) != 0 ? i : i - (bl2 ? 1 : getDecayPenalty(sculkSpreader, blockPos2, blockPos, i));
            }
        } else {
            return i;
        }
    }

    private static boolean canPlaceGrowth(LevelAccessor levelAccessor, BlockPos blockPos) {
        BlockState blockState = levelAccessor.getBlockState(blockPos.above());
        if (blockState.isAir() || blockState.is(Blocks.WATER) && blockState.getFluidState().is(Fluids.WATER) || blockState.is(Blocks.SCULK_VEIN)) {
            if(blockState.is(Blocks.SCULK_CATALYST)) {
                return false;
            }
            else {
                return true;
            }
        } else {
            return false;
        }
    }

    private static int getDecayPenalty(SculkSpreader sculkSpreader, BlockPos blockPos, BlockPos blockPos2, int i) {
        int j = sculkSpreader.noGrowthRadius();
        float f = Mth.square((float)Math.sqrt(blockPos.distSqr(blockPos2)) - (float)j);
        int k = Mth.square(24 - j);
        float g = Math.min(1.0F, f / (float)k);
        return Math.max(1, (int)((float)i * g * 0.5F));
    }

    private static boolean isSculkJawDestroied(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return levelReader.getBlockState(blockPos.above()).getBlock().equals(ModBlocks.SCULK_JAW) && blockState.getValue(COMBINED_WITH_SCULK_JAW) || !blockState.getValue(COMBINED_WITH_SCULK_JAW);
    }
}
