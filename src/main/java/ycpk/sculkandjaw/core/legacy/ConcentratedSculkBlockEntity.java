package ycpk.sculkandjaw.core.legacy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import ycpk.sculkandjaw.registry.ModBlockEntities;

public class ConcentratedSculkBlockEntity extends BlockEntity {
    private boolean HAS_COMBINED_WITH_SCULK_JAW = false;
    private int EXPERIENCE_REWARD = 5;

    public ConcentratedSculkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CONCENTRATED_SCULK_BLOCK_ENTITY, blockPos, blockState);
    }

    public boolean getHasCombinedWithSculkJaw() {return this.HAS_COMBINED_WITH_SCULK_JAW;}

    public int getExperienceReward() {return this.EXPERIENCE_REWARD;}

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        valueOutput.putInt("EXPERIENCE_REWARD", EXPERIENCE_REWARD);
        valueOutput.putBoolean("HAS_COMBINED_WITH_SCULK_JAW", HAS_COMBINED_WITH_SCULK_JAW);
        super.saveAdditional(valueOutput);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.HAS_COMBINED_WITH_SCULK_JAW = valueInput.getBooleanOr("HAS_COMBINED_WITH_SCULK_JAW", false);
        this.EXPERIENCE_REWARD = valueInput.getIntOr("EXPERIENCE_REWARD", 5);
    }
}
