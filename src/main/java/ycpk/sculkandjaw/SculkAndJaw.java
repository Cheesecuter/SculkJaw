package ycpk.sculkandjaw;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ycpk.sculkandjaw.commands.ModCommands;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;
import ycpk.sculkandjaw.level.material.ModFluids;
import ycpk.sculkandjaw.registry.*;
import ycpk.sculkandjaw.world.damagesource.ModDamageSources;
import ycpk.sculkandjaw.world.item.alchemy.ModPotions;
import ycpk.sculkandjaw.worldgen.ModWorldGen;

@SuppressWarnings("removal")
@Mod(SculkAndJaw.MOD_ID)
public class SculkAndJaw {
    public static final Logger LOGGER = LoggerFactory.getLogger("SculkAndJaw");
    public static final String MOD_ID = "ycpk";

    public SculkAndJaw() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        ModTags.registerModTags();
        ModBlockEntities.registerModBlockEntities(modEventBus);
        ModFluids.registerModFluids(modEventBus);
        ModBlocks.registerModBlocks(modEventBus);
        ModCommands.registerModCommands();
        MinecraftForge.EVENT_BUS.register(this);
        ModDamageTypes.registerModDamageTypes();
        ModDamageSources.registerModDamageSources();
        ModParticleTypes.registerModParticleTypes(modEventBus);
        ModMobEffects.registerModEffects(modEventBus);
        ModItems.registerModItems(modEventBus);
        ModCreativeModeTabs.registerModCreativeModeTabs(modEventBus);
        ModSoundEvents.registerSoundEvents(modEventBus);
        ModPotions.registerModPotions(modEventBus);
        ModFeatures.registerModFeatures(modEventBus);
        ModWorldGen.registerModWorldGen();


        // Register the item to a creative tab
        modEventBus.addListener(ModCreativeModeTabs::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static ModDamageSources getDamageSources(Level level) {
        return new ModDamageSources(
                level.registryAccess()
        );
    }
}
