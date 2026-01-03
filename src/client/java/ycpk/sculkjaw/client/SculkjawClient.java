package ycpk.sculkjaw.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import ycpk.sculkjaw.client.renderer.blockentity.SculkJawBlockEntityRenderer;
import ycpk.sculkjaw.registry.ModBlockEntities;

public class SculkjawClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ModBlockEntities.SCULK_JAW_BLOCK_ENTITY, SculkJawBlockEntityRenderer::new);
    }
}
