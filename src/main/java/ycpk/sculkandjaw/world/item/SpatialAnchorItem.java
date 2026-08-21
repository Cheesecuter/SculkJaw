package ycpk.sculkandjaw.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import ycpk.sculkandjaw.blocks.blockentities.SculkTeleporterBlockEntity;
import ycpk.sculkandjaw.registry.ModBlocks;

public class SpatialAnchorItem extends Item {
    private static final String PENDING = "Pending";
    private static final String LAST = "Last";

    public SpatialAnchorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel() instanceof ServerLevel serverLevel)) {
            return context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.SCULK_TELEPORTER)
                    ? InteractionResult.SUCCESS
                    : InteractionResult.PASS;
        }
        if (!serverLevel.getBlockState(context.getClickedPos()).is(ModBlocks.SCULK_TELEPORTER)
                || !(serverLevel.getBlockEntity(context.getClickedPos()) instanceof SculkTeleporterBlockEntity teleporter)) {
            return InteractionResult.PASS;
        }

        ItemStack stack = context.getItemInHand();
        AnchorPoint current = new AnchorPoint(serverLevel.dimension(), context.getClickedPos().immutable());
        var data = copyData(stack);
        AnchorPoint last = readPoint(data, LAST);
        AnchorPoint pending = readPoint(data, PENDING);
        if (current.equals(last) || current.equals(pending)) {
            return InteractionResult.SUCCESS;
        }
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return InteractionResult.FAIL;
        }

        stack.setDamageValue(stack.getDamageValue() + 1);
        writePoint(data, LAST, current);
        if (pending == null) {
            writePoint(data, PENDING, current);
        } else {
            ServerLevel firstLevel = serverLevel.getServer().getLevel(pending.dimension());
            if (firstLevel != null
                    && firstLevel.getBlockState(pending.pos()).is(ModBlocks.SCULK_TELEPORTER)
                    && firstLevel.getBlockEntity(pending.pos()) instanceof SculkTeleporterBlockEntity firstTeleporter) {
                firstTeleporter.setDestination(current.dimension(), current.pos());
                teleporter.setDestination(pending.dimension(), pending.pos());
                removePoint(data, PENDING);
            } else {
                writePoint(data, PENDING, current);
            }
        }
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
        return InteractionResult.SUCCESS;
    }

    private static net.minecraft.nbt.CompoundTag copyData(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData == null ? new net.minecraft.nbt.CompoundTag() : customData.copyTag();
    }

    private static void writePoint(net.minecraft.nbt.CompoundTag data, String prefix, AnchorPoint point) {
        data.putInt(prefix + "X", point.pos().getX());
        data.putInt(prefix + "Y", point.pos().getY());
        data.putInt(prefix + "Z", point.pos().getZ());
        data.putString(prefix + "Dimension", point.dimension().identifier().toString());
    }

    private static void removePoint(net.minecraft.nbt.CompoundTag data, String prefix) {
        data.remove(prefix + "X");
        data.remove(prefix + "Y");
        data.remove(prefix + "Z");
        data.remove(prefix + "Dimension");
    }

    @Nullable
    private static AnchorPoint readPoint(net.minecraft.nbt.CompoundTag data, String prefix) {
        String dimensionId = data.getStringOr(prefix + "Dimension", "");
        if (dimensionId.isEmpty() || !data.contains(prefix + "X") || !data.contains(prefix + "Y") || !data.contains(prefix + "Z")) {
            return null;
        }
        try {
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, Identifier.parse(dimensionId));
            return new AnchorPoint(dimension, new BlockPos(
                    data.getIntOr(prefix + "X", 0),
                    data.getIntOr(prefix + "Y", 0),
                    data.getIntOr(prefix + "Z", 0)
            ));
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private record AnchorPoint(ResourceKey<Level> dimension, BlockPos pos) {
    }
}
