package ycpk.sculkandjaw.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.world.level.sculktransporternetwork.*;

import java.util.Iterator;
import java.util.Optional;

public class SculkTransporterBlockEntity extends BlockEntity implements RandomizableContainer, MenuProvider {
    public static final int MOVE_ITEM_SPEED = 1;
    public static final int CONTAINER_SIZE = 5;
    private NonNullList<ItemStack> items;
    private int cooldownTime;
    private long tickedGameTime;
    private int transferDirectionIndex;
    private SculkTransferAmount transferAmount = SculkTransferAmount.FULL_STACK;

    public SculkTransporterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SCULK_TRANSPORTER_BLOCK_ENTITY, blockPos, blockState);
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        this.cooldownTime = -1;
        this.transferDirectionIndex = 0;
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(valueInput)) {
            ContainerHelper.loadAllItems(valueInput, this.items);
        }
        this.cooldownTime = valueInput.getIntOr("TransferCooldown", -1);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        if (!this.trySaveLootTable(valueOutput)) {
            ContainerHelper.saveAllItems(valueOutput, this.items);
        }
        valueOutput.putInt("TransferCooldown", this.cooldownTime);
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
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        this.unpackLootTable((Player) null);
        Iterator<ItemStack> itemsIterator = this.items.iterator();
        ItemStack itemStack;
        do {
            if (!itemsIterator.hasNext()) {
                return true;
            }
            itemStack = (ItemStack) itemsIterator.next();
        } while (itemStack.isEmpty());
        return false;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.unpackLootTable((Player) null);
        this.items.set(i, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
        this.setChanged();
    }

    @Override
    public ItemStack getItem(int i) {
        this.unpackLootTable((Player) null);
        return (ItemStack) this.items.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        this.unpackLootTable((Player) null);
        ItemStack result = ContainerHelper.removeItem(this.items, i, j);
        if (!result.isEmpty()) {
            this.setChanged();
        }
        return result;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        this.unpackLootTable((Player) null);
        ItemStack result = ContainerHelper.takeItem(this.items, i);
        if (!result.isEmpty()) {
            this.setChanged();
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.unpackLootTable((Player) null);
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        this.setChanged();
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, SculkTransporterBlockEntity sculkTransporterBlockEntity) {
        if (level.isClientSide()) {
            return;
        }
        sculkTransporterBlockEntity.tick();
    }

    private void tick() {
        if (this.level == null) {
            return;
        }
        long gameTime = this.level.getGameTime();
        if (this.tickedGameTime == gameTime) {
            return;
        }
        this.tickedGameTime = gameTime;
        if (this.cooldownTime > 0) {
            this.cooldownTime--;
            return;
        }
        if (this.transferItem()) {
            this.cooldownTime = MOVE_ITEM_SPEED;
        }
    }

    private boolean transferItem() {
        if (this.level == null || this.level.isClientSide()) {
            return false;
        }
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return false;
        }
        SculkTransporterNetworkManager manager = SculkTransporterNetworkManagerProvider.get(serverLevel);
        Optional<SculkTransporterNetwork> optionalNetwork = manager.getNetwork(this.worldPosition);
        if (optionalNetwork.isEmpty()) {
            return false;
        }
        SculkTransporterNetwork network = optionalNetwork.get();
        for (int slot = 0; slot < this.getContainerSize(); slot++) {
            ItemStack itemStack = this.getItem(slot);
            if (itemStack.isEmpty()) {
                continue;
            }
            for (BlockPos nextHop : network.getNextHops(serverLevel, this.worldPosition, itemStack)) {
                Direction direction = getDirectionTo(this.worldPosition, nextHop);
                Optional<SculkTransporterTarget> target = SculkTransporterTargets.findTarget(
                        serverLevel, nextHop, direction.getOpposite());
                if (target.isEmpty()) {
                    continue;
                }
                int amount = this.transferAmount.getAmount(itemStack);
                int inserted = target.get().insert(itemStack, amount);
                if (inserted <= 0) {
                    continue;
                }
                itemStack.shrink(inserted);
                this.setChanged();
                target.get().onItemInserted(inserted);
                return true;
            }
        }
        return false;
    }

    private static Direction getDirectionTo(BlockPos from, BlockPos to) {
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        int dz = to.getZ() - from.getZ();
        if (dx > 0) {
            return Direction.EAST;
        }
        if (dx < 0) {
            return Direction.WEST;
        }
        if (dy > 0) {
            return Direction.UP;
        }
        if (dy < 0) {
            return Direction.DOWN;
        }
        if (dz > 0) {
            return Direction.SOUTH;
        }
        return Direction.NORTH;
    }

    public void setTransferCooldown(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        //return ChestMenu.oneRow(i, inventory);
        return new HopperMenu(i, inventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ycpk.sculk_transporter");
    }
}
