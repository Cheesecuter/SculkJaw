package ycpk.sculkandjaw.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModSoundEvents {
    public static void registerSoundEvents() {
        SculkAndJaw.LOGGER.info("Registering SoundEvents for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final SoundEvent SCULK_ACID_BUBBLE_EMERGE = register("block.sculk_acid_fluid.bubble_emerge");
    public static final SoundEvent SCULLK_ACID_FLOW = register("block.sculk_acid_fluid.flow");
    public static final SoundEvent SCULK_JAW_BITE = register("block.sculk_jaw.bite");
    public static final SoundEvent SCULK_JAW_BURP = register("block.sculk_jaw.burp");
    public static final SoundEvent SCULK_TELEPORTER_BREAK = register("block.sculk_teleporter.break");
    public static final SoundEvent SCULK_TELEPORTER_PLACE = register("block.sculk_teleporter.place");
    public static final SoundEvent SCULK_ACID = register("effect.acid_etching.decompose");

    private static SoundEvent register(String id){
        Identifier identifier = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }
}
