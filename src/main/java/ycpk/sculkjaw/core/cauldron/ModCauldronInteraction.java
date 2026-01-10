package ycpk.sculkjaw.core.cauldron;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import ycpk.sculkjaw.blocks.modblocks.SculkAcidCauldronBlock;
import ycpk.sculkjaw.registry.ModBlocks;
import ycpk.sculkjaw.registry.ModItems;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public interface ModCauldronInteraction extends CauldronInteraction {
    Map<String, ModCauldronInteraction.InteractionMap> INTERACTIONS = new Object2ObjectArrayMap();
    Codec<InteractionMap> CODEC = Codec.stringResolver(InteractionMap::name, INTERACTIONS::get);
    InteractionMap EMPTY = newInteractionMap("empty");
    InteractionMap SCULK_ACID = newInteractionMap("sculk_acid");

    static InteractionMap newInteractionMap(String string) {
        Object2ObjectOpenHashMap<Item, ModCauldronInteraction> object2ObjectOpenHashMap = new Object2ObjectOpenHashMap();
        object2ObjectOpenHashMap.defaultReturnValue((blockState, level, blockPos, player, interactionHand, itemStack) -> {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        });
        InteractionMap interactionMap = new InteractionMap(string, object2ObjectOpenHashMap);
        INTERACTIONS.put(string, interactionMap);
        return interactionMap;
    }

    InteractionResult interact(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack);

    static void bootStrap() {
        Map<Item, ModCauldronInteraction> map = EMPTY.map();
        addDefaultInteractions(map);
        map.put(ModItems.SCULK_ACID_BOTTLE, (blockstate, level, blockPos, player, interactionHand, itemStack) -> {
            PotionContents potionContents = (PotionContents) itemStack.get(DataComponents.POTION_CONTENTS);
            if(potionContents != null && potionContents.is(Potions.WATER)) {
                if(!level.isClientSide()) {
                    Item item = itemStack.getItem();
                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(item));
                    level.setBlockAndUpdate(blockPos, ModBlocks.SCULK_ACID_CAULDRON.defaultBlockState());
                    level.playSound((Entity) null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent((Entity) null, GameEvent.FLUID_PLACE, blockPos);
                }
                return InteractionResult.SUCCESS;
            }
            else {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
        });
        Map<Item, ModCauldronInteraction> map2 = SCULK_ACID.map();
        addDefaultInteractions(map2);
        map2.put(Items.BUCKET, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            return fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, new ItemStack(ModItems.SCULK_ACID_BUCKET), (blockStatex) -> {
                return (Integer) blockStatex.getValue(SculkAcidCauldronBlock.LEVEL) == 3;
            }, SoundEvents.BUCKET_FILL);
        });
        map2.put(Items.GLASS_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, PotionContents.createItemStack(ModItems.SCULK_ACID_BOTTLE, Potions.WATER)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                SculkAcidCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound((Entity)null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, blockPos);
            }
            return InteractionResult.SUCCESS;
        });
        map2.put(ModItems.SCULK_ACID_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if ((Integer) blockState.getValue(SculkAcidCauldronBlock.LEVEL) == 3) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
            else {
                PotionContents potionContents = (PotionContents)itemStack.get(DataComponents.POTION_CONTENTS);
                if (potionContents != null && potionContents.is(Potions.WATER)) {
                    if (!level.isClientSide()) {
                        player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                        player.awardStat(Stats.USE_CAULDRON);
                        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                        level.setBlockAndUpdate(blockPos, (BlockState)blockState.cycle(SculkAcidCauldronBlock.LEVEL));
                        level.playSound((Entity)null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent((Entity)null, GameEvent.FLUID_PLACE, blockPos);
                    }

                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                }
            }
        });
    }

    static void addDefaultInteractions(Map<Item, ModCauldronInteraction> map) {
        map.put(ModItems.SCULK_ACID_BUCKET, ModCauldronInteraction::fillSculkAcidInteraction);
    }

    static InteractionResult fillBucket(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack, ItemStack itemStack2, Predicate<BlockState> predicate, SoundEvent soundEvent) {
        if (!predicate.test(blockState)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        } else {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, itemStack2));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());
                level.playSound((Entity)null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, blockPos);
            }

            return InteractionResult.SUCCESS;
        }
    }

    static InteractionResult emptyBucket(Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack, BlockState blockState, SoundEvent soundEvent) {
        if(!level.isClientSide()) {
            Item item = itemStack.getItem();
            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.BUCKET)));
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            level.setBlockAndUpdate(blockPos, blockState);
            level.playSound((Entity) null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent((Entity) null, GameEvent.FLUID_PLACE, blockPos);
        }
        return InteractionResult.SUCCESS;
    }

    private static InteractionResult fillSculkAcidInteraction(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        return (InteractionResult) (isUnderWater(level, blockPos) ? InteractionResult.CONSUME : emptyBucket(level, blockPos, player, interactionHand, itemStack, ModBlocks.SCULK_ACID_CAULDRON.defaultBlockState(), SoundEvents.BUCKET_EMPTY));
    }

    private static boolean isUnderWater(Level level, BlockPos blockPos) {
        FluidState fluidState = level.getFluidState(blockPos.above());
        return fluidState.is(FluidTags.WATER);
    }

    public static record InteractionMap(String identifier, Map<Item, ModCauldronInteraction> map) {
        public InteractionMap(String identifier, Map<Item, ModCauldronInteraction> map) {
            this.identifier = identifier;
            this.map = map;
        }

        public String name() {
            return this.identifier;
        }

        public Map<Item, ModCauldronInteraction> map() {
            return this.map;
        }
    }
}
