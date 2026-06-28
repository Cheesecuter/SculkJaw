package com.ycpk.sculkandjaw.registry;

import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.server.commands.SpreadSculkCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommands {
    public static void registerModCommands() {
        SculkAndJaw.LOGGER.info("Registering Commands for Mod " + SculkAndJaw.MOD_ID);
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        SpreadSculkCommand.register(event.getDispatcher());
    }
}
