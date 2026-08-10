package ycpk.sculkandjaw.world.damagesource;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.registry.ModDamageTypes;

public class ModDamageSources {
    public static void registerModDamageSources(){
        SculkAndJaw.LOGGER.info("Registering Damage Sources for Mod " + SculkAndJaw.MOD_ID);
    }

    private final Registry<DamageType> damageTypes;
    private final DamageSource sculkAcid;
    private final DamageSource sculkJawBite;

    public ModDamageSources(RegistryAccess registryAccess) {
        this.damageTypes = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
        this.sculkAcid = this.source(ModDamageTypes.SCULK_ACID);
        this.sculkJawBite = this.source(ModDamageTypes.SCULK_JAW_BITE);
    }

    private DamageSource source(ResourceKey<DamageType> damageType) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(damageType));
    }

    public DamageSource sculkAcid() {
        return this.sculkAcid;
    }

    public DamageSource sculkJawBite() {
        return this.sculkJawBite;
    }
}
