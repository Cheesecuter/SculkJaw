package ycpk.sculkandjaw.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.resources.Identifier;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.client.model.geom.ModModelLayers;
import ycpk.sculkandjaw.client.particle.ModParticleTypesClient;
import ycpk.sculkandjaw.client.renderer.blockentity.SculkJawBlockEntityRenderer;
import ycpk.sculkandjaw.level.material.ModFluids;
import ycpk.sculkandjaw.registry.ModBlockEntities;
import ycpk.sculkandjaw.registry.ModBlocks;

public class SculkAndJawClient implements ClientModInitializer {
    private static final Identifier SCULK_ACID_STILL_TEXTURE =
            Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_still");
    private static final Identifier SCULK_ACID_FLOWING_TEXTURE =
            Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "block/sculk_acid_flow");

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
        BlockRenderLayerMap.putFluid(ModFluids.SCULK_ACID, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putFluid(ModFluids.FLOWING_SCULK_ACID, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.SCULK_JELLY, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.SCULK_ACID_CAULDRON, ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.SCULK_TELEPORTER, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.ACIDCOIL_CATTAIL, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.UMBRAFERN, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.LARGE_UMBRAFERN, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.POTTED_UMBRAFERN, ChunkSectionLayer.CUTOUT);
    }
}
