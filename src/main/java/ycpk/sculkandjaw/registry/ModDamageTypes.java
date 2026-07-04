package ycpk.sculkandjaw.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModDamageTypes {
    public static void registerModDamageTypes(){
        SculkAndJaw.LOGGER.info("Registering Damage Types for Mod " + SculkAndJaw.MOD_ID);
    }

    public static ResourceKey<DamageType> SCULK_JAW_BITE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_jaw_bite"));
    public static ResourceKey<DamageType> SCULK_ACID = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "sculk_acid"));
}
