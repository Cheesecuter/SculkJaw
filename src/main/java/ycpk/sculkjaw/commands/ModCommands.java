package ycpk.sculkjaw.commands;

import ycpk.sculkjaw.Sculkjaw;
import ycpk.sculkjaw.server.commands.SpreadSculkCommand;

public class ModCommands {
    public static void registerModCommands() {
        Sculkjaw.LOGGER.info("Registering Commands for Mod " + Sculkjaw.MOD_ID);
        SpreadSculkCommand.register();
    }
}
