package ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
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
import ycpk.sculkandjaw.blocks.blockentities.SculkJawBlockEntity;
import ycpk.sculkandjaw.core.sculk_jaw.SculkJawInteraction;
import ycpk.sculkandjaw.registry.*;
import ycpk.sculkandjaw.tags.ModEnchantmentTags;
import ycpk.sculkandjaw.world.level.block.state.properties.ModBlockStateProperties;
import ycpk.sculkandjaw.world.level.block.state.properties.SculkJawBiteState;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SculkJawBlock extends BaseEntityBlock {
    public static final MapCodec<SculkJawBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(SculkJawInteraction.CODEC.fieldOf("interactions").forGetter((sculkJawBlock) -> {
            return sculkJawBlock.interactions;
        }), propertiesCodec()).apply(instance, SculkJawBlock::new);
    });
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<SculkJawBiteState> BITE_STATE = ModBlockStateProperties.BITE_STATE;
    public static final BooleanProperty COMBINED = BooleanProperty.create("combined");
    public static final BooleanProperty ACID_FILLED = BooleanProperty.create("acid_filled");
    public int EXPERIENCE_REWARD = 5;
    protected final SculkJawInteraction.InteractionMap interactions;
    public static final VoxelShape COLLISION_SHAPE_OPEN = Shapes.join(
            Block.box(0.0, 0.0, 0.0, 16.0,  16.0, 16.0),
            Block.box(1.0, 1.0, 1.0, 15.0, 16.0, 15.0),
            BooleanOp.ONLY_FIRST
    );
    public static final VoxelShape COLLISION_SHAPE_CLOSE = Shapes.join(
            Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(1.0, 1.0, 1.0, 15.0, 15.0, 15.0),
            BooleanOp.ONLY_FIRST
    );
    public static final VoxelShape COLLISION_SHAPE_COMBINED_OPEN = Shapes.join(
            Block.box(0.0, -16.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(1.0, -15.0, 1.0, 15.0, 16.0, 15.0),
            BooleanOp.ONLY_FIRST
    );
    public static final VoxelShape COLLISION_SHAPE_COMBINED_CLOSE = Shapes.join(
            Block.box(0.0, -16.0, 0.0, 16.0, 16.0, 16.0),
            Block.box(1.0, -15.0, 1.0, 15.0, 15.0, 15.0),
            BooleanOp.ONLY_FIRST
    );
    public static final VoxelShape INSIDE_COLLISION_SHAPE = Block.box(1.0, 1.0, 1.0, 15.0, 14.0, 15.0);
    public static final VoxelShape INSIDE_COLLISION_SHAPE_COMBINED = Block.box(1.0, -15.0, 1.0, 15.0, 14.0, 15.0);

    public SculkJawBlock(SculkJawInteraction.InteractionMap interactionMap, BlockBehaviour.Properties properties) {
        super(properties);
        this.interactions = interactionMap;
        this.registerDefaultState(getStateDefinition().getPossibleStates().getFirst()
                .setValue(FACING, Direction.NORTH)
                .setValue(BITE_STATE, SculkJawBiteState.NOT_BITE)
                .setValue(COMBINED, false)
                .setValue(ACID_FILLED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SculkJawBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY, level.isClientSide() ? SculkJawBlockEntity::clientTick : SculkJawBlockEntity::serverTick);
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
        return Shapes.block();
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Entity entity) {
        return blockState.getValue(COMBINED) ? INSIDE_COLLISION_SHAPE_COMBINED : INSIDE_COLLISION_SHAPE;
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
        if(!blockState.getValue(BITE_STATE).equals(SculkJawBiteState.NOT_BITE)) {
            return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_OPEN : COLLISION_SHAPE_OPEN;
        }
        return blockState.getValue(COMBINED) ? COLLISION_SHAPE_COMBINED_CLOSE : COLLISION_SHAPE_CLOSE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, BITE_STATE, COMBINED, ACID_FILLED});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected void spawnAfterBreak(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, ItemStack itemStack, boolean bl) {
        super.spawnAfterBreak(blockState, serverLevel, blockPos, itemStack, bl);
        if (blockState.getValue(COMBINED)) {
            if (EnchantmentHelper.hasTag(itemStack, ModEnchantmentTags.COMBINED_SCULK_JAW_DROPPING)) {
                /*
                * can't be used
                * */
                serverLevel.getBlockEntity(blockPos.below(),
                        ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorBlockEntity -> {
                    int experienceReward = sculkAggregatorBlockEntity.getExperienceReward() - EXPERIENCE_REWARD;
                    sculkAggregatorBlockEntity.setExperienceReward(experienceReward);
                    this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(experienceReward));
                }));
            }
            else {
                serverLevel.getBlockEntity(blockPos,
                        ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                    int experienceReward = sculkJawBlockEntity.getExperienceReward() - 5;
                    sculkJawBlockEntity.setExperienceReward(experienceReward);
                    this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(experienceReward));
                }));
            }
        }
        else {
            this.tryDropExperience(serverLevel, blockPos, itemStack, ConstantInt.of(EXPERIENCE_REWARD));
        }
    }

    @Override
    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos blockPos2, BlockState blockState2, RandomSource randomSource) {
        if(!blockState.canSurvive(levelReader, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        else if(levelReader.getBlockState(blockPos.below()).getBlock().equals(ModBlocks.SCULK_AGGREGATOR)) {
            levelReader.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                if(!sculkJawBlockEntity.getHasCombined()) {
                    sculkJawBlockEntity.setHasCombined(true);
                    sculkJawBlockEntity.setBiteDamage(6.0F);
                    sculkJawBlockEntity.setAcidDamage(15.0F);
                    sculkJawBlockEntity.getLevel().addDestroyBlockEffect(blockPos, blockState);
                    sculkJawBlockEntity.getLevel().addDestroyBlockEffect(blockPos.below(), blockState);
                }
            }));
            levelReader.getBlockEntity(blockPos.below(), ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorBlockEntity -> {
                if(!sculkAggregatorBlockEntity.getHasCombinedWithSculkJaw()) {
                    sculkAggregatorBlockEntity.setHasCombinedWithSculkJaw(true);
                }
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
            if(block1.equals(ModBlocks.SCULK_AGGREGATOR)) {
                serverLevel.getBlockEntity(blockPos.below(),
                        ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorBlockEntity -> {
                    if(!sculkAggregatorBlockEntity.getHasCombinedWithSculkJaw()) {
                        sculkAggregatorBlockEntity.setHasCombinedWithSculkJaw(true);
                    }
                }));
            }
        }
    }

    @Override
    protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return isSculkAggregatorDestroied(levelReader, blockPos, blockState);
    }

    @Override
    public InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        SculkJawInteraction sculkJawInteraction = (SculkJawInteraction) this.interactions.map().get(itemStack.getItem());
        return sculkJawInteraction.interact(blockState, level, blockPos, player, interactionHand, itemStack);
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        super.stepOn(level, blockPos, blockState, entity);
        if(blockState.getValue(ACID_FILLED)) {
            return;
        }
        if((entity instanceof LivingEntity || entity instanceof ItemEntity || entity.getInBlockState().is(this)) &&
                !blockState.getValue(BITE_STATE).equals(SculkJawBiteState.ON_BITE) && !(entity.getType().is(ModTags.IMMUNE_TO_SCULK_JAW))) {
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
                                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.BEFORE_BITE));
                                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        ModSoundEvents.SCULK_JAW_BURP, SoundSource.BLOCKS, 1.0F, 1.0F);
                                serverLevel.scheduleTick(blockPos, this, 8);
                            }
                            if(sculkJawBlockEntity.addItem(itemEntity.getItem())) {
                                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.BEFORE_BITE));
                                entity.kill(serverLevel);
                                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                                serverLevel.scheduleTick(blockPos, this, 8);
                            }
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
                        if(entity instanceof ItemEntity itemEntity) {
                            if(blockState.getValue(ACID_FILLED)) {
                                entity.kill(serverLevel);
                                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        ModSoundEvents.SCULK_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                                return;
                            }
                            if(sculkJawBlockEntity.getHasCombined()) {
                                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                                        ModSoundEvents.SCULK_JAW_BURP, SoundSource.BLOCKS, 1.0F, 1.0F);
                                return;
                            }
                            if(sculkJawBlockEntity.addItem(itemEntity.getItem())) {
                                entity.kill(serverLevel);
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
                            if(sculkJawBlockEntity.getIsBitingProjectile() || blockState.getValue(ACID_FILLED)) {
                                entity.kill(serverLevel);
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
                                addSculkophobia(level, blockPos, blockState, entity, insideBlockEffectApplier);
                            }
                            entity.push(blockPos.getCenter().add(0, 0.2, 0).subtract(entity.position()).multiply(new Vec3(0.5, 0.5, 0.5)));
                        }
                    }
                }));
            }
        }
    }

    @Override
    protected void onProjectileHit(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile) {
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
                        serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.BEFORE_BITE));
                        level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                                ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        serverLevel.scheduleTick(blockPos, this, 8);
                        serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(
                                (sculkJawBlockEntity) -> {
                                    sculkJawBlockEntity.setIsBitingProjectile(true);
                                }
                        );
                        return;
                    }
                }
                if(blockState.getValue(ACID_FILLED)) {
                    projectile.kill(serverLevel);
                    level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                            ModSoundEvents.SCULK_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return;
                }
                else {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.BEFORE_BITE));
                    projectile.kill(serverLevel);
                    level.playSound(null, projectile.getX(), projectile.getY(), projectile.getZ(),
                            ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    serverLevel.scheduleTick(blockPos, this, 8);
                    serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(
                            (sculkJawBlockEntity) -> {
                                sculkJawBlockEntity.setIsBitingProjectile(true);
                            }
                    );
                }
            }
        }
        super.onProjectileHit(level, blockState, blockHitResult, projectile);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if(!blockState.getValue(BITE_STATE).equals(SculkJawBiteState.NOT_BITE) || blockState.getValue(ACID_FILLED)) {
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
    protected boolean isRandomlyTicking(BlockState blockState) {
        return true;
    }

    @Override
    protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        super.randomTick(blockState, serverLevel, blockPos, randomSource);
        int d = randomSource.nextInt(0, 1000);
        if(d == 200 && !blockState.getValue(ACID_FILLED)) {
            serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.BEFORE_BITE));
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
                if(blockState.getValue(BITE_STATE).equals(SculkJawBiteState.BEFORE_BITE)) {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.ON_BITE));
                    serverLevel.scheduleTick(blockPos, this, 20);
                }
                else if(blockState.getValue(BITE_STATE).equals(SculkJawBiteState.ON_BITE)) {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.AFTER_BITE));
                    serverLevel.scheduleTick(blockPos, this, 8);
                }
                else if(blockState.getValue(BITE_STATE).equals(SculkJawBiteState.AFTER_BITE)) {
                    serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.NOT_BITE));
                    sculkJawBlockEntity.setIsBitingProjectile(false);
                }
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
                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.NOT_BITE));
                sculkJawBlockEntity.setIsBitingLargeEntity(false);
                sculkJawBlockEntity.setIsLargeEntity(false);
                sculkJawBlockEntity.setIsBitingProjectile(false);
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
                                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.BEFORE_BITE));
                                level.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSoundEvents.SCULK_JAW_BITE, SoundSource.BLOCKS, 1.0F, 1.0F);
                                targetEntity.hurtServer(serverLevel, level.damageSources().source(ModDamageTypes.SCULK_JAW_BITE), sculkJawBlockEntity.getBiteDamage());
                                serverLevel.scheduleTick(blockPos, this, 8);
                                if(!targetEntity.isAlive()) {
                                    sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                                    serverLevel.getBlockEntity(blockPos.below(),
                                            ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorBlockEntity -> {
                                        sculkAggregatorBlockEntity.consumeLivingEntityExperience(serverLevel, targetEntity);
                                    }));
                                }
                            }
                        }
                    }
                    sculkJawBlockEntity.setIsBitingLargeEntity(true);
                }
                else{
                    if(!blockState.getValue(BITE_STATE).equals(SculkJawBiteState.BEFORE_BITE)) {
                        serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.BEFORE_BITE));
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
                                    targetEntity.hurtServer(serverLevel, level.damageSources().source(ModDamageTypes.SCULK_JAW_BITE), sculkJawBlockEntity.getBiteDamage());
                                    serverLevel.scheduleTick(blockPos, this, 8);
                                    if(!targetEntity.isAlive()) {
                                        sculkJawBlockEntity.removeBiteDamageEntity(entityIterator);
                                        serverLevel.getBlockEntity(blockPos.below(),
                                                ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorBlockEntity -> {
                                            sculkAggregatorBlockEntity.consumeLivingEntityExperience(serverLevel, targetEntity);
                                        }));
                                    }
                                }
                            }
                            else{
                                serverLevel.setBlockAndUpdate(blockPos, blockState.setValue(BITE_STATE, SculkJawBiteState.NOT_BITE));
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
                if(blockState.getValue(BITE_STATE).equals(SculkJawBiteState.NOT_BITE)) {
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
                                    mobEffectInstance = new MobEffectInstance(ModMobEffects.ACID_ETCHING_EFFECT, 20, 2, false, false, true);
                                    livingEntity.addEffect(mobEffectInstance);
                                    serverLevel.scheduleTick(blockPos, this, 20);
                                    if(!livingEntity.isAlive()) {
                                        sculkJawBlockEntity.removeAcidDamageEntity(entityIterator);
                                        serverLevel.getBlockEntity(blockPos.below(),
                                                ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorBlockEntity -> {
                                            sculkAggregatorBlockEntity.consumeLivingEntityExperience(serverLevel, livingEntity);
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

    public void addSculkophobia(Level level, BlockPos blockPos, BlockState blockState, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier) {
        if(!checkAboveIsAbleToBite(level, blockPos)) {
            return;
        }
        if(level instanceof ServerLevel serverLevel) {
            serverLevel.getBlockEntity(blockPos, ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent(sculkJawBlockEntity -> {
                if(blockState.getValue(BITE_STATE).equals(SculkJawBiteState.NOT_BITE)) {
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
                                    if(!livingEntity.hasEffect(ModMobEffects.SCULKOPHOBIA_EFFECT)) {
                                        mobEffectInstance = new MobEffectInstance(ModMobEffects.SCULKOPHOBIA_EFFECT, 2400, 0, false, true, true);
                                        livingEntity.addEffect(mobEffectInstance, livingEntity);
                                    }
                                    else {
                                        mobEffectInstance = livingEntity.getEffect(ModMobEffects.SCULKOPHOBIA_EFFECT);
                                        int amplifier = mobEffectInstance.getAmplifier();
                                        mobEffectInstance = new MobEffectInstance(ModMobEffects.SCULKOPHOBIA_EFFECT, 2400, Math.min(4, (amplifier + 1)), false, true, true);
                                        livingEntity.addEffect(mobEffectInstance, livingEntity);
                                    }
                                    sculkJawBlockEntity.setIsEffectingEntity(true);
                                    if(!livingEntity.isAlive()) {
                                        sculkJawBlockEntity.removeSculkophobiaEffectEntity(entityIterator);
                                        serverLevel.getBlockEntity(blockPos.below(),
                                                ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((sculkAggregatorBlockEntity -> {
                                            sculkAggregatorBlockEntity.consumeLivingEntityExperience(serverLevel, livingEntity);
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
        if (level.getBlockState(blockPos.above()).isSolidRender()) {
            return false;
        }
        return true;
    }

    private static boolean isSculkAggregatorDestroied(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return levelReader.getBlockState(blockPos.below()).getBlock().equals(ModBlocks.SCULK_AGGREGATOR) && blockState.getValue(COMBINED) ||
                !blockState.getValue(COMBINED);
    }
}
