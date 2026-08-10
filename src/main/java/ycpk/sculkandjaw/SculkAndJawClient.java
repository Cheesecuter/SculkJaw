package ycpk.sculkandjaw;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import ycpk.sculkandjaw.client.model.SculkJawCombinedStomachModel;
import ycpk.sculkandjaw.client.model.SculkJawStomachModel;
import ycpk.sculkandjaw.client.model.geom.ModModelLayers;
import ycpk.sculkandjaw.client.particle.ModParticleTypesClient;
import ycpk.sculkandjaw.client.renderer.blockentity.SculkJawBlockEntityRenderer;
import ycpk.sculkandjaw.level.material.ModFluids;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.registry.ModBlocks;
import ycpk.sculkandjaw.registry.ModItems;

import java.util.function.Consumer;

@SuppressWarnings("removal")
@Mod(value = SculkAndJaw.MOD_ID)
@Mod.EventBusSubscriber(modid = SculkAndJaw.MOD_ID, value = Dist.CLIENT)
public class SculkAndJawClient {
    public SculkAndJawClient(ModContainer container) {

    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        ModModelLayers.registerModModelLayers();
        ItemBlockRenderTypes.setRenderLayer(ModFluids.SCULK_ACID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SCULK_ACID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SCULK_JELLY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SCULK_ACID_CAULDRON.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ACIDCOIL_CATTAIL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.UMBRAFERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LARGE_UMBRAFERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_UMBRAFERN.get(), RenderType.cutout());
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.SOUND_EVENTS, (helper) -> {
            helper.register(SoundEvents.BUCKET_EMPTY.getLocation(), SoundEvent.createVariableRangeEvent(SoundEvents.BUCKET_EMPTY.getLocation()));
            helper.register(SoundEvents.BUCKET_FILL.getLocation(), SoundEvent.createVariableRangeEvent(SoundEvents.BUCKET_FILL.getLocation()));
        });
        event.register(ForgeRegistries.Keys.FLUID_TYPES, (helper) -> {
            helper.register(
                    ModFluids.SCULK_ACID_FLUID_TYPE.getId(),
                    new FluidType(
                            FluidType.Properties.create()
                                    .density(1024)
                                    .viscosity(1024)
                                    .sound(SoundActions.BUCKET_FILL, (SoundEvent) SoundEvents.BUCKET_FILL)
                                    .sound(SoundActions.BUCKET_EMPTY, (SoundEvent) SoundEvents.BUCKET_EMPTY)
                    ) {
                        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                            consumer.accept(new IClientFluidTypeExtensions() {
                                private static final ResourceLocation SCULK_ACID_STILL = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_still");
                                private static final ResourceLocation SCULK_ACID_FLOW = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_flow");
                                private static final ResourceLocation SCULK_ACID_OVERLAY = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_overlay");
                                private static final ResourceLocation UNDER_SCULK_ACID_LOCATION = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/misc/under_sculk_acid.png");

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
        });
        event.register(ForgeRegistries.Keys.FLUIDS, (helper) -> {
            ForgeFlowingFluid.Properties properties = (new ForgeFlowingFluid.Properties(ModFluids.SCULK_ACID_FLUID_TYPE, ModFluids.SCULK_ACID, ModFluids.FLOWING_SCULK_ACID)).bucket(() -> {
                return ModItems.SCULK_ACID_BUCKET.get();
            });
            helper.register(ModFluids.SCULK_ACID.getId(), new ForgeFlowingFluid.Source(properties));
            helper.register(ModFluids.FLOWING_SCULK_ACID.getId(), new ForgeFlowingFluid.Flowing(properties));
        });
    }

    @SubscribeEvent
    static void registerModParticleTypesClient(RegisterParticleProvidersEvent event) {
        ModParticleTypesClient.registerModParticleTypesClient(event);
    }

    @SubscribeEvent
    static void registerModBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntities.SCULK_JAW_BLOCK_ENTITY.get(),
                SculkJawBlockEntityRenderer::new
        );
    }

    @SubscribeEvent
    static void registerModLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                ModModelLayers.SCULK_JAW_STOMACH,
                SculkJawStomachModel::createBodyLayer
        );
        event.registerLayerDefinition(
                ModModelLayers.SCULK_JAW_COMBINED_STOMACH,
                SculkJawCombinedStomachModel::createBodyLayer
        );
    }
}
