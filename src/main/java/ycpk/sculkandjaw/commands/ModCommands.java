package ycpk.sculkandjaw.commands;

import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.server.commands.SpreadSculkCommand;

public class ModCommands {
    public static void registerModCommands() {
        SculkAndJaw.LOGGER.info("Registering Commands for Mod " + SculkAndJaw.MOD_ID);
        SpreadSculkCommand.register();
    }
}
