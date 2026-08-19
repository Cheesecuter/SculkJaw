package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import ycpk.sculkandjaw.blocks.blockentities.SculkTransporterBlockEntity;

public class ContainerSculkTransportTarget implements SculkTransporterTarget {
    private final Container container;
    private final BlockPos blockPos;
    private final Direction direction;

    public ContainerSculkTransportTarget(Container container, BlockPos blockPos, Direction direction) {
        this.container = container;
        this.blockPos = blockPos.immutable();
        this.direction = direction;
    }

    @Override
    public BlockPos getPosition() {
        return blockPos;
    }

    @Override
    public boolean canInsert(ItemStack sourceStack) {
        if (sourceStack.isEmpty()) {
            return false;
        }
        for (int slot : getSlots()) {
            if (!container.canPlaceItem(slot, sourceStack)) {
                continue;
            }
            if (container instanceof WorldlyContainer worldlyContainer) {
                if (!worldlyContainer.canPlaceItemThroughFace(slot, sourceStack, direction)) {
                    continue;
                }
            }
            ItemStack existing = container.getItem(slot);
            if (existing.isEmpty()) {
                return true;
            }
            if (ItemStack.isSameItemSameComponents(existing, sourceStack)) {
                if (existing.getCount() < existing.getMaxStackSize()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int insert(ItemStack sourceStack, int amount) {
        if (sourceStack.isEmpty() || amount <= 0) {
            return 0;
        }
        //int remaining = amount;
        int remaining = Math.min(amount, sourceStack.getCount());
        int requested = remaining;
        for (int slot : getSlots()) {
            if (remaining <= 0) {
                break;
            }
            if (!container.canPlaceItem(slot, sourceStack)) {
                continue;
            }
            if (container instanceof WorldlyContainer worldlyContainer) {
                if (!worldlyContainer.canPlaceItemThroughFace(slot, sourceStack, direction)) {
                    continue;
                }
            }
            ItemStack existing = container.getItem(slot);
            if (existing.isEmpty()) {
                int insertAmount = Math.min(remaining, sourceStack.getMaxStackSize());
                ItemStack inserted = sourceStack.copyWithCount(insertAmount);
                container.setItem(slot, inserted);
                remaining -= insertAmount;
                continue;
            }
            if (!ItemStack.isSameItemSameComponents(existing, sourceStack)) {
                continue;
            }
            int capacity = existing.getMaxStackSize() - existing.getCount();
            if (capacity <= 0) {
                continue;
            }
            int insertAmount = Math.min(remaining, capacity);
            existing.grow(insertAmount);
            remaining -= insertAmount;
        }
        if (remaining != requested) {
            container.setChanged();
        }
        return requested - remaining;
    }

    @Override
    public void onItemInserted(int amount) {
        if (container instanceof SculkTransporterBlockEntity sculkTransporterBlockEntity) {
            sculkTransporterBlockEntity.setTransferCooldown(SculkTransporterBlockEntity.MOVE_ITEM_SPEED);
        }
    }

    @Override
    public boolean canExtract() {
        for (int slot : getSlots()) {
            ItemStack stack = container.getItem(slot);
            if (!stack.isEmpty() && canTakeFromSlot(slot, stack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack extract(int amount) {
        if (amount <= 0) {
            return ItemStack.EMPTY;
        }
        for (int slot : getSlots()) {
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty() || !canTakeFromSlot(slot, stack)) {
                continue;
            }
            ItemStack extracted = container.removeItem(slot, Math.min(amount, stack.getCount()));
            if (!extracted.isEmpty()) {
                container.setChanged();
                return extracted;
            }
        }
        return ItemStack.EMPTY;
    }

    private int[] getSlots() {
        if (container instanceof WorldlyContainer worldlyContainer) {
            return worldlyContainer.getSlotsForFace(direction);
        }
        int size = container.getContainerSize();
        int[] slots = new int[size];
        for (int i = 0; i < size; i++) {
            slots[i] = i;
        }
        return slots;
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

    private boolean canTakeFromSlot(int slot, ItemStack itemStack) {
        if (container instanceof WorldlyContainer worldlyContainer) {
            return worldlyContainer.canTakeItemThroughFace(slot, itemStack, direction);
        }
        return true;
    }
}
