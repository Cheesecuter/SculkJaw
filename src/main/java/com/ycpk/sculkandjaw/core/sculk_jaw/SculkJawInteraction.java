package com.ycpk.sculkandjaw.core.sculk_jaw;

import com.mojang.serialization.Codec;
import com.ycpk.sculkandjaw.blocks.modblocks.SculkJawBlock;
import com.ycpk.sculkandjaw.registry.ModBlocks;
import com.ycpk.sculkandjaw.registry.ModItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;

import java.util.Map;
import java.util.function.Predicate;

public interface SculkJawInteraction {
    Map<String, InteractionMap> INTERACTIONS = new Object2ObjectArrayMap();
    Codec<InteractionMap> CODEC = Codec.stringResolver(InteractionMap::identifier, INTERACTIONS::get);
    InteractionMap EMPTY = newInteractionMap("empty");
    InteractionMap SCULK_ACID = newInteractionMap("sculk_acid");

    static InteractionMap newInteractionMap(String string) {
        Object2ObjectOpenHashMap<Item, SculkJawInteraction> object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
        object2ObjectOpenHashMap.defaultReturnValue((blockState, level, blockPos, player, interactionHand, itemStack) -> {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        });
        InteractionMap interactionMap = new InteractionMap(string, object2ObjectOpenHashMap);
        INTERACTIONS.put(string, interactionMap);
        return interactionMap;
    }

    ItemInteractionResult interact(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack);

    static void bootStrap() {
        Map<Item, SculkJawInteraction> mapEmpty = EMPTY.map();
        addDefaultInteractions(mapEmpty);
        Map<Item, SculkJawInteraction> mapSculkAcid = SCULK_ACID.map();
        addDefaultInteractions(mapSculkAcid);
        mapSculkAcid.put(Items.BUCKET, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            return fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, new ItemStack(ModItems.SCULK_ACID_BUCKET), (blockStatex) -> {
                return (Boolean) blockStatex.getValue(SculkJawBlock.ACID_FILLED);
            }, SoundEvents.BUCKET_FILL);
        });
    }

    static void addDefaultInteractions(Map<Item, SculkJawInteraction> map) {
        map.put(ModItems.SCULK_ACID_BUCKET.get(), SculkJawInteraction::fillSculkAcidInteraction);
    }

    static ItemInteractionResult fillBucket(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack, ItemStack itemStack2, Predicate<BlockState> predicate, SoundEvent soundEvent) {
        if (!predicate.test(blockState)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        else {
            if (!level.isClientSide) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, itemStack2));
                player.awardStat(Stats.ITEM_USED.get(item));
                Direction direction = level.getBlockState(blockPos).getValue(SculkJawBlock.FACING);
                boolean isCombined = level.getBlockState(blockPos).getValue(SculkJawBlock.COMBINED);
                level.setBlockAndUpdate(blockPos, ModBlocks.SCULK_JAW.get().defaultBlockState().setValue(SculkJawBlock.FACING, direction).setValue(SculkJawBlock.COMBINED, isCombined));
                level.playSound((Player) null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent((Entity) null, GameEvent.FLUID_PICKUP, blockPos);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    static ItemInteractionResult emptyBucket(Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack, BlockState blockState, SoundEvent soundEvent) {
        if(!level.isClientSide()) {
            Item item = itemStack.getItem();
            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.BUCKET)));
            player.awardStat(Stats.ITEM_USED.get(item));
            level.setBlockAndUpdate(blockPos, blockState);
            level.playSound((Player) null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent((Entity) null, GameEvent.FLUID_PLACE, blockPos);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private static ItemInteractionResult fillSculkAcidInteraction(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        BlockState blockState2 = level.getBlockState(blockPos);
        return (ItemInteractionResult) (isUnderWater(level, blockPos) ? ItemInteractionResult.CONSUME : emptyBucket(level, blockPos, player, interactionHand, itemStack, blockState2.setValue(SculkJawBlock.ACID_FILLED, true), SoundEvents.BUCKET_EMPTY));
    }

    private static boolean isUnderWater(Level level, BlockPos blockPos) {
        FluidState fluidState = level.getFluidState(blockPos.above());
        return fluidState.is(FluidTags.WATER);
    }

    public static record InteractionMap(String identifier, Map<Item, SculkJawInteraction> map) {
        public InteractionMap(String identifier, Map<Item, SculkJawInteraction> map) {
            this.identifier = identifier;
            this.map = map;
        }

        public String identifier() {return this.identifier;}

        public Map<Item, SculkJawInteraction> map() {return this.map;}
    }
}
