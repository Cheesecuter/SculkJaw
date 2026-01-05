package ycpk.sculkjaw.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.Nullable;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.blocks.blockentities.SculkJawBlockEntity;
import ycpk.sculkjaw.level.storage.loot.ModBuiltInLootTables;
import ycpk.sculkjaw.registry.*;
import ycpk.sculkjaw.tags.ModEnchantmentTags;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SculkJawBlock extends BaseEntityBlock{
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty START_BITE = BooleanProperty.create("start_bite");
    public static final BooleanProperty STOP_BITE = BooleanProperty.create("stop_bite");
    public static final BooleanProperty BITE = BooleanProperty.create("bite");
    public static final BooleanProperty COMBINED = BooleanProperty.create("combined");
    private boolean IS_BITING_PROJECTILE = false;
    private int EXPERIENCE_REWARD = 5;
    public static final VoxelShape COLLISION_SHAPE_OPEN = Shapes.or(
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0),
            Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0),
            Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0),
            Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0)
    );
    public static final VoxelShape COLLISION_SHAPE_CLOSE = Shapes.or(
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0),
            Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0),
            Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0),
            Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0),
            Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0)
    );
    public static final VoxelShape INSIDE_COLLISION_SHAPE = SculkJawBlock.box(1.0, 1.0, 1.0, 15.0, 14.0, 15.0);
    public static final VoxelShape COLLISION_SHAPE_COMBINED_OPEN = Shapes.or(
            Block.box(0.0, -16.0, 0.0, 16.0, 16.0, 1.0),
            Block.box(0.0, -16.0, 0.0, 1.0, 16.0, 16.0),
            Block.box(0.0, -16.0, 15.0, 16.0, 16.0, 16.0),
            Block.box(15.0, -16.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(0.0, -16.0, 0.0, 16.0, -15.0, 16.0)
    );
    public static final VoxelShape COLLISION_SHAPE_COMBINED_CLOSE = Shapes.or(
            Block.box(0.0, -16.0, 0.0, 16.0, 16.0, 1.0),
            Block.box(0.0, -16.0, 0.0, 1.0, 16.0, 16.0),
            Block.box(0.0, -16.0, 15.0, 16.0, 16.0, 16.0),
            Block.box(15.0, -16.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(0.0, -16.0, 0.0, 16.0, -15.0, 16.0),
            Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0)
    );
    public static final VoxelShape INSIDE_COLLISION_SHAPE_COMBINED = SculkJawBlock.box(1.0, -15.0, 1.0, 15.0, 14.0, 15.0);

    public SculkJawBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst()
                .setValue(FACING, Direction.NORTH)
                .setValue(START_BITE, false)
                .setValue(BITE, false)
                .setValue(STOP_BITE, false)
                .setValue(COMBINED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(SculkJawBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SculkJawBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_CLOSE : COLLISION_SHAPE_CLOSE;
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_CLOSE : COLLISION_SHAPE_CLOSE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState) {
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return blockState.getValue(COMBINED) ? INSIDE_COLLISION_SHAPE_COMBINED : INSIDE_COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if(collisionContext instanceof EntityCollisionContext) {
            Entity entity = ((EntityCollisionContext) collisionContext).getEntity();
            if(entity != null) {
                if(entity.getType().is(ModTags.IMMUNE_TO_SCULK_JAW)){
                    return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_CLOSE : COLLISION_SHAPE_CLOSE;
                }
                else if(entity.isShiftKeyDown() && entity.distanceToSqr(blockPos.getCenter().add(0, 0.5, 0)) > 0.2){
                    return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_CLOSE : COLLISION_SHAPE_CLOSE;
                }
            }
        }
        if(blockState.getValue(START_BITE) || blockState.getValue(BITE) || blockState.getValue(STOP_BITE)) {
            return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_OPEN : COLLISION_SHAPE_OPEN;
        }
        return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_CLOSE : COLLISION_SHAPE_CLOSE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, START_BITE, BITE, STOP_BITE, COMBINED});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    @Override
    protected void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        if (bl) {
            if(blockState.getValue(COMBINED)) {
                serverLevel.getBlockEntity(blockPos,
                        ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                    int experienceReward = sculkJawBlockEntity.getExperienceReward();
                    this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(experienceReward));
                }));
                serverLevel.getBlockEntity(blockPos.below(),
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

    @Override
    protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return true;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
        if(level instanceof ServerLevel serverLevel && blockState.getValue(COMBINED)) {
            if(player.isCreative()) {
                this.setExperiencecReward(0);
                serverLevel.setBlock(blockPos.below(), Blocks.AIR.defaultBlockState(), 3);
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
                //this.setExperiencecReward(0);
                //serverLevel.setBlock(blockPos.below(), Blocks.AIR.defaultBlockState(), 3);
            }
            else {
                this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(EXPERIENCE_REWARD));
            }
        }
        else {
            dropResources(blockState, level, blockPos, blockEntity, player, itemStack);
        }
    }

    @Override
    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        if(!blockState.canSurvive(levelReader, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        else if(levelReader.getBlockState(blockPos.below()).getBlock().equals(ModBlocks.CONCENTRATED_SCULK)) {
            levelReader.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                sculkJawBlockEntity.setHasCombined(true);
                sculkJawBlockEntity.setBiteDamage(10.0F);
                sculkJawBlockEntity.setAcidDamage(15.0F);
            }));
            return blockState.setValue(COMBINED, true);
        }
        else {
            super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
        }
        return blockState;
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean bl) {
        if(level instanceof ServerLevel serverLevel && level.getBlockState(blockPos).is(this)) {
            Block block1 = level.getBlockState(blockPos.below()).getBlock();
            if(block1.equals(ModBlocks.CONCENTRATED_SCULK)) {
                serverLevel.getBlockEntity(blockPos.below(),
                        ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                            if(!concentratedSculkEntity.getHasCombinedWithSculkJaw()) {
                                concentratedSculkEntity.setHasCombinedWithSculkJaw(true);
                                //level.addDestroyBlockEffect(blockPos, blockState);
                                /*level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSoundEvents.SCULK_JAW_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                                level.addParticle(ParticleTypes.SCULK_CHARGE_POP, blockPos.getX(), blockPos.getY() + 2, blockPos.getZ(), blockPos.getX(), blockPos.getY() + 2, blockPos.getZ());
                                playCombinedParticle(blockPos, blockState, level);*/
                            }
                }));
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return isConcentratedSculkDestroied(levelReader, blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY, SculkJawBlockEntity::tick);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity,
                             InsideBlockEffectApplier insideBlockEffectApplier, boolean bl) {
        if((entity instanceof LivingEntity || entity instanceof ItemEntity || entity.getInBlockState().is(this)) &&
                !(entity.getType().is(ModTags.IMMUNE_TO_SCULK_JAW))) {
            if(level instanceof ServerLevel serverLevel) {
                serverLevel.getBlockEntity(blockPos,
                        ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                    double ex = entity.getBoundingBox().getXsize();
                    double ey = entity.getBoundingBox().getYsize();
                    double ez = entity.getBoundingBox().getZsize();
                    if((ex > 0.9 || ey > 0.9 || ez > 0.9) && entity.getType() != EntityType.PLAYER) {
                        sculkJawBlockEntity.setIsLargeEntity(true);
                        entity.makeStuckInBlock(blockState, new Vec3(0.8, 1.5, 0.8));
                        sculkJawBlockEntity.addBiteDamageEntity(entity.getUUID());
                        if(!sculkJawBlockEntity.getIsBitingLargeEntity()){
                            biteDamage(level, blockPos, blockState, entity);
                        }
                    }
                    else{
                        if(entity instanceof ItemEntity) {
                            if(sculkJawBlockEntity.getHasCombined()) {
                                return;
                            }
                            entity.kill(serverLevel);
                            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                    ModSoundEvents.SCULK_JAW_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                        }
                        else if(entity instanceof Projectile) {
                            if(IS_BITING_PROJECTILE) {
                                entity.kill(serverLevel);
                                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        ModSoundEvents.SCULK_JAW_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                        else {
                            sculkJawBlockEntity.addAcidDamageEntity(entity.getUUID());
                            if(!sculkJawBlockEntity.getIsAcidingEntity()) {
                                acidDamage(level, blockPos, blockState, entity);
                            }
                            if(!sculkJawBlockEntity.getIsEffectingEntity()) {
                                addEffect(level, blockPos, blockState, entity, insideBlockEffectApplier);
                            }
                            entity.push(blockPos.getCenter().add(0, 0.2, 0).subtract(entity.position()).multiply(new Vec3(0.5, 0.5, 0.5)));
                        }
                    }
                }));
            }
        }
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if((entity instanceof LivingEntity || entity instanceof ItemEntity || entity.getInBlockState().is(this)) &&
                !blockState.getValue(BITE) && !(entity.getType().is(ModTags.IMMUNE_TO_SCULK_JAW))) {
            if(level instanceof ServerLevel serverLevel) {
                serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                    double ex = entity.getBoundingBox().getXsize();
                    double ey = entity.getBoundingBox().getYsize();
                    double ez = entity.getBoundingBox().getZsize();
                    if((ex > 0.9 || ey > 0.9 || ez > 0.9) && entity.getType() != EntityType.PLAYER) {
                        sculkJawBlockEntity.setIsLargeEntity(true);
                        entity.makeStuckInBlock(blockState, new Vec3(0.8, 1.5, 0.8));
                        sculkJawBlockEntity.addBiteDamageEntity(entity.getUUID());
                        if(!sculkJawBlockEntity.getIsBitingLargeEntity()){
                            biteDamage(level, blockPos, blockState, entity);
                        }
                    }
                    else if(!entity.isShiftKeyDown() || entity.distanceToSqr(blockPos.getCenter().add(0, 1, 0)) < 0.4) {
                        if(entity instanceof ItemEntity) {
                            if(sculkJawBlockEntity.getHasCombined()) {
                                return;
                            }
                            serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true).setValue(BITE, false).setValue(STOP_BITE, false));
                            entity.kill(serverLevel);
                            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                    ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            serverLevel.scheduleTick(blockPos, this, 8);
                        }
                        else {
                            sculkJawBlockEntity.addBiteDamageEntity(entity.getUUID());
                            biteDamage(level, blockPos, blockState, entity);
                        }
                        entity.push(blockPos.getCenter().add(0, 0.2, 0).subtract(entity.position()).multiply(new Vec3(0.5, 0.5, 0.5)));
                        entity.setShiftKeyDown(false);
                        entity.push(entity.blockPosition().below().getCenter().add(0, 0.3, 0).subtract(entity.position()).multiply(new Vec3(0.3, 0.1, 0.3)));
                        entity.makeStuckInBlock(entity.level().getBlockState(entity.blockPosition().below()), new Vec3(0.5, 1, 0.5));
                    }
                }));
            }
        }
        super.stepOn(level, blockPos, blockState, entity);
    }

    @Override
    protected void onProjectileHit(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if(projectile.distanceToSqr(blockPos.getCenter().add(0, 0.5, 0)) <= 0.3) {
                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true).setValue(BITE, false).setValue(STOP_BITE, false));
                projectile.kill(serverLevel);
                level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                        ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                serverLevel.scheduleTick(blockPos, this, 8);
                IS_BITING_PROJECTILE = true;
            }
        }
        super.onProjectileHit(level, blockState, blockHitResult, projectile);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if(blockState.getValue(START_BITE) || blockState.getValue(BITE) || blockState.getValue(STOP_BITE)) {
            Direction direction = Direction.getRandom(randomSource);
            if (direction != Direction.UP && direction != Direction.DOWN) {
                double d = (double)blockPos.getX() + 0.5 + (direction.getStepX() == 0 ? 0.5 - randomSource.nextDouble() : (double)direction.getStepX() * 0.5);
                double e = (double)blockPos.getY() + 1.05;
                double f = (double)blockPos.getZ() + 0.5 + (direction.getStepZ() == 0 ? 0.5 - randomSource.nextDouble() : (double)direction.getStepZ() * 0.5);
                double g = (double)randomSource.nextFloat() * 0.04;
                double h = (randomSource.nextFloat() - 0.5) * 0.04;
                double i = (randomSource.nextFloat() - 0.5) * 0.04;
                level.addParticle(ParticleTypes.SCULK_CHARGE_POP, d, e, f, g, h, i);
            }
        }
        super.animateTick(blockState, level, blockPos, randomSource);
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
        Sculkjaw.LOGGER.info("randomTick 1");
        if(true) {

        }
        Sculkjaw.LOGGER.info("randomTick 2");
        /*int d = getRandom(0, 1000, randomSource);
        if(d == 200){
            serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true));
            serverLevel.scheduleTick(blockPos, this, 100);
        }*/
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);
        serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(sculkJawBlockEntity -> {
            if(!sculkJawBlockEntity.getIsLargeEntity()){
                if(blockState.getValue(START_BITE)) {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, false).setValue(BITE, true).setValue(STOP_BITE, false));
                    serverLevel.scheduleTick(blockPos, this, 60);
                }
                else if(blockState.getValue(BITE)) {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, false).setValue(BITE, false).setValue(STOP_BITE, true));
                    serverLevel.scheduleTick(blockPos, this, 8);
                }
                else if(blockState.getValue(STOP_BITE)) {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, false).setValue(BITE, false).setValue(STOP_BITE, false));
                    IS_BITING_PROJECTILE = false;}
                if(sculkJawBlockEntity.getIsAcidingEntity()) {
                    int acidCounter = sculkJawBlockEntity.getAcidCounter();
                    acidCounter++;
                    sculkJawBlockEntity.setAcidCounter(acidCounter);
                    sculkJawBlockEntity.setIsAcidingEntity(false);
                    if(sculkJawBlockEntity.getAcidCounter() == 2) {
                        sculkJawBlockEntity.setAcidCounter(0);
                        sculkJawBlockEntity.setIsEffectingEntity(false);
                    }
                }
            }
            else{
                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, false).setValue(BITE, false).setValue(STOP_BITE, false));
                sculkJawBlockEntity.setIsBitingLargeEntity(false);
                sculkJawBlockEntity.setIsLargeEntity(false);
                IS_BITING_PROJECTILE = false;
            }
        });
    }

    public void biteDamage(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if(checkAboveIsAirOrWater(level, blockPos)) {
            return;
        }
        entity.setShiftKeyDown(false);
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(sculkJawBlockEntity -> {
                if(sculkJawBlockEntity.getIsLargeEntity()){
                    Set<UUID> biteDamageEntities = new HashSet<>(sculkJawBlockEntity.getBiteDamageEntities());
                    for(UUID entityIterator : biteDamageEntities) {
                        Entity targetEntity = serverLevel.getEntity(entityIterator);
                        if(targetEntity == null) {
                            sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                            continue;
                        }
                        if(targetEntity.isAlive()) {
                            serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true).setValue(BITE, false).setValue(STOP_BITE, false));
                            level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            targetEntity.hurtServer(serverLevel, level.damageSources().source(ModDamageSources.SCULK_JAW_BITE), sculkJawBlockEntity.getBiteDamage());
                            serverLevel.scheduleTick(blockPos, this, 8);
                            if(!targetEntity.isAlive()) {
                                sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                                serverLevel.getBlockEntity(blockPos.below(),
                                        ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                                            concentratedSculkEntity.consumeLivingEntityExperience(serverLevel, targetEntity);
                                }));
                                continue;
                            }
                            if(targetEntity.distanceToSqr(blockPos.getCenter().add(0, 0.5, 0)) > 0.7) {
                                sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                            }
                        }
                    }
                    sculkJawBlockEntity.setIsBitingLargeEntity(true);
                }
                else{
                    if(!blockState.getValue(START_BITE)) {
                        serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true).setValue(BITE, false).setValue(STOP_BITE, false));
                        Set<UUID> biteDamageEntities = new HashSet<>(sculkJawBlockEntity.getBiteDamageEntities());
                        for(UUID entityIterator : biteDamageEntities) {
                            Entity targetEntity = serverLevel.getEntity(entityIterator);
                            if(targetEntity == null) {
                                sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                                continue;
                            }
                            if(targetEntity.isAlive()) {
                                level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                                targetEntity.hurtServer(serverLevel, level.damageSources().source(ModDamageSources.SCULK_JAW_BITE), sculkJawBlockEntity.getBiteDamage());
                                serverLevel.scheduleTick(blockPos, this, 8);
                                if(!targetEntity.isAlive()) {
                                    sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                                    serverLevel.getBlockEntity(blockPos.below(),
                                            ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                                        concentratedSculkEntity.consumeLivingEntityExperience(serverLevel, targetEntity);
                                    }));
                                }
                            }
                            else{
                                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, false).setValue(BITE, false).setValue(STOP_BITE, false));
                            }
                        }
                    }
                }
            });
        }
    }

    public void acidDamage(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if(checkAboveIsAirOrWater(level, blockPos)) {
            return;
        }
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(sculkJawBlockEntity -> {
                if((!blockState.getValue(START_BITE))&&(!blockState.getValue(BITE))&&(!blockState.getValue(STOP_BITE))) {
                    Set<UUID> acidDamageEntities = new HashSet<>(sculkJawBlockEntity.getAcidDamageEntities());
                    for(UUID entityIterator : acidDamageEntities) {
                        Entity targetEntity = serverLevel.getEntity(entityIterator);
                        if(targetEntity == null) {
                            sculkJawBlockEntity.removeAcidDamageEntity(entityIterator);
                            continue;
                        }
                        if(targetEntity.isAlive()) {
                            level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSoundEvents.SCULK_JAW_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                            targetEntity.hurtServer(serverLevel, level.damageSources().source(ModDamageSources.SCULK_JAW_ACID), sculkJawBlockEntity.getAcidDamage());
                            serverLevel.scheduleTick(blockPos, this, 20);
                            if(!targetEntity.isAlive()) {
                                sculkJawBlockEntity.removeAcidDamageEntity(entityIterator);
                                serverLevel.getBlockEntity(blockPos.below(),
                                        ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                                    concentratedSculkEntity.consumeLivingEntityExperience(serverLevel, targetEntity);
                                }));
                            }
                        }
                    }
                    sculkJawBlockEntity.setIsAcidingEntity(true);
                }
            });
        }
    }

    public void addEffect(Level level, BlockPos blockPos, BlockState blockState, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier) {
        if(checkAboveIsAirOrWater(level, blockPos)) {
            return;
        }
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(sculkJawBlockEntity -> {
                if(entity instanceof ServerPlayer serverPlayer) {
                    MobEffectInstance mobEffectInstance = null;
                    if(!serverPlayer.hasEffect(ModEffects.SCULKOPHOBIA_EFFECT)) {
                        mobEffectInstance = new MobEffectInstance(ModEffects.SCULKOPHOBIA_EFFECT, 2400, 0, false, false, true);
                        serverPlayer.addEffect(mobEffectInstance, serverPlayer);
                    }
                    else {
                        mobEffectInstance = serverPlayer.getEffect(ModEffects.SCULKOPHOBIA_EFFECT);
                        int amplifier = mobEffectInstance.getAmplifier();
                        mobEffectInstance = new MobEffectInstance(ModEffects.SCULKOPHOBIA_EFFECT, 2400, Math.min(4, (amplifier + 1)), false, false, true);
                        serverPlayer.addEffect(mobEffectInstance, serverPlayer);
                    }
                    sculkJawBlockEntity.setIsEffectingEntity(true);
                }
                else if(entity instanceof LivingEntity livingEntity){
                    MobEffectInstance mobEffectInstance = null;
                    if(!livingEntity.hasEffect(ModEffects.SCULKOPHOBIA_EFFECT)) {
                        mobEffectInstance = new MobEffectInstance(ModEffects.SCULKOPHOBIA_EFFECT, 2400, 0, false, false, true);
                        livingEntity.addEffect(mobEffectInstance, livingEntity);
                    }
                    else {
                        mobEffectInstance = livingEntity.getEffect(ModEffects.SCULKOPHOBIA_EFFECT);
                        int amplifier = mobEffectInstance.getAmplifier();
                        mobEffectInstance = new MobEffectInstance(ModEffects.SCULKOPHOBIA_EFFECT, 2400, Math.min(4, (amplifier + 1)), false, false, true);
                        livingEntity.addEffect(mobEffectInstance, livingEntity);
                    }
                    sculkJawBlockEntity.setIsEffectingEntity(true);
                }
            });
        }
    }

    private boolean checkAboveIsAirOrWater(Level level, BlockPos blockPos) {
        if(level.getBlockState(blockPos.above()).getBlock().equals(Blocks.AIR) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.WATER) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.SCULK_VEIN) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.MOSS_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.PALE_MOSS_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.GLOW_LICHEN) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.BLACK_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.GRAY_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.LIGHT_GRAY_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.WHITE_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.BROWN_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.RED_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.ORANGE_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.YELLOW_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.LIME_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.GREEN_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.LIGHT_BLUE_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.CYAN_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.BLUE_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.PINK_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.MAGENTA_CARPET) ||
                level.getBlockState(blockPos.above()).getBlock().equals(Blocks.PURPLE_CARPET)) {
            return false;
        }
        return true;
    }

    private static boolean isConcentratedSculkDestroied(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return levelReader.getBlockState(blockPos.below()).getBlock().equals(ModBlocks.CONCENTRATED_SCULK) && blockState.getValue(COMBINED) ||
                !blockState.getValue(COMBINED);
    }

    public void setExperiencecReward(int i) {
        EXPERIENCE_REWARD = i;
    }

    private int getRandom(int pMin, int pMax, RandomSource randomSource){
        return randomSource.nextInt(pMax - pMin + 1) + pMin;
    }

    private void playCombinedParticle(BlockPos blockPos, BlockState blockState, Level level) {
        if (!blockState.isAir() && blockState.shouldSpawnTerrainParticles()) {
            VoxelShape voxelShape = COLLISION_SHAPE_COMBINED_CLOSE;
            double d = 0.25;
            voxelShape.forAllBoxes((dx, e, f, g, h, i) -> {
                double j = Math.min(1.0, g - dx);
                double k = Math.min(1.0, h - e);
                double l = Math.min(1.0, i - f);
                int m = Math.max(2, Mth.ceil(j / 0.25));
                int n = Math.max(2, Mth.ceil(k / 0.25));
                int o = Math.max(2, Mth.ceil(l / 0.25));

                for(int p = 0; p < m; ++p) {
                    for(int q = 0; q < n; ++q) {
                        for(int r = 0; r < o; ++r) {
                            double s = ((double)p + 0.5) / (double)m;
                            double t = ((double)q + 0.5) / (double)n;
                            double u = ((double)r + 0.5) / (double)o;
                            double v = s * j + dx;
                            double w = t * k + e;
                            double x = u * l + f;
                            level.addParticle(ParticleTypes.INFESTED, (double)blockPos.getX() + v, (double)blockPos.getY() + w, (double)blockPos.getZ() + x, s - 0.5, t - 0.5, u - 0.5);
                            //this.minecraft.particleEngine.add(new TerrainParticle(this, (double)blockPos.getX() + v, (double)blockPos.getY() + w, (double)blockPos.getZ() + x, s - 0.5, t - 0.5, u - 0.5, blockState, blockPos));
                        }
                    }
                }

            });
        }
    }
}
