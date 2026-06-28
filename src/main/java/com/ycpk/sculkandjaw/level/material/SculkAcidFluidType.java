package com.ycpk.sculkandjaw.level.material;

import com.ycpk.sculkandjaw.registry.ModBlocks;
import com.ycpk.sculkandjaw.tags.ModFluidTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.material.Fluid;
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
                .lightLevel(1)
                .density(3000)
                .viscosity(6000)
                .supportsBoating(true)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .canConvertToSource(true)
                .temperature(20)
                //.addDripstoneDripping(PointedDripstoneBlock.LAVA_TRANSFER_PROBABILITY_PER_RANDOM_TICK, ParticleTypes.DRIPPING_DRIPSTONE_LAVA, ModBlocks.SCULK_ACID_CAULDRON.value(), SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON)
        );
    }
}
