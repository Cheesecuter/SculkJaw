package ycpk.sculkandjaw;

import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ycpk.sculkandjaw.commands.ModCommands;
import ycpk.sculkandjaw.core.cauldron.ModCauldronInteraction;
import ycpk.sculkandjaw.core.dispenser.ModDispenseItemBehavior;
import ycpk.sculkandjaw.core.particles.ModParticleTypes;
import ycpk.sculkandjaw.core.sculk_jaw.SculkJawInteraction;
import ycpk.sculkandjaw.level.material.ModFluids;
import ycpk.sculkandjaw.registry.*;
import ycpk.sculkandjaw.world.damagesource.ModDamageSources;
import ycpk.sculkandjaw.world.item.alchemy.ModPotions;
import ycpk.sculkandjaw.world.item.alchemy.ModPotioonBrewingRecipes;
import ycpk.sculkandjaw.worldgen.ModWorldGen;

@SuppressWarnings("removal")
@Mod(SculkAndJaw.MOD_ID)
public class SculkAndJaw {
    public static final Logger LOGGER = LoggerFactory.getLogger("SculkAndJaw");
    public static final String MOD_ID = "ycpk";

    public SculkAndJaw() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModTags.registerModTags();
        ModBlockEntities.registerModBlockEntities(modEventBus);
        ModFluids.registerModFluids(modEventBus);
        ModBlocks.registerModBlocks(modEventBus);
        ModCommands.registerModCommands();
        MinecraftForge.EVENT_BUS.register(ModCommands.class);
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
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(ModCreativeModeTabs::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModPotioonBrewingRecipes.registerModPotionBrewingRecipes(event);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        ModCauldronInteraction.bootStrap();
        ModDispenseItemBehavior.bootStrap();
        SculkJawInteraction.bootStrap();
        LOGGER.info("Mod " + MOD_ID + " initialized");
    }

    public static ModDamageSources getDamageSources(Level level) {
        return new ModDamageSources(level.registryAccess());
    }
}
