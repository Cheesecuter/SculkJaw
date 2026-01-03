package ycpk.sculkjaw.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import ycpk.sculkjaw.registry.ModBlockEntities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SculkJawBlockEntity extends BlockEntity {
    public SculkJawBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCULK_JAW_BLOCK_ENTITY, pos, state);
        BITE_DAMAGE_ENTITIES = new HashSet<>();
        ACID_DAMAGE_ENTITIES = new HashSet<>();
    }

    private boolean IS_LARGE_ENTITY = false;
    private boolean IS_BITING_LARGE_ENTITY = false;
    private boolean IS_ACIDING_ENTITY = false;
    private boolean IS_EFFECTING_ENTITY = true;
    private boolean HAS_COMBINED = false;
    private float BITE_DAMAGE = 2.0F;
    private float ACID_DAMAGE = 1.0F;
    private int EFFECT_AMPLIFIER = 0;
    private int ACID_COUNTER = 0;
    private int EXPERIENCE_REWARD = 5;
    private Set<UUID> BITE_DAMAGE_ENTITIES = null;
    private Set<UUID> ACID_DAMAGE_ENTITIES = null;

    public void setIsLargeEntity(boolean bl) {this.IS_LARGE_ENTITY = bl;}

    public boolean getIsLargeEntity() {return this.IS_LARGE_ENTITY;}

    public void setIsBitingLargeEntity(boolean bl) {this.IS_BITING_LARGE_ENTITY = bl;}

    public boolean getIsBitingLargeEntity() {return this.IS_BITING_LARGE_ENTITY;}

    public void setIsAcidingEntity(boolean bl) {this.IS_ACIDING_ENTITY = bl;}

    public boolean getIsAcidingEntity() {return this.IS_ACIDING_ENTITY;}

    public void setIsEffectingEntity(boolean bl) {this.IS_EFFECTING_ENTITY = bl;}

    public boolean getIsEffectingEntity() {return this.IS_EFFECTING_ENTITY;}

    public void setBiteDamage(float f) {this.BITE_DAMAGE = f;}

    public float getBiteDamage() {return this.BITE_DAMAGE;}

    public void setAcidDamage(float f) {this.ACID_DAMAGE = f;}

    public float getAcidDamage() {return this.ACID_DAMAGE;}

    public void setEffectAmplifier(int i) {this.EFFECT_AMPLIFIER = i;}

    public int getEffectAmplifier() {return this.EFFECT_AMPLIFIER;}

    public void setAcidCounter(int i) {this.ACID_COUNTER = i;}

    public int getAcidCounter() {return this.ACID_COUNTER;}

    public void setHasCombined(boolean bl) {this.HAS_COMBINED = bl;}

    public boolean getHasCombined() {return this.HAS_COMBINED;}

    public void addBiteDamageEntity(UUID uuid) {this.BITE_DAMAGE_ENTITIES.add(uuid);}

    public Set<UUID> getBiteDamageEntities() {return this.BITE_DAMAGE_ENTITIES;}

    public void removeBiteDamageEntity(UUID uuid) {this.BITE_DAMAGE_ENTITIES.remove(uuid);}

    public void addAcidDamageEntity(UUID uuid) {this.ACID_DAMAGE_ENTITIES.add(uuid);}

    public Set<UUID> getAcidDamageEntities() {return this.ACID_DAMAGE_ENTITIES;}

    public void removeAcidDamageEntity(UUID uuid) {this.ACID_DAMAGE_ENTITIES.remove(uuid);}

    public void setExperienceReward(int i) {this.EXPERIENCE_REWARD = i;}

    public int getExperienceReward() {return this.EXPERIENCE_REWARD;}

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        valueOutput.putBoolean("IS_LARGE_ENTITY", IS_LARGE_ENTITY);
        valueOutput.putBoolean("IS_ACIDING_ENTITY", IS_ACIDING_ENTITY);
        valueOutput.putBoolean("IS_EFFECTING_ENTITY", IS_EFFECTING_ENTITY);
        valueOutput.putBoolean("HAS_COMBINED", HAS_COMBINED);
        valueOutput.putFloat("BITE_DAMAGE", BITE_DAMAGE);
        valueOutput.putFloat("ACID_DAMAGE", ACID_DAMAGE);
        valueOutput.putInt("EFFECT_AMPLIFIER", EFFECT_AMPLIFIER);
        valueOutput.putInt("ACID_COUNTER", ACID_COUNTER);
        valueOutput.putInt("EXPERIENCE_REWARD", EXPERIENCE_REWARD);
        super.saveAdditional(valueOutput);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.IS_LARGE_ENTITY = valueInput.getBooleanOr("IS_LARGE_ENTITY", false);
        this.IS_ACIDING_ENTITY = valueInput.getBooleanOr("IS_ACIDING_ENTITY", false);
        this.IS_EFFECTING_ENTITY = valueInput.getBooleanOr("IS_EFFECTING_ENTITY", true);
        this.HAS_COMBINED = valueInput.getBooleanOr("HAS_COMBINED", false);
        this.BITE_DAMAGE = valueInput.getFloatOr("BITE_DAMAGE", 2.0F);
        this.ACID_DAMAGE = valueInput.getFloatOr("ACID_DAMAGE", 1.0F);
        this.EFFECT_AMPLIFIER = valueInput.getIntOr("EFFECT_AMPLIFIER", 0);
        this.ACID_COUNTER = valueInput.getIntOr("ACID_COUNTER", 0);
        this.EXPERIENCE_REWARD = valueInput.getIntOr("EXPERIENCE_REWARD", 5);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SculkJawBlockEntity blockEntity) {
        //Sculkjaw.LOGGER.info("ticking every 20 ticks");

    }
}
