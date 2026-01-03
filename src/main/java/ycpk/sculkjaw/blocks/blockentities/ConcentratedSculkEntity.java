package ycpk.sculkjaw.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ycpk.sculkjaw.registry.ModBlockEntities;

public class ConcentratedSculkEntity extends BlockEntity {
    public ConcentratedSculkEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONCENTRATED_SCULK_ENTITY, pos, state);
    }

    private boolean HAS_COMBINED_WITH_SCULK_JAW = false;
    private int XP_COLLECTED = 0;
    private int EXPERIENCE_REWARD = 5;

    public void setHasCombinedWithSculkJaw(boolean bl) {
        this.HAS_COMBINED_WITH_SCULK_JAW = bl;
    }

    public boolean getHasCombinedWithSculkJaw() {
        return this.HAS_COMBINED_WITH_SCULK_JAW;
    }

    public void addXpCollected(int d) {
        this.XP_COLLECTED += d;
    }

    public void setXpCollected(int d) {
        this.XP_COLLECTED = d;
    }

    public int getXpCollected() {
        return this.XP_COLLECTED;
    }

    public boolean consumeLivingEntityExperience(ServerLevel serverLevel, Entity entity) {
        if(entity instanceof LivingEntity livingEntity && this.HAS_COMBINED_WITH_SCULK_JAW) {
            if(!livingEntity.wasExperienceConsumed()) {
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

    public int getExperienceReward() {
        return this.EXPERIENCE_REWARD;
    }
}
