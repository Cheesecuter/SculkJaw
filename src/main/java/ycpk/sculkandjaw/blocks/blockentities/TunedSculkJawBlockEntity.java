package ycpk.sculkandjaw.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.registry.ModBlockEntities;

public class TunedSculkJawBlockEntity extends BlockEntity implements RandomizableContainer {
    private NonNullList<ItemStack> aItems;

    public TunedSculkJawBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TUNED_SCULK_JAW_BLOCK_ENTITY, blockPos, blockState);
        this.aItems = NonNullList.withSize(27, ItemStack.EMPTY);
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
        return this.aItems.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return (ItemStack) this.aItems.get(i);
    }

    public void setItems(NonNullList<ItemStack> nonNullList) {
        this.aItems = nonNullList;
    }

    public NonNullList<ItemStack> getItems() {
        return this.aItems;
    }

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
    public void setItem(int i, ItemStack itemStack) {
        this.unpackLootTable((Player) null);
        this.getItems().set(i, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {

    }
}
