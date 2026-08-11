package ycpk.sculkandjaw;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import ycpk.sculkandjaw.client.model.SculkJawCombinedStomachModel;
import ycpk.sculkandjaw.client.model.SculkJawStomachModel;
import ycpk.sculkandjaw.client.model.geom.ModModelLayers;
import ycpk.sculkandjaw.client.particle.ModParticleTypesClient;
import ycpk.sculkandjaw.client.renderer.blockentity.SculkJawBlockEntityRenderer;
import ycpk.sculkandjaw.level.material.ModFluids;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.registry.ModBlocks;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = SculkAndJaw.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
