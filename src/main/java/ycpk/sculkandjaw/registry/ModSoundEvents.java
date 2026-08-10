package ycpk.sculkandjaw.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModSoundEvents {
    public static void registerSoundEvents(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering SoundEvents for Mod " + SculkAndJaw.MOD_ID);
        MOD_SOUND_EVENTS.register(modEventBus);
    }

    public static final DeferredRegister<SoundEvent> MOD_SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SculkAndJaw.MOD_ID);
    public static final RegistryObject<SoundEvent> SCULK_JAW_BITE = MOD_SOUND_EVENTS.register(
            "block.sculk_jaw.sculk_jaw_bite",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(
                            SculkAndJaw.MOD_ID,
                            "sculk_jaw_bite"
                    )
            )
    );
    public static final RegistryObject<SoundEvent> SCULK_JAW_BURP = MOD_SOUND_EVENTS.register(
            "block.sculk_jaw.sculk_jaw_burp",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(
                            SculkAndJaw.MOD_ID,
                            "sculk_jaw_burp"
                    )
            )
    );
    public static final RegistryObject<SoundEvent> SCULK_ACID_BUBBLE_EMERGE = MOD_SOUND_EVENTS.register(
            "block.sculk_acid_fluid.sculk_acid_bubble_emerge",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(
                            SculkAndJaw.MOD_ID,
                            "sculk_acid_bubble_emerge"
                    )
            )
    );
    public static final RegistryObject<SoundEvent> SCULLK_ACID_FLOW = MOD_SOUND_EVENTS.register(
            "block.sculk_acid_fluid.sculk_acid_flow",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(
                            SculkAndJaw.MOD_ID,
                            "sculk_acid_flow"
                    )
            )
    );
    public static final RegistryObject<SoundEvent> SCULK_ACID_DECOMPOSE = MOD_SOUND_EVENTS.register(
            "effect.acid_etching.decompose",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(
                            SculkAndJaw.MOD_ID,
                            "decompose"
                    )
            )
    );
}
