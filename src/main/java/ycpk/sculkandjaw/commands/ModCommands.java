package ycpk.sculkandjaw.commands;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.server.commands.SpreadSculkCommand;

public class ModCommands {
    public static void registerModCommands() {
        SculkAndJaw.LOGGER.info("Registering Commands for Mod " + SculkAndJaw.MOD_ID);
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        SpreadSculkCommand.register(event.getDispatcher());
    }
}
