package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSoundEvents {
    public static void registerSoundEvents(IEventBus modEventBus) {
        SculkAndJaw.LOGGER.info("Registering SoundEvents for Mod " + SculkAndJaw.MOD_ID);
        MOD_SOUND_EVENTS.register(modEventBus);
    }

    public static final DeferredRegister<SoundEvent> MOD_SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, SculkAndJaw.MOD_ID);

    public static final Holder<SoundEvent> SCULK_JAW_BITE = MOD_SOUND_EVENTS.register("block.sculk_jaw.sculk_jaw_bite", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> SCULK_JAW_BURP = MOD_SOUND_EVENTS.register("block.sculk_jaw.sculk_jaw_burp", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> SCULK_ACID_BUBBLE_EMERGE = MOD_SOUND_EVENTS.register("block.sculk_acid_fluid.sculk_acid_bubble_emerge", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> SCULLK_ACID_FLOW = MOD_SOUND_EVENTS.register("block.sculk_acid_fluid.sculk_acid_flow", SoundEvent::createVariableRangeEvent);
    public static final Holder<SoundEvent> SCULK_ACID = MOD_SOUND_EVENTS.register("effect.acid_etching.decompose", SoundEvent::createVariableRangeEvent);
}
