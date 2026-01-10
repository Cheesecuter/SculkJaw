package ycpk.sculkjaw.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ycpk.sculkjaw.core.cauldron.ModCauldronInteraction;
import ycpk.sculkjaw.registry.ModItems;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {
    @Shadow @Final protected CauldronInteraction.InteractionMap interactions;

    /*@Unique
    private final ModCauldronInteraction.InteractionMap modInteractions = (ModCauldronInteraction.InteractionMap)(Object)this.interactions;*/

    @Inject(at = @At("HEAD"), method = "useItemOn")
    private void useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if(itemStack.getItem().equals(ModItems.SCULK_ACID_BOTTLE) || itemStack.getItem().equals(ModItems.SCULK_ACID_BUCKET)) {
            ModCauldronInteraction.InteractionMap modInteractions = (ModCauldronInteraction.InteractionMap)(Object)this.interactions;
            ModCauldronInteraction cauldronInteraction = (ModCauldronInteraction) modInteractions.map().get(itemStack.getItem());
            cir.setReturnValue(cauldronInteraction.interact(blockState, level, blockPos, player, interactionHand, itemStack));
        }
    }
}
