package ycpk.sculkjaw.world.item.alchemy;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.registry.ModEffects;

public class ModPotions {
    public static void registerModPotions() {
        Sculkjaw.LOGGER.info("Registering Potions for Mod " + Sculkjaw.MOD_ID);
    }

    public static final Holder<Potion> ACID_ETCHING = register("acid_etching", new Potion("acid_etching", new MobEffectInstance[]{new MobEffectInstance(ModEffects.ACID_ETCHING, 3600)}));

    public ModPotions() {

    }

    private static Holder<Potion> register(String identifier, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, identifier), potion);
    }
}
