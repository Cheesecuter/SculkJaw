package ycpk.sculkandjaw.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TunedSculkJawMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SIZE = 9;
    private final Container tunedSculkJaw;

    public TunedSculkJawMenu(int i, Inventory inventory) {
        this(i, inventory, new SimpleContainer(9));
    }

    public TunedSculkJawMenu(int i, Inventory inventory, Container container) {
        super(ModMenuType.TUNED_SCULK_JAW, i);
        this.tunedSculkJaw = container;
        checkContainerSize(container, 9);
        container.startOpen(inventory.player);
        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(container, j, 8 + j * 18, 20));
        }
        this.addStandardInventorySlots(inventory, 8, 51);
    }

    public boolean stillValid(Player player) {
        return this.tunedSculkJaw.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (i < this.tunedSculkJaw.getContainerSize()) {
                if (!this.moveItemStackTo(itemStack2, this.tunedSculkJaw.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, this.tunedSculkJaw.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.tunedSculkJaw.stopOpen(player);
    }
}
