package ycpk.sculkandjaw.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import ycpk.sculkandjaw.registry.ModBlockEntities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SculkJawBlockEntity extends BlockEntity implements RandomizableContainer {
    public SculkJawBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCULK_JAW_BLOCK_ENTITY, pos, state);
        this.aBiteDamageEntities = new HashSet<>();
        this.aAcidDamageEntities = new HashSet<>();
        this.aSculkophobiaEffectEntities = new HashSet<>();
        this.aItems = NonNullList.withSize(27, ItemStack.EMPTY);
    }

    private boolean aIsLargeEntity = false;
    private boolean aIsBitingLargeEntity = false;
    private boolean aIsDecomposingEntity = false;
    private boolean aIsEffectingEntity = true;
    private boolean aHasCombined = false;
    private float aBiteDamage = 2.0F;
    private float aAcidDamage = 1.0F;
    private int aEffectAmplifier = 0;
    private int aAcidCounter = 0;
    private int aExperienceReward = 5;
    public int tickCount = 0;
    private Set<UUID> aBiteDamageEntities = null;
    private Set<UUID> aAcidDamageEntities = null;
    private Set<UUID> aSculkophobiaEffectEntities = null;
    private NonNullList<ItemStack> aItems;

    public void setIsLargeEntity(boolean bl) {this.aIsLargeEntity = bl;}

    public boolean getIsLargeEntity() {return this.aIsLargeEntity;}

    public void setIsBitingLargeEntity(boolean bl) {this.aIsBitingLargeEntity = bl;}

    public boolean getIsBitingLargeEntity() {return this.aIsBitingLargeEntity;}

    public void setIsDecomposingEntity(boolean bl) {this.aIsDecomposingEntity = bl;}

    public boolean getIsDecomposingEntity() {return this.aIsDecomposingEntity;}

    public void setIsEffectingEntity(boolean bl) {this.aIsEffectingEntity = bl;}

    public boolean getIsEffectingEntity() {return this.aIsEffectingEntity;}

    public void setBiteDamage(float f) {this.aBiteDamage = f;}

    public float getBiteDamage() {return this.aBiteDamage;}

    public void setAcidDamage(float f) {this.aAcidDamage = f;}

    public float getAcidDamage() {return this.aAcidDamage;}

    public void setEffectAmplifier(int i) {this.aEffectAmplifier = i;}

    public int getEffectAmplifier() {return this.aEffectAmplifier;}

    public void setAcidCounter(int i) {this.aAcidCounter = i;}

    public int getAcidCounter() {return this.aAcidCounter;}

    public void setHasCombined(boolean bl) {this.aHasCombined = bl;}

    public boolean getHasCombined() {return this.aHasCombined;}

    public void addBiteDamageEntity(UUID uuid) {this.aBiteDamageEntities.add(uuid);}

    public Set<UUID> getBiteDamageEntities() {return this.aBiteDamageEntities;}

    public void removeBiteDamageEntity(UUID uuid) {this.aBiteDamageEntities.remove(uuid);}

    public void addAcidDamageEntity(UUID uuid) {this.aAcidDamageEntities.add(uuid);}

    public Set<UUID> getAcidDamageEntities() {return this.aAcidDamageEntities;}

    public void removeAcidDamageEntity(UUID uuid) {this.aAcidDamageEntities.remove(uuid);}

    public void addSculkophobiaEffectEntity(UUID uuid) {this.aSculkophobiaEffectEntities.add(uuid);}

    public Set<UUID> getSculkophobiaEffectEntities() {return this.aSculkophobiaEffectEntities;}

    public void removeSculkophobiaEffectEntity(UUID uuid) {this.aSculkophobiaEffectEntities.remove(uuid);}

    public void setExperienceReward(int i) {this.aExperienceReward = i;}

    public void addExperienceReward(int i) {this.aExperienceReward += i;}

    public int getExperienceReward() {return this.aExperienceReward;}

    public void setItems(NonNullList<ItemStack> nonNullList) {this.aItems = nonNullList;}

    public NonNullList<ItemStack> getItems() {return this.aItems;}

    private static boolean canMergeItems(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack.getCount() <= itemStack.getMaxStackSize() && ItemStack.isSameItemSameComponents(itemStack, itemStack2);
    }

    public boolean addItem(ItemStack itemStack) {
        for (int i = 0; i < this.aItems.size(); i++) {
            ItemStack itemStack2 = this.aItems.get(i);
            if (canMergeItems(itemStack2, itemStack)) {
                int j = itemStack.getMaxStackSize() - itemStack2.getCount();
                int k = Math.min(itemStack.getCount(), j);
                itemStack.shrink(k);
                itemStack2.grow(k);
                if(itemStack.isEmpty()) {
                    return true;
                }
            }
            else if(itemStack2.isEmpty()) {
                this.aItems.set(i, itemStack);
                itemStack = ItemStack.EMPTY;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putBoolean("aIsLargeEntity", aIsLargeEntity);
        compoundTag.putBoolean("aIsDecomposingEntity", aIsDecomposingEntity);
        compoundTag.putBoolean("aIsEffectingEntity", aIsEffectingEntity);
        compoundTag.putBoolean("aHasCombined", aHasCombined);
        compoundTag.putFloat("aBiteDamage", aBiteDamage);
        compoundTag.putFloat("aAcidDamage", aAcidDamage);
        compoundTag.putInt("aEffectAmplifier", aEffectAmplifier);
        compoundTag.putInt("aAcidCounter", aAcidCounter);
        compoundTag.putInt("aExperienceReward", aExperienceReward);
        if(!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.aItems, provider);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.aIsLargeEntity = compoundTag.getBoolean("aIsLargeEntity");
        this.aIsDecomposingEntity = compoundTag.getBoolean("aIsDecomposingEntity");
        this.aIsEffectingEntity = compoundTag.getBoolean("aIsEffectingEntity");
        this.aHasCombined = compoundTag.getBoolean("aHasCombined");
        this.aBiteDamage = compoundTag.getFloat("aBiteDamage");
        this.aAcidDamage = compoundTag.getFloat("aAcidDamage");
        this.aEffectAmplifier = compoundTag.getInt("aEffectAmplifier");
        this.aAcidCounter = compoundTag.getInt("aAcidCounter");
        this.aExperienceReward = compoundTag.getInt("aExperienceReward");
        this.aItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if(!this.tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, this.aItems, provider);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    public static void clientTick(Level level, BlockPos blockPos, BlockState blockState, SculkJawBlockEntity blockEntity) {
        ++blockEntity.tickCount;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, SculkJawBlockEntity blockEntity) {
        ++blockEntity.tickCount;
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> resourceKey) {

    }

    @Override
    public @Nullable ResourceKey<LootTable> getLootTable() {
        return null;
    }

    @Override
    public void setLootTableSeed(long l) {

    }

    @Override
    public long getLootTableSeed() {
        return 0;
    }

    @Override
    public int getContainerSize() {
        return this.aItems.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.unpackLootTable((Player) null);
        this.getItems().set(i, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
    }

    @Override
    public ItemStack getItem(int i) {
        return (ItemStack) this.aItems.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        this.unpackLootTable((Player) null);
        return ContainerHelper.removeItem(this.getItems(), i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        this.unpackLootTable((Player)null);
        return ContainerHelper.takeItem(this.getItems(), i);
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {

    }
}
