package com.ycpk.sculkandjaw.level.material;

import com.ycpk.sculkandjaw.core.particles.ModParticleTypes;
import com.ycpk.sculkandjaw.registry.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;

public class SculkAcidFluidType extends FluidType implements IClientFluidTypeExtensions {
    public SculkAcidFluidType() {
        super(Properties.create()
                .fallDistanceModifier(0.0F)
                .canDrown(true)
                .canSwim(true)
                .pathType(PathType.LAVA)
                .adjacentPathType(null)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .temperature(20)
                .addDripstoneDripping(PointedDripstoneBlock.LAVA_TRANSFER_PROBABILITY_PER_RANDOM_TICK, ModParticleTypes.DRIPPING_SCULK_ACID.get(), ModBlocks.SCULK_ACID_CAULDRON.value(), SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON)
        );
    }
}
