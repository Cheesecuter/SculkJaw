package ycpk.sculkandjaw.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import ycpk.sculkandjaw.level.material.SculkAcidFluid;

@Mixin(Entity.class)
public abstract class EntityBaseTickIsInSculkAcid {
    @Shadow public abstract Level level();

    @WrapOperation(method = "checkInsideBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;entityInside(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V"))
    private void EntityInSculkAcid(BlockState instance, Level level, BlockPos blockPos, Entity entity, Operation<Void> original) {
        Fluid fluid = this.level().getFluidState(blockPos).getType();
        if (fluid instanceof SculkAcidFluid sculkAcidFluid) {
            sculkAcidFluid.entityInside(level, blockPos, entity);
        }
        original.call(instance, level, blockPos, entity);
    }
}
