package ycpk.sculkjaw.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import org.intellij.lang.annotations.Identifier;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.client.renderer.blockentity.SculkJawBlockEntityRenderer;
import ycpk.sculkjaw.level.material.ModFluids;
import ycpk.sculkjaw.registry.ModBlockEntities;

public class SculkjawClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ModBlockEntities.SCULK_JAW_BLOCK_ENTITY, SculkJawBlockEntityRenderer::new);
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.SCULK_ACID, ModFluids.FLOWING_SCULK_ACID,
                new SimpleFluidRenderHandler(
                        /*ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/block/sculk_acid"),
                        ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "textures/block/sculk_acid"),*/
                        ResourceLocation.withDefaultNamespace("block/water_still"),
                        ResourceLocation.withDefaultNamespace("block/water_flow"),
                        0x00A4A4
                ));
    }
}
