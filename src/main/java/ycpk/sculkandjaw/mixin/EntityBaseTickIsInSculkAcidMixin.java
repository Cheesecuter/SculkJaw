package ycpk.sculkandjaw.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkandjaw.level.material.SculkAcidFluid;

@Mixin(Entity.class)
public abstract class EntityBaseTickIsInSculkAcidMixin {
    @Shadow
    public abstract Level level();

    @Shadow public abstract AABB getBoundingBox();

    @Inject(method = "checkInsideBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;entityInside(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)V"))
    private void EntityInSculkAcid(CallbackInfo ci) {
        AABB aabb = this.getBoundingBox();
        BlockPos blockPos = BlockPos.containing(aabb.minX + 1.0E-7, aabb.minY + 1.0E-7, aabb.minZ + 1.0E-7);
        Fluid fluid = this.level().getFluidState(blockPos).getType();
        if (fluid instanceof SculkAcidFluid sculkAcidFluid) {
            Entity entity = (Entity) (Object) this;
            //sculkAcidFluid.entityInside(this.level(), blockPos, entity);
        }
    }
}
