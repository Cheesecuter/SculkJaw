package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.world.item.ItemStack;

public enum SculkTransferAmount {
    ONE,
    HALF_STACK,
    FULL_STACK;

    public int getAmount(ItemStack sourceStack) {
        if (sourceStack.isEmpty()) {
            return 0;
        }
        int maxStackSize = sourceStack.getMaxStackSize();
        return switch (this) {
            case ONE -> Math.min(sourceStack.getCount(), 1);
            case HALF_STACK -> Math.min(sourceStack.getCount(), Math.max(1, maxStackSize / 2));
            case FULL_STACK -> Math.min(sourceStack.getCount(), maxStackSize);
        };
    }
}
