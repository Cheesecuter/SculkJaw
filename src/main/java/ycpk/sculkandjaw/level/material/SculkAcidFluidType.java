package ycpk.sculkandjaw.level.material;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

public class SculkAcidFluidType extends FluidType implements IClientFluidTypeExtensions {
    public SculkAcidFluidType() {
        super(Properties.create()
                .fallDistanceModifier(0.0F)
                .canDrown(true)
                .canSwim(true)
                .pathType(BlockPathTypes.LAVA.LAVA)
                .adjacentPathType(null)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .temperature(20)
        );
    }
}
