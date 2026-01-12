package ycpk.sculkjaw.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.client.renderer.blockentity.SculkJawBlockEntityRenderer;
import ycpk.sculkjaw.core.particles.ModParticleTypes;
import ycpk.sculkjaw.core.particles.ModSimpleParticleType;
import ycpk.sculkjaw.level.material.ModFluids;
import ycpk.sculkjaw.registry.ModBlockEntities;

public class SculkjawClient implements ClientModInitializer {

    private static final ResourceLocation SCULK_ACID_STILL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "block/sculk_acid_still");
    private static final ResourceLocation SCULK_ACID_FLOWING_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "block/sculk_acid_flow");

    private static final ResourceLocation SCULKOPHOBIA_PARTICLE =
            ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "particles/sculkophobia");

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(ModBlockEntities.SCULK_JAW_BLOCK_ENTITY, SculkJawBlockEntityRenderer::new);
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.SCULK_ACID, ModFluids.FLOWING_SCULK_ACID,
                new SimpleFluidRenderHandler(
                        SCULK_ACID_STILL_TEXTURE,
                        SCULK_ACID_FLOWING_TEXTURE,
                        0x299983
                ));

        ParticleFactoryRegistry.getInstance().register(ModParticleTypes.SCULKOPHOBIA, SpellParticle.Provider::new);
    }
}
