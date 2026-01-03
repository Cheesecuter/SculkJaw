package ycpk.sculkjaw.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.registry.ModBlocks;
import ycpk.sculkjaw.blocks.custom.SculkJawBlock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mixin(SculkBlock.class)
public abstract class SculkAttemptUseChargePlacingSculkJawMixin {

    @Unique
    private static final Map<Integer, Direction> FACING_MAP = new HashMap<>(Map.of(0, Direction.NORTH, 1, Direction.EAST, 2, Direction.SOUTH, 3, Direction.WEST));

    @Unique
    private static int getDecayPenalty(SculkSpreader sculkSpreader, BlockPos blockPos, BlockPos blockPos2, int i){
        int j = sculkSpreader.noGrowthRadius();
        float f = Mth.square((float)Math.sqrt(blockPos.distSqr(blockPos2)) - (float)j);
        int k = Mth.square(24 - j);
        float g = Math.min(1.0F, f / (float)k);
        return Math.max(1, (int)((float)i * g * 0.5F));
    }

    @Unique
    private static boolean canPlaceSculkJawGrowth(LevelAccessor levelAccessor, BlockPos blockPos){
        BlockState blockState = levelAccessor.getBlockState(blockPos);
        BlockState blockState1 = levelAccessor.getBlockState(blockPos.above());
        if ((blockState1.isAir() || blockState1.is(Blocks.WATER) && blockState1.getFluidState().is(Fluids.WATER)) && blockState.is(Blocks.SCULK)) {
            int i = 0;
            Iterator var4 = BlockPos.betweenClosed(blockPos.offset(-4, -1, -4), blockPos.offset(4, 1, 4)).iterator();
            do {
                if (!var4.hasNext()) {
                    return true;
                }
                BlockPos blockPos2 = (BlockPos)var4.next();
                BlockState blockState2 = levelAccessor.getBlockState(blockPos2);
                if (blockState2.is(ModBlocks.SCULK_JAW)) {
                    ++i;
                }
            } while(i <= 1);
            return false;
        } else {
            return false;
        }
    }

    @Inject(at = @At("RETURN"), method = "attemptUseCharge", cancellable = true)
    public void attemptUseChargePlacingSculkJaw(SculkSpreader.ChargeCursor chargeCursor, LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, SculkSpreader sculkSpreader, boolean shouldConvertToBlock, CallbackInfoReturnable<Integer> cir){
        int i = chargeCursor.getCharge();
        if (i != 0 && randomSource.nextInt(sculkSpreader.chargeDecayRate()) == 0) {
            BlockPos blockPos2 = chargeCursor.getPos();
            boolean bl2 = blockPos2.closerThan(blockPos, (double)sculkSpreader.noGrowthRadius());
            if (!bl2 && canPlaceSculkJawGrowth(levelAccessor, blockPos2)) {
                int j = sculkSpreader.growthSpawnCost();
                if (randomSource.nextInt(j) < i) {
                    BlockState blockState = ModBlocks.SCULK_JAW.defaultBlockState().setValue(SculkJawBlock.FACING, FACING_MAP.get(randomSource.nextInt(4)));
                    levelAccessor.setBlock(blockPos2, blockState, 3);
                    levelAccessor.playSound((Entity)null, blockPos2, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                cir.setReturnValue(Math.max(0, i - j));
            } else {
                cir.setReturnValue(randomSource.nextInt(sculkSpreader.additionalDecayRate()) != 0 ? i : i - (bl2 ? 1 : getDecayPenalty(sculkSpreader, blockPos2, blockPos, i)));
            }
        } else {
            cir.setReturnValue(i);
        }
    }
}
