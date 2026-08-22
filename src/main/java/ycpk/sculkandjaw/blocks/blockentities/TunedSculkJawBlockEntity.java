package ycpk.sculkandjaw.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.modblocks.TunedSculkJawBlock;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.world.level.block.state.properties.TunedSculkJawIOState;
import ycpk.sculkandjaw.world.level.sculktransporternetwork.*;

import java.util.Iterator;
import java.util.Optional;

public class TunedSculkJawBlockEntity extends BlockEntity implements RandomizableContainer, MenuProvider, ItemOwner {
    public static final int CONTAINER_SIZE = 9;
    public static final int MOVE_ITEM_SPEED = 1;
    private ItemStack filterItem;
    private NonNullList<ItemStack> items;
    private int cooldownTime;
    private long tickedGameTime;
    private TunedSculkJawIOState lastIOState;
    private Direction lastFacing;

    public TunedSculkJawBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TUNED_SCULK_JAW_BLOCK_ENTITY, blockPos, blockState);
        this.filterItem = ItemStack.EMPTY;
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        this.cooldownTime = -1;
        this.lastIOState = blockState.getValue(TunedSculkJawBlock.IO_STATE);
        this.lastFacing = blockState.getValue(TunedSculkJawBlock.FACING);
    }

    public void setFilterItem(ItemStack itemStack) {
        this.filterItem = itemStack;
        this.setChanged();
    }

    public ItemStack getFilterItem() {
        return this.filterItem;
    }

    public boolean acceptsItem(ItemStack itemStack) {
        return !itemStack.isEmpty()
                && (this.filterItem.isEmpty() || ItemStack.isSameItemSameComponents(this.filterItem, itemStack));
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        if (!this.tryLoadLootTable(valueInput)) {
            this.filterItem = (ItemStack) valueInput.read("FilterItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
            ContainerHelper.loadAllItems(valueInput, this.items);
        }
        this.cooldownTime = valueInput.getIntOr("TransferCooldown", -1);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        if (!this.trySaveLootTable(valueOutput)) {
            if (!this.filterItem.isEmpty()) {
                valueOutput.store("FilterItem", ItemStack.CODEC, this.filterItem);
            }
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
    public ItemStack getItem(int i) {
        this.unpackLootTable(null);
        return (ItemStack) this.items.get(i);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return itemStack.isEmpty() || acceptsItem(itemStack);
    }

    public void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        for (int slot = 0; slot < Math.min(CONTAINER_SIZE, nonNullList.size()); slot++) {
            this.items.set(slot, nonNullList.get(slot));
        }
        this.setChanged();
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    private static boolean canMergeItems(ItemStack target, ItemStack source) {
        return !target.isEmpty()
                && ItemStack.isSameItemSameComponents(target, source)
                && target.getCount() < target.getMaxStackSize();
    }

    public boolean addItem(ItemStack source) {
        this.unpackLootTable(null);
        if (source.isEmpty()) {
            return true;
        }
        if (!acceptsItem(source)) {
            return false;
        }
        for (int slot = 0; slot < this.items.size(); slot++) {
            ItemStack target = this.items.get(slot);
            if (canMergeItems(target, source)) {
                int amount = Math.min(source.getCount(), target.getMaxStackSize() - target.getCount());
                target.grow(amount);
                source.shrink(amount);
                this.setChanged();
                if (source.isEmpty()) {
                    return true;
                }
            }
        }
        for (int slot = 0; slot < this.items.size(); slot++) {
            if (this.items.get(slot).isEmpty()) {
                int amount = Math.min(source.getCount(), source.getMaxStackSize());
                this.items.set(slot, source.copyWithCount(amount));
                source.shrink(amount);
                this.setChanged();
                return source.isEmpty();
            }
        }
        return false;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        this.unpackLootTable(null);
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        if (!result.isEmpty()) {
            this.setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        this.unpackLootTable(null);
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.unpackLootTable(null);
        if (!itemStack.isEmpty() && !acceptsItem(itemStack)) {
            return;
        }
        this.items.set(slot, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.unpackLootTable(null);
        for (int slot = 0; slot < this.items.size(); slot++) {
            this.items.set(slot, ItemStack.EMPTY);
        }
        this.setChanged();
    }

    @Override
    public Level level() {
        return this.level;
    }

    @Override
    public Vec3 position() {
        return this.getBlockPos().getCenter();
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return ((Direction) this.getBlockState().getValue(TunedSculkJawBlock.FACING)).getOpposite().toYRot();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level == null) {
            return;
        }
        BlockState blockState = getBlockState();
        level.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_ALL);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TunedSculkJawBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            blockEntity.tick();
        }
    }

    private void tick() {
        if (!(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        long gameTime = serverLevel.getGameTime();
        if (this.tickedGameTime == gameTime) {
            return;
        }
        this.tickedGameTime = gameTime;

        TunedSculkJawIOState ioState = this.getBlockState().getValue(TunedSculkJawBlock.IO_STATE);
        Direction facing = this.getBlockState().getValue(TunedSculkJawBlock.FACING);
        if (ioState != this.lastIOState || facing != this.lastFacing) {
            this.lastIOState = ioState;
            this.lastFacing = facing;
            SculkTransporterNetworkManagerProvider.get(serverLevel).markDirty();
        }
        if (this.cooldownTime > 0) {
            this.cooldownTime--;
            return;
        }

        boolean moved = ioState == TunedSculkJawIOState.INPUT
                ? pushToNetwork(serverLevel) || pullFromExternalContainer(serverLevel)
                : pushToExternalContainer(serverLevel);
        if (moved) {
            this.cooldownTime = MOVE_ITEM_SPEED;
        }
    }

    public void entityInside(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (entity instanceof ItemEntity itemEntity) {
            if (!itemEntity.getItem().isEmpty()) {
                addItem(itemEntity.getItem());
            }
        }
    }

    private boolean pullFromExternalContainer(ServerLevel serverLevel) {
        Optional<SculkTransporterTarget> optionalTarget = getExternalTarget(serverLevel);
        if (optionalTarget.isEmpty() || !optionalTarget.get().canExtract(this.filterItem)) {
            return false;
        }
        ItemStack extracted = optionalTarget.get().extract(1, this.filterItem);
        if (extracted.isEmpty()) {
            return false;
        }
        if (this.addItem(extracted)) {
            return true;
        }
        optionalTarget.get().insert(extracted, extracted.getCount());
        return false;
    }

    private boolean pushToNetwork(ServerLevel serverLevel) {
        Optional<SculkTransporterNetwork> optionalNetwork = SculkTransporterNetworkManagerProvider.get(serverLevel)
                .getNetwork(this.worldPosition);
        if (optionalNetwork.isEmpty()) {
            return false;
        }
        SculkTransporterNetwork network = optionalNetwork.get();
        for (int slot = 0; slot < this.getContainerSize(); slot++) {
            ItemStack source = this.getItem(slot);
            if (!acceptsItem(source)) {
                continue;
            }
            for (BlockPos nextHop : network.getNextHops(serverLevel, this.worldPosition, source)) {
                Direction direction = getDirectionTo(this.worldPosition, nextHop);
                Optional<SculkTransporterTarget> optionalTarget = SculkTransporterTargets.findTarget(
                        serverLevel, nextHop, direction.getOpposite());
                if (optionalTarget.isEmpty()) {
                    continue;
                }
                int amount = SculkTransferAmount.FULL_STACK.getAmount(source);
                int inserted = optionalTarget.get().insert(source, amount);
                if (inserted <= 0) {
                    continue;
                }
                source.shrink(inserted);
                this.setChanged();
                optionalTarget.get().onItemInserted(inserted);
                return true;
            }
        }
        return false;
    }

    private boolean pushToExternalContainer(ServerLevel serverLevel) {
        Optional<SculkTransporterTarget> optionalTarget = getExternalTarget(serverLevel);
        if (optionalTarget.isEmpty()) {
            return false;
        }
        for (int slot = 0; slot < this.getContainerSize(); slot++) {
            ItemStack source = this.getItem(slot);
            if (!acceptsItem(source)) {
                continue;
            }
            int amount = Math.min(source.getCount(), source.getMaxStackSize());
            int inserted = optionalTarget.get().insert(source, amount);
            if (inserted > 0) {
                source.shrink(inserted);
                this.setChanged();
                return true;
            }
        }
        return false;
    }

    private Optional<SculkTransporterTarget> getExternalTarget(ServerLevel serverLevel) {
        Direction facing = this.getBlockState().getValue(TunedSculkJawBlock.FACING);
        BlockPos containerPos = this.worldPosition.relative(facing);
        BlockEntity adjacent = serverLevel.getBlockEntity(containerPos);
        if (adjacent instanceof SculkTransporterBlockEntity || adjacent instanceof TunedSculkJawBlockEntity) {
            return Optional.empty();
        }
        return SculkTransporterTargets.findTarget(serverLevel, containerPos, facing.getOpposite());
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
        this.cooldownTime = Math.max(0, cooldownTime);
    }

    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return ChestMenu.oneRow(i, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ycpk.tuned_sculk_jaw");
    }
}
