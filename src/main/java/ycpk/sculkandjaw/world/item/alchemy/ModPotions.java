package ycpk.sculkandjaw.world.item.alchemy;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModMobEffects;

public class ModPotions {
    public static void registerModPotions(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering Potions for Mod " + SculkAndJaw.MOD_ID);
        MOD_POTIONS.register(modEventBus);
    }

    public static final DeferredRegister<Potion> MOD_POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, SculkAndJaw.MOD_ID);
    public static final RegistryObject<Potion> ACID_ETCHING = MOD_POTIONS.register(
            "acid_etching",
            () -> new Potion(
                    "acid_etching",
                    new MobEffectInstance[]{new MobEffectInstance(ModMobEffects.ACID_ETCHING.get(), 3600)}
            )
    );
    public static final RegistryObject<Potion> LONG_ACID_ETCHING = MOD_POTIONS.register(
            "long_acid_etching",
            () -> new Potion(
                    "acid_etching",
                    new MobEffectInstance[]{new MobEffectInstance(ModMobEffects.ACID_ETCHING.get(), 9600)}
            )
    );
    public static final RegistryObject<Potion> STRONG_ACID_ETCHING = MOD_POTIONS.register(
            "strong_acid_etching",
            () -> new Potion(
                    "acid_etching",
                    new MobEffectInstance[]{new MobEffectInstance(ModMobEffects.ACID_ETCHING.get(), 1800, 1)}
            )
    );

    public ModPotions() {

    }
}
