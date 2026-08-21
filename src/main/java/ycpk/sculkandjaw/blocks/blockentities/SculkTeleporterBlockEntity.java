package ycpk.sculkandjaw.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.registry.ModBlockEntities;

public class SculkTeleporterBlockEntity extends BlockEntity {
    @Nullable
    private ResourceKey<Level> destinationDimension;
    @Nullable
    private BlockPos destinationPos;

    public SculkTeleporterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.SCULK_TELEPORTER_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.destinationPos = valueInput.read("destinationPos", BlockPos.CODEC)
                .filter(Level::isInSpawnableBounds)
                .orElse(null);
        this.destinationDimension = valueInput.read("destinationDimension", Level.RESOURCE_KEY_CODEC)
                .orElse(this.destinationPos == null ? null : Level.OVERWORLD);
    }

    @Override
    public void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.storeNullable("destinationPos", BlockPos.CODEC, this.destinationPos);
        valueOutput.storeNullable("destinationDimension", Level.RESOURCE_KEY_CODEC, this.destinationDimension);
    }

    public void setDestinationPos(BlockPos blockPos) {
        this.setDestination(Level.OVERWORLD, blockPos);
    }

    @Nullable
    public BlockPos getDestinationPos() {
        return this.destinationPos;
    }

    public void setDestination(ResourceKey<Level> dimension, BlockPos blockPos) {
        this.destinationDimension = dimension;
        this.destinationPos = blockPos.immutable();
        this.setChanged();
    }

    @Nullable
    public ResourceKey<Level> getDestinationDimension() {
        return this.destinationDimension;
    }

    public void clearDestination() {
        this.destinationDimension = null;
        this.destinationPos = null;
        this.setChanged();
    }
}
