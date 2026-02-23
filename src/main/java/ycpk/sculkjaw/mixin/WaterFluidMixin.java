package ycpk.sculkjaw.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ycpk.sculkjaw.tags.ModFluidTags;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {
    @Inject(at = @At("HEAD"), method = "canBeReplacedWith", cancellable = true)
    public void canBeReplacedWithSculkAcid(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = (direction == Direction.DOWN && (!fluid.is(FluidTags.WATER) || fluid.is(ModFluidTags.SCULK_ACID))) || (fluid.is(ModFluidTags.SCULK_ACID));
        cir.setReturnValue(bl);
    }
}
