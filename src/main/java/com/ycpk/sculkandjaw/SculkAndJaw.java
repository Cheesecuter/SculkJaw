package com.ycpk.sculkandjaw;

import com.ycpk.sculkandjaw.blocks.modblocks.SculkAcidCauldronBlock;
import com.ycpk.sculkandjaw.core.cauldron.ModCauldronInteratction;
import com.ycpk.sculkandjaw.core.dispenser.ModDispenseItemBehavior;
import com.ycpk.sculkandjaw.core.particles.ModParticleTypes;
import com.ycpk.sculkandjaw.core.sculk_jaw.SculkJawInteraction;
import com.ycpk.sculkandjaw.level.material.ModFluids;
import com.ycpk.sculkandjaw.registry.*;
import com.ycpk.sculkandjaw.world.item.alchemy.ModPotions;
import com.ycpk.sculkandjaw.worldgen.ModWorldGen;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.LoggerFactory;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SculkAndJaw.MOD_ID)
public class SculkAndJaw {
    public static final Logger LOGGER = LoggerFactory.getLogger("SculkAndJaw");
    public static final String MOD_ID = "ycpk";

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public SculkAndJaw(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModTags.registerModTags();
        ModBlockEntities.registerModBlockEntities(modEventBus);
        ModFluids.registerModFluids(modEventBus);
        ModBlocks.registerModBlocks(modEventBus);
        ModCommands.registerModCommands();
        NeoForge.EVENT_BUS.register(ModCommands.class);
        ModDamageTypes.registerModDamageTypes();
        ModParticleTypes.registerModParticleTypes(modEventBus);
        ModMobEffects.registerModMobEffects(modEventBus);
        ModItems.registerModItems(modEventBus);
        ModCreativeModeTabs.registerModCreativeModeTabs(modEventBus);
        ModSoundEvents.registerSoundEvents(modEventBus);
        ModPotions.registerModPotions(modEventBus);
        ModFeatures.registerModFeatures(modEventBus);
        ModWorldGen.registerModWorldGen();

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (SculkAndJaw) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(ModCreativeModeTabs::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    private void generateData(final GatherDataEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        ModCauldronInteratction.bootStrap();
        ModDispenseItemBehavior.bootStrap();
        SculkJawInteraction.bootStrap();
        LOGGER.info("Mod " + MOD_ID + " initialized");
    }

    @SubscribeEvent
    public void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, ModItems.SCULK_JAW.get(), ModPotions.ACID_ETCHING);
        builder.addMix(ModPotions.ACID_ETCHING, Items.REDSTONE, ModPotions.LONG_ACID_ETCHING);
        builder.addMix(ModPotions.ACID_ETCHING, Items.GLOWSTONE_DUST, ModPotions.STRONG_ACID_ETCHING);
    }
}
