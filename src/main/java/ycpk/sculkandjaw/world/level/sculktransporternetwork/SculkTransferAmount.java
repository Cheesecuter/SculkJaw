package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.world.item.ItemStack;
import ycpk.sculkandjaw.world.level.block.state.properties.TransferAmount;

public enum SculkTransferAmount {
    ONE,
    HALF_STACK,
    FULL_STACK;

    public static SculkTransferAmount from(TransferAmount transferAmount) {
        if (transferAmount == null) {
            return FULL_STACK;
        }
        return switch (transferAmount) {
            case ONE -> ONE;
            case HALF_STACK -> HALF_STACK;
            case FULL_STACK -> FULL_STACK;
        };
    }

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
