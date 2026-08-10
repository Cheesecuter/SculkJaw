package ycpk.sculkandjaw.level.material;

import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModBlocks;
import ycpk.sculkandjaw.registry.ModItems;

import java.util.Iterator;
import java.util.function.Consumer;

public class ModFluids {
    public static void registerModFluids(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Fluids for Mod " + SculkAndJaw.MOD_ID);
        MOD_FLUIDS.register(modEventBus);
        MOD_FLUID_TYPES.register(modEventBus);
    }

    public static final DeferredRegister<Fluid> MOD_FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, SculkAndJaw.MOD_ID);
    public static final DeferredRegister<FluidType> MOD_FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, SculkAndJaw.MOD_ID);
    private static final ResourceLocation SCULK_ACID_STILL = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_still");
    private static final ResourceLocation SCULK_ACID_FLOW = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_flow");
    private static final ResourceLocation SCULK_ACID_OVERLAY = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_overlay");
    private static final ResourceLocation UNDER_SCULK_ACID_LOCATION = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/misc/under_sculk_acid.png");
    public static final RegistryObject<FluidType> SCULK_ACID_FLUID_TYPE;
    public static final RegistryObject<SculkAcidFluid.Flowing> FLOWING_SCULK_ACID;
    public static final RegistryObject<SculkAcidFluid> SCULK_ACID;

    static {
        SCULK_ACID_FLUID_TYPE = MOD_FLUID_TYPES.register(
                "sculk_acid",
                () -> new FluidType(
                        FluidType.Properties.create()
                                .canDrown(true)
                                .canSwim(true)
                                .pathType(BlockPathTypes.LAVA)
                                .adjacentPathType(null)
                                .supportsBoating(true)
                                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                                .temperature(20)
                ) {
                    @Override
                    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                        consumer.accept(new IClientFluidTypeExtensions() {
                            @Override
                            public ResourceLocation getStillTexture() {
                                return SCULK_ACID_STILL;
                            }

                            @Override
                            public ResourceLocation getFlowingTexture() {
                                return SCULK_ACID_FLOW;
                            }

                            @Override
                            public ResourceLocation getOverlayTexture() {
                                return SCULK_ACID_OVERLAY;
                            }

                            @Override
                            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                                return UNDER_SCULK_ACID_LOCATION;
                            }

                            @Override
                            public int getTintColor() {
                                return 0xFF299983;
                            }
                        });
                    }
                });

        FLOWING_SCULK_ACID = MOD_FLUIDS.register(
                "flowing_sculk_acid",
                SculkAcidFluid.Flowing::new
        );

        SCULK_ACID = MOD_FLUIDS.register(
                "sculk_acid",
                SculkAcidFluid.Source::new
        );
    }
    /*public static final RegistryObject<FlowingFluid> FLOWING_SCULK_ACID = RegistryObject.create(
            ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "flowing_sculk_acid"),
            ForgeRegistries.FLUIDS
    );
    public static final RegistryObject<FlowingFluid> SCULK_ACID = RegistryObject.create(
            ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid"),
            ForgeRegistries.FLUIDS
    );
    public static final RegistryObject<FluidType> SCULK_ACID_FLUID_TYPE = RegistryObject.create(
            ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid"),
            ForgeRegistries.Keys.FLUID_TYPES.location(),
            SculkAndJaw.MOD_ID
    );*/

    static {
        Iterator var0  = BuiltInRegistries.FLUID.iterator();

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
