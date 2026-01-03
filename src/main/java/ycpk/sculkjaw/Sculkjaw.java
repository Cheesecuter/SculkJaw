package ycpk.sculkjaw;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ycpk.sculkjaw.level.storage.loot.ModBuiltInLootTables;
import ycpk.sculkjaw.registry.*;

public class Sculkjaw implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Sculkjaw");
    public static final String MOD_ID = "ycpk";

    @Override
    public void onInitialize() {
        ModBlockEntities.registerModBlockEntities();
        ModBlocks.registerModBlocks();
        ModDamageSources.registerModDamageSources();
        ModEffects.registerModEffects();
        ModItems.registerModItems();
        ModParticles.registerModParticles();
        ModSoundEvents.registerSoundEvents();
        ModTags.registerModTags();
        ModBuiltInLootTables.registerModBuiltInLootTables();

        LOGGER.info("Mod " + MOD_ID + " initialized");
    }
}
