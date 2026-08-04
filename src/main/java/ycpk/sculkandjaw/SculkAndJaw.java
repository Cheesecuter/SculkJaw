package ycpk.sculkandjaw;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ycpk.sculkandjaw.commands.ModCommands;
import ycpk.sculkandjaw.core.cauldron.ModCauldronInteraction;
import ycpk.sculkandjaw.core.dispenser.ModDispenseItemBehavior;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;
import ycpk.sculkandjaw.core.sculk_jaw.SculkJawInteraction;
import ycpk.sculkandjaw.level.material.ModFluids;
import ycpk.sculkandjaw.registry.*;
import ycpk.sculkandjaw.world.item.alchemy.ModPotions;
import ycpk.sculkandjaw.worldgen.ModWorldGen;

public class SculkAndJaw implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("SculkAndJaw");
    public static final String MOD_ID = "ycpk";

    @Override
    public void onInitialize() {
        ModCauldronInteraction.bootStrap();
        ModDispenseItemBehavior.bootStrap();
        SculkJawInteraction.bootStrap();

        ModBlockEntities.registerModBlockEntities();
        ModBlocks.registerModBlocks();
        ModCommands.registerModCommands();
        ModDamageTypes.registerModDamageTypes();
        ModMobEffects.registerModEffects();
        ModItems.registerModItems();
        ModCreativeModeTabs.registerModCreativeModeTabs();
        ModParticleTypes.registerModParticleTypes();
        ModSoundEvents.registerSoundEvents();
        ModPotions.registerModPotions();
        ModTags.registerModTags();
        ModFluids.registerModFluids();
        //ModFeatures.registerModFeatures();
        //ModWorldGen.registerModWorldGen();

        LOGGER.info("Mod " + MOD_ID + " initialized");
    }
}
