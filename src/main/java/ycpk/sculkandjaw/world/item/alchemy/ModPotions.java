package ycpk.sculkandjaw.world.item.alchemy;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModEffects;

public class ModPotions {
    public static void registerModPotions() {
        SculkAndJaw.LOGGER.info("Registering Potions for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final Holder<Potion> ACID_ETCHING = register("acid_etching", new Potion("acid_etching", new MobEffectInstance[]{new MobEffectInstance(ModEffects.ACID_ETCHING, 3600)}));
    public static final Holder<Potion> LONG_ACID_ETCHING = register("long_acid_etching", new Potion("acid_etching", new MobEffectInstance[]{new MobEffectInstance(ModEffects.ACID_ETCHING, 9600)}));
    public static final Holder<Potion> STRONG_ACID_ETCHING = register("strong_acid_etching", new Potion("acid_etching", new MobEffectInstance[]{new MobEffectInstance(ModEffects.ACID_ETCHING, 1800, 1)}));

    public ModPotions() {

    }

    private static Holder<Potion> register(String identifier, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, identifier), potion);
    }
}
