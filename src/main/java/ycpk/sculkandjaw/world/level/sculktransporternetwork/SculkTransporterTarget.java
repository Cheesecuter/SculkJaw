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
     * Current target has an extractable item matching the filter.
     * An empty filter accepts every items.
     */
    default boolean canExtract(ItemStack filterItem) {
        return (filterItem == null || filterItem.isEmpty()) && canExtract();
    }

    /**
     * Try to extract items from target.
     *
     * @param amount Max count to extract
     */
    default ItemStack extract(int amount) {
        return ItemStack.EMPTY;
    }

    /**
     * Try to extract an item matching the filter.
     */
    default ItemStack extract(int amount, ItemStack filterItem) {
        return filterItem == null || filterItem.isEmpty() ? extract(amount) : ItemStack.EMPTY;
    }

    /**
     * Try to extract an item amount described by the transfer mode.
     * The implementation must inspect the actual source stack and use
     * {@link SculkTransferAmount#getAmount(ItemStack)} so custom stack sizes
     * are handled correctly.
     */
    ItemStack extract(SculkTransferAmount transferAmount, ItemStack filterItem);
}
