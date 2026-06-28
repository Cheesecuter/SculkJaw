package com.ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import com.ycpk.sculkandjaw.blocks.blockentities.ConcentratedSculkEntity;
import com.ycpk.sculkandjaw.registry.ModBlockEntities;
import com.ycpk.sculkandjaw.registry.ModBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ConcentratedSculkBlock extends BaseEntityBlock implements SculkBehaviour {
    public static final BooleanProperty COMBINED_WITH_SCULK_JAW = BooleanProperty.create("combined_with_sculk_jaw");
    public static final BooleanProperty COMBINED_WITH_SCULK_CATALYST = BooleanProperty.create("combined_with_sculk_catalyst");
    private int EXPERIENCE_REWARD = 5;
    public static final VoxelShape COLLISION_SHAPE_NOT_COMBINED = Block.box(0, 0, 0, 16, 16, 16);
    public static final VoxelShape COLLISION_SHAPE_COMBINED_OPEN = Shapes.or(
            Block.box(0, 0, 0, 16, 32, 1),
            Block.box(0, 0, 0, 1, 32, 16),
            Block.box(0, 0, 15, 16, 32, 16),
            Block.box(15, 0, 0, 16, 32, 16),
            Block.box(0, 0, 0, 16, 1, 16)
    );
    public static final VoxelShape COLLISION_SHAPE_COMBINED_CLOSE = Shapes.or(
            Block.box(0, 0, 0, 16, 32, 1),
            Block.box(0, 0, 0, 1, 32, 16),
            Block.box(0, 0, 15, 16, 32, 16),
            Block.box(15, 0, 0, 16, 32, 16),
            Block.box(0, 0, 0, 16, 1, 16),
            Block.box(0, 31, 0, 16, 32, 16)
    );

    public ConcentratedSculkBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst()
                .setValue(COMBINED_WITH_SCULK_JAW, false)
                .setValue(COMBINED_WITH_SCULK_CATALYST, false)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ConcentratedSculkBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ConcentratedSculkEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(COMBINED_WITH_SCULK_JAW) ? COLLISION_SHAPE_COMBINED_CLOSE : COLLISION_SHAPE_NOT_COMBINED;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return COLLISION_SHAPE_NOT_COMBINED;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(COMBINED_WITH_SCULK_JAW) ? COLLISION_SHAPE_COMBINED_OPEN : COLLISION_SHAPE_NOT_COMBINED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{COMBINED_WITH_SCULK_JAW, COMBINED_WITH_SCULK_CATALYST});
    }

    @Override
    protected void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        /*if (bl) {
            if(blockState.getValue(COMBINED_WITH_SCULK_JAW)) {
                if(EnchantmentHelper.hasTag(itemStack, ModEnchantmentTags.COMBINED_SCULK_JAW_DROPPING)) {
                    Direction direction = Direction.DOWN;
                    LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(ModBuiltInLootTables.SCULK_JAW_COMBINATION);
                    LootParams lootParams = (new LootParams.Builder(serverLevel)).create(LootContextParamSets.EMPTY);
                    ObjectArrayList<ItemStack> objectArrayList = lootTable.getRandomItems(lootParams);
                    if (!objectArrayList.isEmpty()) {
                        ObjectListIterator iterator = objectArrayList.iterator();
                        while (iterator.hasNext()) {
                            ItemStack itemStack1 = (ItemStack) iterator.next();
                            ItemEntity itemEntity = new ItemEntity(serverLevel, (double) blockPos.getX() + 0.5 + (double) direction.getStepX() * 0.65, (double) blockPos.getY() + 0.1, (double) blockPos.getZ() + 0.5 + (double) direction.getStepZ() * 0.65, itemStack1);
                            itemEntity.setDeltaMovement(0.05 * (double)direction.getStepX() + serverLevel.random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction.getStepZ() + serverLevel.random.nextDouble() * 0.02);
                            serverLevel.addFreshEntity(itemEntity);
                        }
                    }
                }
                else {
                    serverLevel.getBlockEntity(blockPos,
                            ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                        int experienceReward = concentratedSculkEntity.getExperienceReward() + EXPERIENCE_REWARD;
                        this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(experienceReward));
                    }));
                }
            }
            else {
                this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(EXPERIENCE_REWARD));
            }
        }*/
    }

    @Override
    protected BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if(!blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        else if(levelAccessor.getBlockState(blockPos.above()).getBlock().equals(ModBlocks.SCULK_JAW.get())) {
            levelAccessor.getBlockEntity(blockPos.above(), ModBlockEntities.SCULK_JAW_BLOCK_ENTITY.get()).ifPresent((sculkJawBlockEntity -> {
                if(!sculkJawBlockEntity.getHasCombined()) {
                    sculkJawBlockEntity.setHasCombined(true);
                    sculkJawBlockEntity.setBiteDamage(4.0F);
                    sculkJawBlockEntity.setAcidDamage(15.0F);
                    sculkJawBlockEntity.getLevel().addDestroyBlockEffect(blockPos.above(), blockState);
                    sculkJawBlockEntity.getLevel().addDestroyBlockEffect(blockPos, blockState);
                }
            }));
            levelAccessor.getBlockEntity(blockPos, ModBlockEntities.CONCENTRATED_SCULK_ENTITY.get()).ifPresent((concentratedSculkEntity -> {
                if(!concentratedSculkEntity.getHasCombinedWithSculkJaw()) {
                    concentratedSculkEntity.setHasCombinedWithSculkJaw(true);
                }
            }));
            return blockState.setValue(COMBINED_WITH_SCULK_JAW, true);
        }
        else if(levelAccessor.getBlockState(blockPos.above()).getBlock().equals(Blocks.SCULK_CATALYST)) {
            return blockState.setValue(COMBINED_WITH_SCULK_CATALYST, true);
        }
        else {
            super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
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
            if (canPlaceGrowth(levelAccessor, blockPos2)) {
                int j = sculkSpreader.growthSpawnCost();
                if (randomSource.nextInt(j) < i) {
                    BlockPos blockPos3 = blockPos2.above();
                    BlockState blockState = Blocks.SCULK_CATALYST.defaultBlockState();
                    levelAccessor.setBlock(blockPos3, blockState, 3);
                    levelAccessor.playSound((Player) null, blockPos2, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
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
        return levelReader.getBlockState(blockPos.above()).getBlock().equals(ModBlocks.SCULK_JAW.get()) && blockState.getValue(COMBINED_WITH_SCULK_JAW) || !blockState.getValue(COMBINED_WITH_SCULK_JAW);
    }
}
