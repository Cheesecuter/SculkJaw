package com.ycpk.sculkandjaw;

import com.ycpk.sculkandjaw.client.model.SculkJawCombinedStomachModel;
import com.ycpk.sculkandjaw.client.model.SculkJawStomachModel;
import com.ycpk.sculkandjaw.client.model.geom.ModModelLayers;
import com.ycpk.sculkandjaw.client.particle.ModParticleTypesClient;
import com.ycpk.sculkandjaw.client.renderer.blockentity.SculkJawBlockEntityRenderer;
import com.ycpk.sculkandjaw.level.material.ModFluids;
import com.ycpk.sculkandjaw.registry.ModBlockEntities;
import com.ycpk.sculkandjaw.registry.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterSpriteSourceTypesEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.intellij.lang.annotations.Identifier;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = SculkAndJaw.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = SculkAndJaw.MOD_ID, value = Dist.CLIENT)
public class SculkAndJawClient {
    public SculkAndJawClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        SculkAndJaw.LOGGER.info("HELLO FROM CLIENT SETUP");
        SculkAndJaw.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        ModModelLayers.registerModModelLayers();
        ItemBlockRenderTypes.setRenderLayer(ModFluids.SCULK_ACID.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SCULK_ACID.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SCULK_JELLY.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SCULK_ACID_CAULDRON.value(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ACIDOPHILIC_CORDYCEPS.value(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.UMBRAFERN.value(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LARGE_UMBRAFERN.value(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_UMBRAFERN.value(), RenderType.cutout());
    }

    @SubscribeEvent
    static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(
                new IClientFluidTypeExtensions() {
                    private static final ResourceLocation UNDER_SCULK_ACID_LOCATION = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/misc/under_sculk_acid.png");
                    private static final ResourceLocation SCULK_ACID_STILL = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_still");
                    private static final ResourceLocation SCULK_ACID_FLOW = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_flow");
                    private static final ResourceLocation SCULK_ACID_OVERLAY = ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_overlay");

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
                },
                ModFluids.SCULK_ACID_FLUID_TYPE.value()
        );
    }

    @SubscribeEvent
    static void onRegisterFluidModels(RegisterSpriteSourceTypesEvent event) {

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
