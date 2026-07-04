package com.ycpk.sculkandjaw;

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
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.LoggerFactory;

@Mod(SculkAndJaw.MOD_ID)
public class SculkAndJaw {
    public static final Logger LOGGER = LoggerFactory.getLogger("SculkAndJaw");
    public static final String MOD_ID = "ycpk";

    public SculkAndJaw(IEventBus modEventBus, ModContainer modContainer) {
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

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(ModCreativeModeTabs::addCreative);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

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
