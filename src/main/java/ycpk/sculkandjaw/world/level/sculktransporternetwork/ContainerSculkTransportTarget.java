package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import ycpk.sculkandjaw.blocks.blockentities.SculkTransporterBlockEntity;

public class ContainerSculkTransportTarget implements SculkTransporterTarget {
    private final Container container;

    public ContainerSculkTransportTarget(Container container) {
        this.container = container;
    }

    @Override
    public boolean canInsert(ItemStack sourceStack) {
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            if (canInsertIntoSlot(slot, sourceStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int insert(ItemStack sourceStack, int amount) {
        int remaining = amount;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            if (remaining <= 0) {
                break;
            }
            ItemStack targetStack = container.getItem(slot);
            if (targetStack.isEmpty()) {
                continue;
            }
            if (!container.canPlaceItem(slot, sourceStack)) {
                continue;
            }
            if (!ItemStack.isSameItemSameComponents(sourceStack, targetStack)) {
                continue;
            }
            int maxStackSize = targetStack.getMaxStackSize();
            int freeSpace = maxStackSize - targetStack.getCount();
            if (freeSpace <= 0) {
                continue;
            }
            int moveAmount = Math.min(remaining, freeSpace);
            targetStack.grow(moveAmount);
            remaining -= moveAmount;
        }
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            if (remaining <= 0) {
                break;
            }
            ItemStack targetStack = container.getItem(slot);
            if (!targetStack.isEmpty()) {
                continue;
            }
            if (!container.canPlaceItem(slot, targetStack)) {
                continue;
            }
            int moveAmount = Math.min(remaining, sourceStack.getMaxStackSize());
            ItemStack inserted = sourceStack.copy();
            inserted.setCount(moveAmount);
            container.setItem(slot, inserted);
            remaining -= moveAmount;
        }
        int insertedAmount = amount - remaining;
        if (insertedAmount > 0) {
            container.setChanged();
        }
        return insertedAmount;
    }

    @Override
    public void onItemInserted(int amount) {
        if (container instanceof SculkTransporterBlockEntity sculkTransporterBlockEntity) {
            sculkTransporterBlockEntity.setTransferCooldown(SculkTransporterBlockEntity.MOVE_ITEM_SPEED);
        }
    }

    private boolean canInsertIntoSlot(int slot, ItemStack itemStack) {
        if (!container.canPlaceItem(slot, itemStack)) {
            return false;
        }
        ItemStack targetStack = container.getItem(slot);
        if (targetStack.isEmpty()) {
            return true;
        }
        return ItemStack.isSameItemSameComponents(itemStack, targetStack) &&
                targetStack.getCount() < targetStack.getMaxStackSize();
    }
}
