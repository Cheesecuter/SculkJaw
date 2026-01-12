package ycpk.sculkjaw;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ycpk.sculkjaw.core.cauldron.ModCauldronInteraction;
import ycpk.sculkjaw.core.particles.ModParticleTypes;
import ycpk.sculkjaw.core.sculk_jaw.SculkJawInteraction;
import ycpk.sculkjaw.level.material.ModFluids;
import ycpk.sculkjaw.level.storage.loot.ModBuiltInLootTables;
import ycpk.sculkjaw.registry.*;
import ycpk.sculkjaw.world.item.alchemy.ModPotions;

public class Sculkjaw implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Sculkjaw");
    public static final String MOD_ID = "ycpk";

    @Override
    public void onInitialize() {
        ModCauldronInteraction.bootStrap();
        SculkJawInteraction.bootStrap();

        ModBlockEntities.registerModBlockEntities();
        ModBlocks.registerModBlocks();
        ModDamageSources.registerModDamageSources();
        ModEffects.registerModEffects();
        ModItems.registerModItems();
        ModParticles.registerModParticles();
        ModSoundEvents.registerSoundEvents();
        ModPotions.registerModPotions();
        ModParticleTypes.registerModParticleTypes();
        ModTags.registerModTags();
        ModBuiltInLootTables.registerModBuiltInLootTables();
        ModFluids.registerModFluids();

        LOGGER.info("Mod " + MOD_ID + " initialized");
    }
}
