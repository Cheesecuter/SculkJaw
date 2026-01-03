package ycpk.sculkjaw.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import ycpk.sculkjaw.Sculkjaw;

public class ModDamageSources {
    public static void registerModDamageSources(){
        Sculkjaw.LOGGER.info("Registering Damage Sources for Mod " + Sculkjaw.MOD_ID);
    }

    public static ResourceKey<DamageType> SCULK_JAW_BITE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "sculk_jaw_bite"));
    public static ResourceKey<DamageType> SCULK_JAW_ACID = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, "sculk_jaw_acid"));
}
