package ycpk.sculkandjaw.blocks.blockentities;

import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
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
import ycpk.sculkandjaw.registry.ModBlockEntities;

public class SculkAggregatorBlockEntity extends BlockEntity implements GameEventListener.Holder<SculkAggregatorBlockEntity.SculkAggregatorListener>{
    private final SculkAggregatorListener concentratedSculkListener;
    private boolean HAS_COMBINED_WITH_SCULK_JAW = false;
    private int EXPERIENCE_REWARD = 5;

    public SculkAggregatorBlockEntity(BlockPos blockPos, BlockState blockstate) {
        super(ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY, blockPos, blockstate);
        this.concentratedSculkListener = new SculkAggregatorListener(blockstate, blockPos, new BlockPositionSource(blockPos));
    }

    public SculkAggregatorListener getListener() {
        return this.concentratedSculkListener;
    }

    public void setHasCombinedWithSculkJaw(boolean bl) {
        this.HAS_COMBINED_WITH_SCULK_JAW = bl;
    }

    public boolean getHasCombinedWithSculkJaw() {
        return this.HAS_COMBINED_WITH_SCULK_JAW;
    }

    public boolean consumeLivingEntityExperience(ServerLevel serverLevel, Entity entity) {
        if(entity instanceof LivingEntity livingEntity && this.HAS_COMBINED_WITH_SCULK_JAW) {
            if(!livingEntity.wasExperienceConsumed()) {
                int i = livingEntity.getExperienceReward();
                this.EXPERIENCE_REWARD += i;
                livingEntity.skipDropExperience();
                return true;
            }
        }
        return false;
    }

    public void setExperienceReward(int i) {this.EXPERIENCE_REWARD = i;}

    public void addExperienceReward(int i) {this.EXPERIENCE_REWARD += i;}

    public int getExperienceReward() {return this.EXPERIENCE_REWARD;}

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        compoundTag.putInt("EXPERIENCE_REWARD", EXPERIENCE_REWARD);
        compoundTag.putBoolean("HAS_COMBINED_WITH_SCULK_JAW", HAS_COMBINED_WITH_SCULK_JAW);
        super.saveAdditional(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.HAS_COMBINED_WITH_SCULK_JAW = compoundTag.getBoolean("HAS_COMBINED_WITH_SCULK_JAW");
        this.EXPERIENCE_REWARD = compoundTag.getInt("EXPERIENCE_REWARD");
    }

    public static class SculkAggregatorListener implements GameEventListener {
        private final BlockState blockState;
        private final BlockPos blockPos;
        private final PositionSource positionSource;

        public SculkAggregatorListener(BlockState blockState, BlockPos blockPos, PositionSource positionSource) {
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
        public boolean handleGameEvent(ServerLevel serverLevel, GameEvent gameEvent, GameEvent.Context context, Vec3 vec3) {
            if(gameEvent == GameEvent.ENTITY_DIE) {
                Entity sourceEntity = context.sourceEntity();
                if(sourceEntity instanceof LivingEntity livingEntity) {
                    if(!livingEntity.wasExperienceConsumed()) {
                        int i = livingEntity.getExperienceReward();
                        if(livingEntity.shouldDropExperience() && i > 0) {
                            serverLevel.getBlockEntity(blockPos.above(),
                                    ModBlockEntities.SCULK_JAW_BLOCK_ENTITY).ifPresent((sculkJawBlockEntity -> {
                                sculkJawBlockEntity.addExperienceReward(i);
                            }));
                            serverLevel.getBlockEntity(blockPos,
                                    ModBlockEntities.SCULK_AGGREGATOR_BLOCK_ENTITY).ifPresent((concentratedSculkEntity -> {
                                concentratedSculkEntity.addExperienceReward(i);
                            }));
                        }
                        livingEntity.skipDropExperience();
                    }
                }
            }

            return false;
        }
    }
}
