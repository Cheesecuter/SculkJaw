package ycpk.sculkjaw.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import ycpk.sculkjaw.Sculkjaw;

public class ModSoundEvents {

    public static final SoundEvent SCULK_JAW_BITE = register("block.sculk_jaw.sculk_jaw_bite");
    public static final SoundEvent SCULK_JAW_ACID = register("block.sculk_jaw.sculk_jaw_acid");

    public static void registerSoundEvents() {
        Sculkjaw.LOGGER.info("Registering SoundEvents for Mod " + Sculkjaw.MOD_ID);
    }

    private static SoundEvent register(String id){
        ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(Sculkjaw.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }
}
