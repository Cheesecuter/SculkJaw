package ycpk.sculkandjaw.world.item.alchemy;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModItems;

public class ModPotioonBrewingRecipes {
    public static void registerModPotionBrewingRecipes(FMLCommonSetupEvent event) {
        SculkAndJaw.LOGGER.info("Registering Potion Brewing Recipes for Mod " + SculkAndJaw.MOD_ID);
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(
                    new PotionBrewingRecipe(
                            Potions.AWKWARD,
                            ModItems.SCULK_JAW.get(),
                            ModPotions.ACID_ETCHING.get()
                    )
            );
            BrewingRecipeRegistry.addRecipe(
                    new PotionBrewingRecipe(
                            ModPotions.ACID_ETCHING.get(),
                            Items.REDSTONE,
                            ModPotions.LONG_ACID_ETCHING.get()
                    )
            );
            BrewingRecipeRegistry.addRecipe(
                    new PotionBrewingRecipe(
                            ModPotions.ACID_ETCHING.get(),
                            Items.GLOWSTONE_DUST,
                            ModPotions.STRONG_ACID_ETCHING.get()
                    )
            );
        });
    }
}
