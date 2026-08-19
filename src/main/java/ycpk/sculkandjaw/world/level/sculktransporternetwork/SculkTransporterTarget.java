package ycpk.sculkandjaw.world.level.sculktransporternetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public interface SculkTransporterTarget {

    /**
     * Get target position.
     *
     * @return Target position
     */
    BlockPos getPosition();

    /**
     * Current target is allowed to accept items.
     */
    boolean canInsert(ItemStack sourceStack);

    /**
     * Try to insert items into target.
     *
     * @param sourceStack Items to insert with.
     * @param amount Max count to insert.
     * @return Real amount inserted.
     */
    int insert(ItemStack sourceStack, int amount);

    /**
     * Called when transporter successfully insert items into this.
     */

    default void onItemInserted(int amount) {
    }

    /**
     * Current target is allowed to extract items.
     */
    default boolean canExtract() {
        return false;
    }

    /**
     * Try to extract items from target.
     *
     * @param amount Max count to extract
     */
    default ItemStack extract(int amount) {
        return ItemStack.EMPTY;
    }
}
