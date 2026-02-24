package ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ycpk.sculkandjaw.blocks.blockentities.SculkJellyEntity;
import ycpk.sculkandjaw.core.ExperienceBlockManager;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.registry.ModBlocks;

public class SculkJelly extends HalfTransparentBlock {
    public static final MapCodec<SculkJelly> CODEC = simpleCodec(SculkJelly::new);
    public static final VoxelShape COLLISION_SHAPE = Shapes.or(
            Block.box(2.0, 2.0, 2.0, 14.0, 14.0, 14.0)
    );
    private static final Direction[] ALL_DIRECTIONS = Direction.values();

    public SculkJelly(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<SculkJelly> codec() {
        return CODEC;
    }

    @Override
    protected boolean skipRendering(BlockState blockState, BlockState blockState2, Direction direction) {
        return blockState2.is(this) ? true : super.skipRendering(blockState, blockState2, direction);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return COLLISION_SHAPE;
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, double d) {
        if (!(d < 4.0) && entity instanceof LivingEntity livingEntity) {
            LivingEntity.Fallsounds fallsounds = livingEntity.getFallSounds();
            SoundEvent soundEvent = d < 7.0 ? fallsounds.small() : fallsounds.big();
            entity.playSound(soundEvent, 1.0F, 1.0F);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        double d = Math.abs(entity.getDeltaMovement().y);
        if (d < 0.1 && !entity.isSteppingCarefully()) {
            double e = 0.4 + d * 0.2;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(e, 1.0, e));
        }
        super.stepOn(level, blockPos, blockState, entity);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        absorbExperience(level, blockPos);
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, @Nullable Orientation orientation, boolean bl) {
        absorbExperience(level, blockPos);
    }

    private void absorbExperience(Level level, BlockPos blockPos) {
        if(level instanceof ServerLevel serverLevel && level.getBlockState(blockPos).is(this)) {
            Direction[] allDirections = ALL_DIRECTIONS;
            for(int i = 0; i < allDirections.length; ++i) {
                Direction direction = allDirections[i];
                if(serverLevel.getBlockState(blockPos.relative(direction)).is(ModBlocks.CONCENTRATED_SCULK)) {
                    if(serverLevel.getBlockState(blockPos.relative(direction)).getValue(ConcentratedSculkBlock.COMBINED_WITH_SCULK_JAW)) {
                        serverLevel.getBlockEntity(blockPos.relative(direction),
                                ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                            int experienceReward = concentratedSculkEntity.getExperienceReward() - 5;
                            int m = experienceReward % 10;
                            int n = experienceReward / 10;
                            for(int xp = 0; xp < 10; ++xp) {
                                popExperience(serverLevel, blockPos, n);
                            }
                            popExperience(serverLevel, blockPos, m);
                            concentratedSculkEntity.setExperienceReward(5);
                        }));
                        serverLevel.destroyBlock(blockPos, false);
                    }
                }
                else if(serverLevel.getBlockState(blockPos.relative(direction)).is(ModBlocks.SCULK_JAW)) {
                    if(serverLevel.getBlockState(blockPos.relative(direction)).getValue(SculkJawBlock.COMBINED)) {
                        serverLevel.getBlockEntity(blockPos.relative(direction).below(),
                                ModBlockEntities.CONCENTRATED_SCULK_ENTITY).ifPresent((concentratedSculkEntity -> {
                            int experienceReward = concentratedSculkEntity.getExperienceReward() - 5;
                            int m = experienceReward % 10;
                            int n = experienceReward / 10;
                            for(int xp = 0; xp < 10; ++xp) {
                                popExperience(serverLevel, blockPos, n);
                            }
                            popExperience(serverLevel, blockPos, m);
                            concentratedSculkEntity.setExperienceReward(5);
                        }));
                        serverLevel.destroyBlock(blockPos, false);
                    }
                }
            }
        }
    }
}
