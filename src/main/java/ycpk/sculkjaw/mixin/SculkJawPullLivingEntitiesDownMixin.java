package ycpk.sculkjaw.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkjaw.registry.ModBlocks;

@Mixin(LivingEntity.class)
public abstract class SculkJawPullLivingEntitiesDownMixin {
    @Inject(at = @At("TAIL"), method = "aiStep")
    public void sculkJawPullLivingEntitiesDown(CallbackInfo ci)
    {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if(!(livingEntity instanceof Warden)){
            BlockState jawState = livingEntity.level().getBlockState(livingEntity.blockPosition().below());
            if(jawState.getBlock() == ModBlocks.SCULK_JAW && !livingEntity.isShiftKeyDown()){
                if(livingEntity instanceof Player && ((Player) livingEntity).getAbilities().flying){
                    return;
                }
                livingEntity.push(livingEntity.blockPosition().below().getCenter().add(0, 0.3, 0).subtract(livingEntity.position()).multiply(new Vec3(0.3, 0.1, 0.3)));
                livingEntity.makeStuckInBlock(livingEntity.level().getBlockState(livingEntity.blockPosition().below()), new Vec3(0.5, 1, 0.5));
            }
        }
    }
}
