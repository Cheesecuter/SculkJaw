package ycpk.sculkandjaw.blocks.modblocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SculkJelly extends HalfTransparentBlock {
    public static final MapCodec<SculkJelly> CODEC = simpleCodec(SculkJelly::new);
    public static final VoxelShape COLLISION_SHAPE = Shapes.or(
            Block.box(2.0, 2.0, 2.0, 14.0, 14.0, 14.0)
    );

    public SculkJelly(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<SculkJelly> codec() {
        return CODEC;
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
}
