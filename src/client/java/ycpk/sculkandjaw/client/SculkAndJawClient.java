package ycpk.sculkandjaw.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.fabricmc.fabric.mixin.block.ChunkSectionMixin;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.client.model.geom.ModModelLayers;
import ycpk.sculkandjaw.client.particle.ModParticleTypesClient;
import ycpk.sculkandjaw.client.renderer.blockentity.SculkJawBlockEntityRenderer;
import ycpk.sculkandjaw.level.marerial.ModFluids;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.registry.ModBlocks;

public class SculkAndJawClient implements ClientModInitializer {
    private static final ResourceLocation SCULK_ACID_STILL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_still");
    private static final ResourceLocation SCULK_ACID_FLOWING_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_flow");

    @Override
    public void onInitializeClient() {
        ModModelLayers.registerModModelLayers();
        ModParticleTypesClient.registerModParticleTypesClient();
        BlockEntityRenderers.register(ModBlockEntities.SCULK_JAW_BLOCK_ENTITY, SculkJawBlockEntityRenderer::new);
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.SCULK_ACID, ModFluids.FLOWING_SCULK_ACID,
                new SimpleFluidRenderHandler(
                        SCULK_ACID_STILL_TEXTURE,
                        SCULK_ACID_FLOWING_TEXTURE,
                        0x299983
                ));
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.SCULK_ACID, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putFluid(ModFluids.FLOWING_SCULK_ACID, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SCULK_JELLY, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.UMBRAFERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_UMBRAFERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_UMBRAFERN, RenderType.cutout());
    }
}
