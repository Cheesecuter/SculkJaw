package com.ycpk.sculkandjaw.level.material;

import com.ycpk.sculkandjaw.SculkAndJaw;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModFluids {
    public static void registerModFluids(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Fluids for Mod " + SculkAndJaw.MOD_ID);
        MOD_FLUIDS.register(modEventBus);
        MOD_FLUID_TYPES.register(modEventBus);
    }

    public static final DeferredRegister<Fluid> MOD_FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, SculkAndJaw.MOD_ID);
    public static final DeferredRegister<FluidType> MOD_FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, SculkAndJaw.MOD_ID);

    public static final DeferredHolder<Fluid, SculkAcidFluid.Flowing> FLOWING_SCULK_ACID = MOD_FLUIDS.register(
            "flowing_sculk_acid",
            SculkAcidFluid.Flowing::new
    );
    public static final DeferredHolder<Fluid, SculkAcidFluid.Source> SCULK_ACID = MOD_FLUIDS.register(
            "sculk_acid",
            SculkAcidFluid.Source::new
    );
    public static final DeferredHolder<FluidType, SculkAcidFluidType> SCULK_ACID_FLUID_TYPE = MOD_FLUID_TYPES.register(
            "sculk_acid",
            SculkAcidFluidType::new
    );
}
