package ycpk.sculkandjaw.level.material;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import ycpk.sculkandjaw.SculkAndJaw;

import java.util.Iterator;

public class ModFluids {
    public static void registerModFluids() {
        SculkAndJaw.LOGGER.info("Registering Fluids for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final FlowingFluid FLOWING_SCULK_ACID = (FlowingFluid) register("flowing_sculk_acid", new SculkAcidFluid.Flowing());
    public static final FlowingFluid SCULK_ACID = (FlowingFluid) register("sculk_acid", new SculkAcidFluid.Source());

    private static <T extends Fluid> T register(String identifier, T fluid) {
        return Registry.register(BuiltInRegistries.FLUID, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, identifier), fluid);
    }

    static {
        Iterator var0 = BuiltInRegistries.FLUID.iterator();

        while (var0.hasNext()) {
            Fluid fluid = (Fluid) var0.next();
            UnmodifiableIterator var2 = fluid.getStateDefinition().getPossibleStates().iterator();

            while (var2.hasNext()) {
                FluidState fluidState = (FluidState) var2.next();
                Fluid.FLUID_STATE_REGISTRY.add(fluidState);
            }
        }
    }
}
