package ycpk.sculkandjaw.level.material;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;
import ycpk.sculkandjaw.registry.ModBlocks;
import ycpk.sculkandjaw.registry.ModItems;
import ycpk.sculkandjaw.registry.ModMobEffects;
import ycpk.sculkandjaw.registry.ModSoundEvents;
import ycpk.sculkandjaw.tags.ModFluidTags;
import ycpk.sculkandjaw.world.level.ModGameRules;

import java.util.Optional;

public abstract class SculkAcidFluid extends FlowingFluid implements ModFluidBlockBehavior {

    public SculkAcidFluid() {
    }

    @Override
    public Fluid getFlowing() {return ModFluids.FLOWING_SCULK_ACID;}

    @Override
    public Fluid getSource() {return ModFluids.SCULK_ACID;}

    @Override
    public Item getBucket() {return ModItems.SCULK_ACID_BUCKET;}

    @Override
    public void animateTick(Level level, BlockPos blockPos, FluidState fluidState, RandomSource randomSource) {
        BlockPos blockPos2 = blockPos.above();
        if (level.getBlockState(blockPos2).isAir() && !level.getBlockState(blockPos2).isSolidRender(level, blockPos2)) {
            if (randomSource.nextInt(100) == 0) {
                double d = (double)blockPos.getX() + randomSource.nextDouble();
                double e = (double)blockPos.getY() + 0.5;
                double f = (double)blockPos.getZ() + randomSource.nextDouble();
                level.addParticle(ModParticleTypes.SCULK_ACID_BUBBLE_PARTICLE, d, e, f, 0.0, 0.02, 0.0);
                level.playLocalSound(d, e, f, ModSoundEvents.SCULK_ACID_BUBBLE_EMERGE, SoundSource.AMBIENT, 0.2F + randomSource.nextFloat() * 0.2F, 0.9F + randomSource.nextFloat() * 0.15F, false);
            }

            if (randomSource.nextInt(200) == 0) {
                level.playLocalSound((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), ModSoundEvents.SCULLK_ACID_FLOW, SoundSource.AMBIENT, 0.2F + randomSource.nextFloat() * 0.2F, 0.9F + randomSource.nextFloat() * 0.15F, false);
            }
        }
    }

    @Nullable
    public ParticleOptions getDripParticle() {
        return ModParticleTypes.DRIPPING_SCULK_ACID;
    }

    @Override
    protected boolean canConvertToSource(Level serverLevel) {
        return serverLevel.getGameRules().getBoolean(ModGameRules.RULE_SCULK_ACID_SOURCE_CONVERSION);
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = blockState.hasBlockEntity() ? levelAccessor.getBlockEntity(blockPos) : null;
        Block.dropResources(blockState, levelAccessor, blockPos, blockEntity);
    }

    @Override
    public void entityInside(Level level, BlockPos blockPos, Entity entity) {
        if (level instanceof ServerLevel serverLevel) {
            if (entity instanceof LivingEntity livingEntity) {
                MobEffectInstance mobEffectInstance = null;
                mobEffectInstance = new MobEffectInstance(ModMobEffects.ACID_ETCHING, 20, 2, false, false, true);
                livingEntity.addEffect(mobEffectInstance);
            }
        }
    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) {return 3;}

    @Override
    protected BlockState createLegacyBlock(FluidState fluidState) {
        return (BlockState) ModBlocks.SCULK_ACID.defaultBlockState().setValue(LiquidBlock.LEVEL, getSculkAcidLegacyLevel(fluidState));
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == ModFluids.SCULK_ACID || fluid == ModFluids.FLOWING_SCULK_ACID;
    }

    @Override
    protected int getDropOff(LevelReader levelReader) {return 1;}

    @Override
    public int getTickDelay(LevelReader levelReader) {return 10;}

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(ModFluidTags.SCULK_ACID);
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    @Override
    public void tick(Level serverLevel, BlockPos blockPos, FluidState fluidState) {
        super.tick(serverLevel, blockPos, fluidState);
    }

    @Override
    public int getSpreadDelay(Level level, BlockPos blockPos, FluidState fluidState, FluidState fluidState2) {
        int i = this.getTickDelay(level);
        if (!fluidState.isEmpty() && !fluidState2.isEmpty() && !(Boolean)fluidState.getValue(FALLING) && !(Boolean)fluidState2.getValue(FALLING) && fluidState2.getHeight(level, blockPos) > fluidState.getHeight(level, blockPos) && level.getRandom().nextInt(4) != 0) {
            i *= 4;
        }
        return i;
    }

    @Override
    protected void spreadTo(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Direction direction, FluidState fluidState) {
        FluidState fluidState2 = levelAccessor.getFluidState(blockPos);
        if (this.is(ModFluidTags.SCULK_ACID) && fluidState2.is(FluidTags.WATER)) {
            if (blockState.getBlock() instanceof LiquidBlock) {
                levelAccessor.setBlock(blockPos, ModBlocks.SCULK_JELLY.defaultBlockState(), 3);
                levelAccessor.playSound(null, blockPos, SoundEvents.SLIME_BLOCK_STEP, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return;
        }
        if (this.is(ModFluidTags.SCULK_ACID) && fluidState2.is(FluidTags.LAVA)) {
            if (blockState.getBlock() instanceof LiquidBlock) {
                levelAccessor.setBlock(blockPos, ModBlocks.SCULK_JELLY.defaultBlockState(), 3);
                levelAccessor.addParticle(ModParticleTypes.SCULK_ACID_BUBBLE_PARTICLE,
                        (double)blockPos.getX() + levelAccessor.getRandom().nextDouble(),
                        (double)blockPos.getY() + 0.5,
                        (double)blockPos.getZ() + levelAccessor.getRandom().nextDouble(),
                        0, 0.02, 0);
                levelAccessor.playSound(null, blockPos, SoundEvents.SLIME_BLOCK_STEP, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return;
        }

        super.spreadTo(levelAccessor, blockPos, blockState, direction, fluidState);
    }

    protected int getSculkAcidLegacyLevel(FluidState fluidState) {
        return fluidState.isSource() ? 0 : 8 - Math.min(fluidState.getAmount(), 8) + ((Boolean) fluidState.getValue(FALLING) ? 8 : 0);
    }

    public static class Flowing extends SculkAcidFluid {
        public Flowing() {
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(new Property[]{LEVEL});
        }

        public int getAmount(FluidState fluidState) {return (Integer) fluidState.getValue(LEVEL);}

        public boolean isSource(FluidState fluidState) {return false;}
    }

    public static class Source extends SculkAcidFluid {
        private int aAmount = 8;
        public Source() {
        }

        public int getAmount(FluidState fluidState) {return aAmount;}

        public void addAmount(FluidState fluidState) {aAmount = Math.min(8, aAmount + 2);}

        public boolean isSource(FluidState fluidState) {return true;}
    }
}
