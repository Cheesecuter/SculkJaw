package ycpk.sculkandjaw.core.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import ycpk.sculkandjaw.blocks.modblocks.SculkAcidCauldronBlock;
import ycpk.sculkandjaw.registry.ModBlocks;
import ycpk.sculkandjaw.registry.ModItems;
import ycpk.sculkandjaw.registry.ModMobEffects;
import ycpk.sculkandjaw.world.item.alchemy.ModPotions;

import java.util.Map;
import java.util.function.Predicate;

public interface ModCauldronInteraction {
    Map<Item, CauldronInteraction> SCULK_ACID = CauldronInteraction.newInteractionMap();
    CauldronInteraction FILL_SCULK_ACID = (blockState, level, blockPos, player, interactionHand, itemStack) -> {
        return emptyBucket(level, blockPos, player, interactionHand, itemStack, (BlockState) ModBlocks.SCULK_ACID_CAULDRON.defaultBlockState().setValue(SculkAcidCauldronBlock.LEVEL, 3), SoundEvents.BOTTLE_EMPTY);
    };

    static void bootStrap() {
        Map<Item, CauldronInteraction> mapEmpty = CauldronInteraction.EMPTY;
        CauldronInteraction.addDefaultInteractions(mapEmpty);
        addDefaultInteractions(mapEmpty);
        mapEmpty.put(Items.POTION, (blockstate, level, blockPos, player, interactionHand, itemStack) -> {
            Potion potion = PotionUtils.getPotion(itemStack);
            if(potion.getEffects().get(0).getEffect().equals(ModMobEffects.ACID_ETCHING)) {
                if(!level.isClientSide()) {
                    Item item = itemStack.getItem();
                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(item));
                    level.setBlockAndUpdate(blockPos, ModBlocks.SCULK_ACID_CAULDRON.defaultBlockState());
                    level.playSound((Entity) null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent((Entity) null, GameEvent.FLUID_PLACE, blockPos);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else {
                return InteractionResult.PASS;
            }
        });
        Map<Item, CauldronInteraction> mapSculkAcid = SCULK_ACID;
        CauldronInteraction.addDefaultInteractions(mapSculkAcid);
        addDefaultInteractions(mapSculkAcid);
        mapSculkAcid.put(Items.BUCKET, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            return fillBucket(blockState, level, blockPos, player, interactionHand, itemStack, new ItemStack(ModItems.SCULK_ACID_BUCKET), (blockStatex) -> {
                return (Integer) blockStatex.getValue(SculkAcidCauldronBlock.LEVEL) == 3;
            }, SoundEvents.BUCKET_FILL);
        });
        mapSculkAcid.put(Items.GLASS_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                PotionItem potionItem = new PotionItem((new Item.Properties()).stacksTo(1));
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, PotionUtils.setPotion(potionItem.getDefaultInstance(), ModPotions.ACID_ETCHING)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                SculkAcidCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound((Entity)null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent((Entity)null, GameEvent.FLUID_PICKUP, blockPos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        });
        mapSculkAcid.put(Items.POTION, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if ((Integer) blockState.getValue(SculkAcidCauldronBlock.LEVEL) == 3) {
                return InteractionResult.PASS;
            }
            else {
                Potion potion = PotionUtils.getPotion(itemStack);
                if (potion.getEffects().get(0).getEffect().equals(ModMobEffects.ACID_ETCHING)) {
                    if (!level.isClientSide()) {
                        player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                        player.awardStat(Stats.USE_CAULDRON);
                        player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                        level.setBlockAndUpdate(blockPos, (BlockState)blockState.cycle(SculkAcidCauldronBlock.LEVEL));
                        level.playSound((Entity)null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent((Entity)null, GameEvent.FLUID_PLACE, blockPos);
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                } else {
                    return InteractionResult.PASS;
                }
            }
        });
    }

    static void addDefaultInteractions(Map<Item, CauldronInteraction> map) {
        map.put(ModItems.SCULK_ACID_BUCKET, ModCauldronInteraction::fillSculkAcidInteraction);
    }

    static InteractionResult fillBucket(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack, ItemStack itemStack2, Predicate<BlockState> predicate, SoundEvent soundEvent) {
        if (!predicate.test(blockState)) {
            return InteractionResult.PASS;
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

            return InteractionResult.sidedSuccess(level.isClientSide);
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
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static InteractionResult fillSculkAcidInteraction(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack) {
        return (InteractionResult) (isUnderWater(level, blockPos) ? InteractionResult.CONSUME : emptyBucket(level, blockPos, player, interactionHand, itemStack, ModBlocks.SCULK_ACID_CAULDRON.defaultBlockState().setValue(SculkAcidCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY));
    }

    private static boolean isUnderWater(Level level, BlockPos blockPos) {
        FluidState fluidState = level.getFluidState(blockPos.above());
        return fluidState.is(FluidTags.WATER);
    }
}
