package ycpk.sculkjaw.level.material;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import ycpk.sculkjaw.registry.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public abstract class SculkAcidFluid extends FlowingFluid {
    private Set<UUID> ACID_DAMAGE_ENTITIES = null;
    private Boolean IS_ACIDING_ENTITIES = false;

    public SculkAcidFluid() {
        ACID_DAMAGE_ENTITIES = new HashSet<>();
    }

    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_SCULK_ACID;
    }

    @Override
    public Fluid getSource() {
        return ModFluids.SCULK_ACID;
    }

    @Override
    public Item getBucket() {
        return ModItems.SCULK_ACID_BUCKET;
    }

    @Override
    public void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {
        BlockPos blockPos2 = blockPos.above();
        if (level.getBlockState(blockPos2).isAir() && !level.getBlockState(blockPos2).isSolidRender()) {
            if (randomSource.nextInt(100) == 0) {
                double d = (double)blockPos.getX() + randomSource.nextDouble();
                double e = (double)blockPos.getY() + 1.0;
                double f = (double)blockPos.getZ() + randomSource.nextDouble();
                level.addParticle(ParticleTypes.LAVA, d, e, f, 0.0, 0.0, 0.0);
                level.playLocalSound(d, e, f, SoundEvents.LAVA_POP, SoundSource.AMBIENT, 0.2F + randomSource.nextFloat() * 0.2F, 0.9F + randomSource.nextFloat() * 0.15F, false);
            }

            if (randomSource.nextInt(200) == 0) {
                level.playLocalSound((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.AMBIENT, 0.2F + randomSource.nextFloat() * 0.2F, 0.9F + randomSource.nextFloat() * 0.15F, false);
            }
        }
    }

    @Override
    public void tick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        Set<UUID> acidDamageEntities = new HashSet<>(ACID_DAMAGE_ENTITIES);
        for(UUID entityIterator : acidDamageEntities) {
            Entity targetEntity = serverLevel.getEntity(entityIterator);
            if(targetEntity == null) {
                ACID_DAMAGE_ENTITIES.remove(entityIterator);
                continue;
            }
            if(targetEntity.isAlive()) {
                serverLevel.playSound(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSoundEvents.SCULK_JAW_ACID, SoundSource.BLOCKS, 1.0F, 1.0F);
                targetEntity.hurtServer(serverLevel, serverLevel.damageSources().source(ModDamageSources.SCULK_JAW_ACID), 2.0F);
                if(!targetEntity.isAlive()) {
                    ACID_DAMAGE_ENTITIES.remove(entityIterator);
                }
            }
        }
    }

    @Override
    protected void entityInside(Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier) {
        if(level instanceof ServerLevel serverLevel) {
            ACID_DAMAGE_ENTITIES.add(entity.getUUID());
            serverLevel.scheduleTick(blockPos, this, 20);
        }
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return fluidState.getHeight(blockGetter, blockPos) >= 0.44444445F && fluid.is(FluidTags.WATER);
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 10;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState fluidState) {
        return (BlockState) ModBlocks.SCULK_ACID.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
    }

    @Override
    protected boolean canConvertToSource(ServerLevel serverLevel) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = blockState.hasBlockEntity() ? levelAccessor.getBlockEntity(blockPos) : null;
        Block.dropResources(blockState, levelAccessor, blockPos, blockEntity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader levelReader) {
        return 1;
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == ModFluids.SCULK_ACID || fluid == ModFluids.FLOWING_SCULK_ACID;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    public static class Flowing extends SculkAcidFluid {
        public Flowing() {

        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(new Property[]{LEVEL});
        }

        public int getAmount(FluidState fluidState) {
            return (Integer) fluidState.getValue(LEVEL);
        }

        public boolean isSource(FluidState fluidState) {
            return false;
        }
    }

    public static class Source extends SculkAcidFluid {
        public Source() {

        }

        public int getAmount(FluidState fluidState) {
            return 8;
        }

        public boolean isSource(FluidState fluidState) {
            return true;
        }
    }
}
