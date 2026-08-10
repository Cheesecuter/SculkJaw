package ycpk.sculkandjaw.blocks.modblocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;
import ycpk.sculkandjaw.core.sculk_jaw.SculkJawInteraction;
import ycpk.sculkandjaw.registry.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SculkJaw extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty START_BITE = BooleanProperty.create("start_bite");
    public static final BooleanProperty STOP_BITE = BooleanProperty.create("stop_bite");
    public static final BooleanProperty BITE = BooleanProperty.create("bite");
    public static final BooleanProperty COMBINED = BooleanProperty.create("combined");
    public static final BooleanProperty ACID_FILLED = BooleanProperty.create("acid_filled");
    private boolean IS_BITING_PROJECTILE = false;
    private int EXPERIENCE_REWARD = 5;
    protected final SculkJawInteraction.InteractionMap interactions;
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
    public static final VoxelShape INSIDE_COLLISION_SHAPE = Block.box(1.0, 1.0, 1.0, 15.0, 14.0, 15.0);
    public static final VoxelShape INSIDE_COLLISION_SHAPE_COMBINED = Block.box(1.0, -15.0, 1.0, 15.0, 14.0, 15.0);

    public SculkJaw(SculkJawInteraction.InteractionMap interactionMap, BlockBehaviour.Properties properties) {
        super(properties);
        this.interactions = interactionMap;
        this.registerDefaultState(
                (BlockState) (
                        (BlockState) (
                                (BlockState) (
                                        (BlockState) (
                                                (BlockState) (
                                                        (BlockState) (
                                                                this.getStateDefinition().any()
                                                        ).setValue(FACING, Direction.NORTH)
                                                ).setValue(START_BITE, false)
                                        ).setValue(BITE, false)
                                ).setValue(STOP_BITE, false)
                        ).setValue(COMBINED, false)
                ).setValue(ACID_FILLED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, START_BITE, BITE, STOP_BITE, COMBINED, ACID_FILLED});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter blockGetter, BlockPos blockPos, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SculkJawBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_CLOSE : COLLISION_SHAPE_CLOSE;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_CLOSE : COLLISION_SHAPE_CLOSE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if(blockState.getValue(ACID_FILLED)) {
            return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_OPEN : COLLISION_SHAPE_OPEN;
        }
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
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.is(blockState2.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof Container) {
                Containers.dropContents(level, blockPos, (Container) blockEntity);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        if (blockState.getValue(COMBINED)) {
            if (EnchantmentHelper.hasSilkTouch(itemStack)) {
                serverLevel.getBlockEntity(blockPos.below(),
                        ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorEntity -> {
                    int experienceReward = sculkAggregatorEntity.getExperienceReward() - EXPERIENCE_REWARD;
                    sculkAggregatorEntity.setExperienceReward(experienceReward);
                }));
            }
            else {
                serverLevel.getBlockEntity(blockPos,
                        ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                    int experienceReward = sculkJawBlockEntity.getExperienceReward() + EXPERIENCE_REWARD;
                    this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(experienceReward));
                }));
            }
        }
        else {
            this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(EXPERIENCE_REWARD));
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if(!blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        else if(levelAccessor.getBlockState(blockPos.below()).getBlock().equals(ModBlocks.SCULK_AGGREGATOR)) {
            levelAccessor.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                if(!sculkJawBlockEntity.getHasCombined()) {
                    sculkJawBlockEntity.setHasCombined(true);
                    sculkJawBlockEntity.setBiteDamage(10.0F);
                    sculkJawBlockEntity.setAcidDamage(15.0F);
                    sculkJawBlockEntity.getLevel().addDestroyBlockEffect(blockPos, blockState);
                    sculkJawBlockEntity.getLevel().addDestroyBlockEffect(blockPos.below(), blockState);
                }
            }));
            levelAccessor.getBlockEntity(blockPos.below(), ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorEntity -> {
                if(!sculkAggregatorEntity.getHasCombinedWithSculkJaw()) {
                    sculkAggregatorEntity.setHasCombinedWithSculkJaw(true);
                }
            }));
            return blockState.setValue(COMBINED, true);
        }
        else {
            super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        }
        return blockState;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if(level instanceof ServerLevel serverLevel && level.getBlockState(blockPos).is(this)) {
            Block block1 = level.getBlockState(blockPos.below()).getBlock();
            if(block1.equals(ModBlocks.SCULK_AGGREGATOR)) {
                serverLevel.getBlockEntity(blockPos.below(),
                        ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorEntity -> {
                    if(!sculkAggregatorEntity.getHasCombinedWithSculkJaw()) {
                        sculkAggregatorEntity.setHasCombinedWithSculkJaw(true);
                    }
                }));
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return isSculkAggregatorDestroied(levelReader, blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY, level.isClientSide() ? SculkJawBlockEntity::clientTick : SculkJawBlockEntity::serverTick);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        SculkJawInteraction sculkJawInteraction = (SculkJawInteraction) this.interactions.map().get(itemStack.getItem());
        return sculkJawInteraction.interact(blockState, level, blockPos, player, interactionHand, itemStack);
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        super.stepOn(level, blockPos, blockState, entity);
        if(blockState.getValue(ACID_FILLED)) {
            return;
        }
        if((entity instanceof LivingEntity || entity instanceof ItemEntity || entity.getFeetBlockState().is(this)) &&
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
                        if(entity instanceof ItemEntity itemEntity) {
                            if(sculkJawBlockEntity.getHasCombined()) {
                                return;
                            }
                            if(sculkJawBlockEntity.addItem(itemEntity.getItem())) {
                                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true).setValue(BITE, false).setValue(STOP_BITE, false));
                                entity.kill();
                                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                                serverLevel.scheduleTick(blockPos, this, 8);
                            }
                        }
                        else {
                            sculkJawBlockEntity.addBiteDamageEntity(entity.getUUID());
                            biteDamage(level, blockPos, blockState, entity);
                        }
                        Vec3 pushDirection = blockPos.getCenter().add(0, 0.2, 0).subtract(entity.position()).multiply(new Vec3(0.5, 0.5, 0.5));
                        entity.push(pushDirection.x, pushDirection.y, pushDirection.z);
                        entity.setShiftKeyDown(false);
                        Vec3 pushDirection2 = entity.blockPosition().below().getCenter().add(0, 0.3, 0).subtract(entity.position()).multiply(new Vec3(0.3, 0.1, 0.3));
                        entity.push(pushDirection2.x, pushDirection2.y, pushDirection2.z);
                        entity.makeStuckInBlock(entity.level().getBlockState(entity.blockPosition().below()), new Vec3(0.5, 1, 0.5));
                    }
                }));
            }
        }
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if((entity instanceof LivingEntity || entity instanceof ItemEntity || entity.getFeetBlockState().is(this)) &&
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
                        if(entity instanceof ItemEntity itemEntity) {
                            if(blockState.getValue(ACID_FILLED)) {
                                entity.kill();
                                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        ModSoundEvents.SCULK_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                                return;
                            }
                            if(sculkJawBlockEntity.getHasCombined()) {
                                return;
                            }
                            if(sculkJawBlockEntity.addItem(itemEntity.getItem())) {
                                entity.kill();
                                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        ModSoundEvents.SCULK_JAW_BURP, SoundSource.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                        else if(entity instanceof Projectile projectile) {
                            if(projectile.getType().equals(EntityType.TRIDENT) ||
                                    projectile.getType().equals(EntityType.ARROW) ||
                                    projectile.getType().equals(EntityType.SPECTRAL_ARROW)) {
                                return;
                            }
                            if(IS_BITING_PROJECTILE || blockState.getValue(ACID_FILLED)) {
                                entity.kill();
                                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        ModSoundEvents.SCULK_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                        else {
                            sculkJawBlockEntity.addAcidDamageEntity(entity.getUUID());
                            sculkJawBlockEntity.addSculkophobiaEffectEntity(entity.getUUID());
                            //entity.makeStuckInBlock(blockState, new Vec3(0.8, 1.5, 0.8));
                            if(!sculkJawBlockEntity.getIsDecomposingEntity()) {
                                acidDamage(level, blockPos, blockState, entity);
                            }
                            if(!sculkJawBlockEntity.getIsEffectingEntity() && !blockState.getValue(ACID_FILLED)) {
                                addSculkophobia(level, blockPos, blockState, entity);
                            }
                            Vec3 pushDirection = blockPos.getCenter().add(0, 0.2, 0).subtract(entity.position()).multiply(new Vec3(0.5, 0.5, 0.5));
                            entity.push(pushDirection.x, pushDirection.y, pushDirection.z);
                        }
                    }
                }));
            }
        }
    }

    @Override
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if(projectile.distanceToSqr(blockPos.getCenter().add(0, 0.5, 0)) <= 0.3) {
                if(projectile.getType().equals(EntityType.TRIDENT) ||
                        projectile.getType().equals(EntityType.ARROW) ||
                        projectile.getType().equals(EntityType.SPECTRAL_ARROW)) {
                    if(blockState.getValue(ACID_FILLED)) {
                        level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                                ModSoundEvents.SCULK_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return;
                    }
                    else {
                        serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true).setValue(BITE, false).setValue(STOP_BITE, false));
                        level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                                ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        serverLevel.scheduleTick(blockPos, this, 8);
                        IS_BITING_PROJECTILE = true;
                        return;
                    }
                }
                if(blockState.getValue(ACID_FILLED)) {
                    projectile.kill();
                    level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                            ModSoundEvents.SCULK_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return;
                }
                else {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true).setValue(BITE, false).setValue(STOP_BITE, false));
                    projectile.kill();
                    level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                            ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    serverLevel.scheduleTick(blockPos, this, 8);
                    IS_BITING_PROJECTILE = true;
                }
            }
        }
        super.onProjectileHit(level, blockState, blockHitResult, projectile);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if(blockState.getValue(START_BITE) || blockState.getValue(BITE) || blockState.getValue(STOP_BITE) || blockState.getValue(ACID_FILLED)) {
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
    public boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
        int d = randomSource.nextInt(0, 1000);
        if(d == 200 && !blockState.getValue(ACID_FILLED)) {
            serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true));
            serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                    ModSoundEvents.SCULK_JAW_BURP, SoundSource.BLOCKS, 1.0F, 1.0F);
            serverLevel.scheduleTick(blockPos, this, 20);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.tick(blockState, serverLevel, blockPos, randomSource);
        serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(sculkJawBlockEntity -> {
            if(!sculkJawBlockEntity.getIsLargeEntity()){
                if(blockState.getValue(START_BITE)) {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, false).setValue(BITE, true).setValue(STOP_BITE, false));
                    serverLevel.scheduleTick(blockPos, this, 20);
                }
                else if(blockState.getValue(BITE)) {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, false).setValue(BITE, false).setValue(STOP_BITE, true));
                    serverLevel.scheduleTick(blockPos, this, 8);
                }
                else if(blockState.getValue(STOP_BITE)) {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, false).setValue(BITE, false).setValue(STOP_BITE, false));
                    IS_BITING_PROJECTILE = false;}
                if(sculkJawBlockEntity.getIsDecomposingEntity()) {
                    int acidCounter = sculkJawBlockEntity.getAcidCounter();
                    acidCounter++;
                    sculkJawBlockEntity.setAcidCounter(acidCounter);
                    sculkJawBlockEntity.setIsDecomposingEntity(false);
                    if(sculkJawBlockEntity.getAcidCounter() == 2) {
                        sculkJawBlockEntity.setAcidCounter(0);
                        sculkJawBlockEntity.setIsEffectingEntity(false);
                    }
                }
                sculkJawBlockEntity.setIsBitingLargeEntity(false);
                sculkJawBlockEntity.setIsLargeEntity(false);
            }
            else if(sculkJawBlockEntity.getIsLargeEntity()){
                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, false).setValue(BITE, false).setValue(STOP_BITE, false));
                sculkJawBlockEntity.setIsBitingLargeEntity(false);
                sculkJawBlockEntity.setIsLargeEntity(false);
                IS_BITING_PROJECTILE = false;
                serverLevel.scheduleTick(blockPos, this, 8);
            }
        });
    }

    public void biteDamage(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if(!checkAboveIsAbleToBite(level, blockPos)) {
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
                            boolean isCombined = sculkJawBlockEntity.getHasCombined();
                            double distanceToSqr = targetEntity.distanceToSqr(blockPos.getCenter().add(0, 0.5, 0));
                            double distanceToSqr2 = 0.0;
                            if(isCombined) {
                                distanceToSqr2 = targetEntity.distanceToSqr(blockPos.below().getCenter().add(0, 0.5, 0));
                            }
                            if(!isCombined && distanceToSqr > 1.0 || (isCombined && distanceToSqr > 1.0 && distanceToSqr2 > 1.0)) {
                                sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                            }
                            else {
                                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(START_BITE, true).setValue(BITE, false).setValue(STOP_BITE, false));
                                level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                                targetEntity.hurt(level.damageSources().source(ModDamageTypes.SCULK_JAW_BITE), sculkJawBlockEntity.getBiteDamage());
                                serverLevel.scheduleTick(blockPos, this, 8);
                                if(!targetEntity.isAlive()) {
                                    sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                                    serverLevel.getBlockEntity(blockPos.below(),
                                            ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorEntity -> {
                                        sculkAggregatorEntity.consumeLivingEntityExperience(serverLevel, targetEntity);
                                    }));
                                }
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
                                boolean isCombined = sculkJawBlockEntity.getHasCombined();
                                double distanceToSqr = targetEntity.distanceToSqr(blockPos.getCenter().add(0, 0.5, 0));
                                double distanceToSqr2 = 0.0;
                                if(isCombined) {
                                    distanceToSqr2 = targetEntity.distanceToSqr(blockPos.below().getCenter().add(0, 0.5, 0));
                                }
                                if(!isCombined && distanceToSqr > 1.0 || (isCombined && distanceToSqr > 1.0 && distanceToSqr2 > 1.0)) {
                                    sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                                }
                                else {
                                    level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                                    targetEntity.hurt(level.damageSources().source(ModDamageTypes.SCULK_JAW_BITE), sculkJawBlockEntity.getBiteDamage());
                                    serverLevel.scheduleTick(blockPos, this, 8);
                                    if(!targetEntity.isAlive()) {
                                        sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                                        serverLevel.getBlockEntity(blockPos.below(),
                                                ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorEntity -> {
                                            sculkAggregatorEntity.consumeLivingEntityExperience(serverLevel, targetEntity);
                                        }));
                                    }
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
        if(!checkAboveIsAbleToBite(level, blockPos)) {
            return;
        }
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(sculkJawBlockEntity -> {
                if((!blockState.getValue(START_BITE))&&(!blockState.getValue(BITE))&&(!blockState.getValue(STOP_BITE))) {
                    Set<UUID> acidDamageEntities = new HashSet<>(sculkJawBlockEntity.getAcidDamageEntities());
                    for(UUID entityIterator : acidDamageEntities) {
                        Entity targetEntity = serverLevel.getEntity(entityIterator);
                        if(targetEntity instanceof LivingEntity livingEntity) {
                            if(livingEntity == null) {
                                sculkJawBlockEntity.removeAcidDamageEntity(entityIterator);
                                continue;
                            }
                            if(livingEntity.isAlive()) {
                                boolean isCombined = sculkJawBlockEntity.getHasCombined();
                                double distanceToSqr = livingEntity.distanceToSqr(blockPos.getCenter().add(0, 0.5, 0));
                                double distanceToSqr2 = 0.0;
                                if(isCombined) {
                                    distanceToSqr2 = livingEntity.distanceToSqr(blockPos.below().getCenter().add(0, 0.5, 0));
                                }
                                if(!isCombined && distanceToSqr > 1.0 || (isCombined && distanceToSqr > 1.0 && distanceToSqr2 > 1.0)) {
                                    sculkJawBlockEntity.removeAcidDamageEntity(entityIterator);
                                }
                                else {
                                    level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSoundEvents.SCULK_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                                    MobEffectInstance mobEffectInstance = null;
                                    mobEffectInstance = new MobEffectInstance(ModMobEffects.ACID_ETCHING, 20, 2, false, false, true);
                                    livingEntity.addEffect(mobEffectInstance);
                                    serverLevel.scheduleTick(blockPos, this, 20);
                                    if(!livingEntity.isAlive()) {
                                        sculkJawBlockEntity.removeAcidDamageEntity(entityIterator);
                                        serverLevel.getBlockEntity(blockPos.below(),
                                                ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorEntity -> {
                                            sculkAggregatorEntity.consumeLivingEntityExperience(serverLevel, livingEntity);
                                        }));
                                    }
                                }
                            }
                        }
                    }
                    sculkJawBlockEntity.setIsDecomposingEntity(true);
                }
            });
        }
    }

    public void addSculkophobia(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        if(!checkAboveIsAbleToBite(level, blockPos)) {
            return;
        }
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(sculkJawBlockEntity -> {
                if((!blockState.getValue(START_BITE))&&(!blockState.getValue(BITE))&&(!blockState.getValue(STOP_BITE))) {
                    Set<UUID> sculkophobiaEffectEntities = new HashSet<>(sculkJawBlockEntity.getSculkophobiaEffectEntities());
                    for(UUID entityIterator : sculkophobiaEffectEntities) {
                        Entity targetEntity = serverLevel.getEntity(entityIterator);
                        if(targetEntity instanceof LivingEntity livingEntity) {
                            if(livingEntity == null) {
                                sculkJawBlockEntity.removeSculkophobiaEffectEntity(entityIterator);
                                continue;
                            }
                            if(livingEntity.isAlive()) {
                                boolean isCombined = sculkJawBlockEntity.getHasCombined();
                                double distanceToSqr = livingEntity.distanceToSqr(blockPos.getCenter().add(0, 0.5, 0));
                                double distanceToSqr2 = 0.0;
                                if(isCombined) {
                                    distanceToSqr2 = livingEntity.distanceToSqr(blockPos.below().getCenter().add(0, 0.5, 0));
                                }
                                if(!isCombined && distanceToSqr > 1.0 || (isCombined && distanceToSqr > 1.0 && distanceToSqr2 > 1.0)) {
                                    sculkJawBlockEntity.removeSculkophobiaEffectEntity(entityIterator);
                                }
                                else {
                                    MobEffectInstance mobEffectInstance = null;
                                    if(!livingEntity.hasEffect(ModMobEffects.SCULKOPHOBIA)) {
                                        mobEffectInstance = new MobEffectInstance(ModMobEffects.SCULKOPHOBIA, 2400, 0, false, true, true);
                                        livingEntity.addEffect(mobEffectInstance, livingEntity);
                                    }
                                    else {
                                        mobEffectInstance = livingEntity.getEffect(ModMobEffects.SCULKOPHOBIA);
                                        int amplifier = mobEffectInstance.getAmplifier();
                                        mobEffectInstance = new MobEffectInstance(ModMobEffects.SCULKOPHOBIA, 2400, Math.min(4, (amplifier + 1)), false, true, true);
                                        livingEntity.addEffect(mobEffectInstance, livingEntity);
                                    }
                                    sculkJawBlockEntity.setIsEffectingEntity(true);
                                    if(!livingEntity.isAlive()) {
                                        sculkJawBlockEntity.removeSculkophobiaEffectEntity(entityIterator);
                                        serverLevel.getBlockEntity(blockPos.below(),
                                                ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorEntity -> {
                                            sculkAggregatorEntity.consumeLivingEntityExperience(serverLevel, livingEntity);
                                        }));
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private boolean checkAboveIsAbleToBite(Level level, BlockPos blockPos) {
        if (level.getBlockState(blockPos.above()).isSolidRender(level, blockPos.above())) {
            return false;
        }
        return true;
    }

    private static boolean isSculkAggregatorDestroied(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return levelReader.getBlockState(blockPos.below()).getBlock().equals(ModBlocks.SCULK_AGGREGATOR) && blockState.getValue(COMBINED) ||
                !blockState.getValue(COMBINED);
    }
}
