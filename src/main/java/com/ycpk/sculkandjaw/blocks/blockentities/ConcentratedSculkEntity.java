package com.ycpk.sculkandjaw.blocks.blockentities;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.blocks.modblocks.SculkJelly;
import com.ycpk.sculkandjaw.registry.ModBlockEntities;
import com.ycpk.sculkandjaw.registry.ModBlocks;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public class ConcentratedSculkEntity extends BlockEntity implements GameEventListener.Provider<ConcentratedSculkEntity.ConcentratedSculkListener> {
    private final ConcentratedSculkListener concentratedSculkListener;
    private boolean HAS_COMBINED_WITH_SCULK_JAW = false;
    private int EXPERIENCE_REWARD = 5;
    private static final Direction[] ALL_DIRECTIONS = Direction.values();

    public ConcentratedSculkEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CONCENTRATED_SCULK_ENTITY.get(), blockPos, blockState);
        this.concentratedSculkListener = new ConcentratedSculkListener(blockState, blockPos, new BlockPositionSource(blockPos));
    }

    public ConcentratedSculkListener getListener() {
        return this.concentratedSculkListener;
    }

    public void setHasCombinedWithSculkJaw(boolean bl) {
        this.HAS_COMBINED_WITH_SCULK_JAW = bl;
    }

    public boolean getHasCombinedWithSculkJaw() {
        return this.HAS_COMBINED_WITH_SCULK_JAW;
    }

    public boolean consumeLivingEntityExperience(ServerLevel serverLevel, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && this.HAS_COMBINED_WITH_SCULK_JAW) {
            if (!livingEntity.wasExperienceConsumed()) {
                int i = livingEntity.getExperienceReward(serverLevel, livingEntity);
                this.EXPERIENCE_REWARD += i;
                livingEntity.skipDropExperience();
                return true;
            }
        }
        return false;
    }

    public void setExperienceReward(int i) {
        this.EXPERIENCE_REWARD = i;
    }

    public void addExperienceReward(int i) {
        this.EXPERIENCE_REWARD += i;
    }

    public int getExperienceReward() {
        return this.EXPERIENCE_REWARD;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putInt("EXPERIENCE_REWARD", EXPERIENCE_REWARD);
        compoundTag.putBoolean("HAS_COMBINED_WITH_SCULK_JAW", HAS_COMBINED_WITH_SCULK_JAW);
        super.saveAdditional(compoundTag, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.HAS_COMBINED_WITH_SCULK_JAW = compoundTag.getBoolean("HAS_COMBINED_WITH_SCULK_JAW");
        this.EXPERIENCE_REWARD = compoundTag.getInt("EXPERIENCE_REWARD");
    }

    public static class ConcentratedSculkListener implements GameEventListener {
        private final BlockState blockState;
        private final BlockPos blockPos;
        private final PositionSource positionSource;

        public ConcentratedSculkListener(BlockState blockState, BlockPos blockPos, PositionSource positionSource) {
            this.blockState = blockState;
            this.blockPos = blockPos;
            this.positionSource = positionSource;
        }

        @Override
        public PositionSource getListenerSource() {
            return this.positionSource;
        }

        @Override
        public int getListenerRadius() {
            return 0;
        }

        @Override
        public boolean handleGameEvent(ServerLevel serverLevel, Holder<GameEvent> holder, GameEvent.Context context, Vec3 vec3) {
            if (holder.is(GameEvent.ENTITY_DIE)) {
                Entity sourceEntity = context.sourceEntity();
                if (sourceEntity instanceof LivingEntity livingEntity) {
                    if (!livingEntity.wasExperienceConsumed()) {
                        DamageSource damageSource = livingEntity.getLastDamageSource();
                        int i = livingEntity.getExperienceReward(serverLevel, (Entity) Optionull.map(damageSource, DamageSource::getEntity));
                        if (livingEntity.shouldDropExperience() && i > 0) {
                            serverLevel.getBlockEntity(blockPos.above(),
                                    ModBlockEntities.SCULK_JAW_BLOCK_ENTITY.get()).ifPresent((sculkJawBlockEntity -> {
                                sculkJawBlockEntity.addExperienceReward(i);
                            }));
                            serverLevel.getBlockEntity(blockPos,
                                    ModBlockEntities.CONCENTRATED_SCULK_ENTITY.get()).ifPresent((concentratedSculkEntity -> {
                                concentratedSculkEntity.addExperienceReward(i);
                            }));
                        }
                        livingEntity.skipDropExperience();
                        if (!tryScanSculkJelly(serverLevel, blockPos)) {
                            tryScanSculkJelly(serverLevel, blockPos.above());
                        }
                    }
                }
            }
            return false;
        }

        private boolean tryScanSculkJelly(ServerLevel serverLevel, BlockPos blockPos) {
            Direction[] allDirections = ALL_DIRECTIONS;
            for (int i = 0; i < allDirections.length; ++i) {
                Direction direction = allDirections[i];
                if (serverLevel.getBlockState(blockPos.relative(direction)).is(ModBlocks.SCULK_JELLY.get())) {
                    try {
                        SculkJelly sculkJelly = (SculkJelly) serverLevel.getBlockState(blockPos.relative(direction)).getBlock();
                        sculkJelly.absorbExperience(serverLevel, blockPos.relative(direction));
                        return true;
                    }
                    catch (Exception e) {
                        SculkAndJaw.LOGGER.info("Scan sculk jelly failed: " + e);
                    }
                }
            }
            return false;
        }
    }
}
