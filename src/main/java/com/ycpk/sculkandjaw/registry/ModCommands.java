package com.ycpk.sculkandjaw.registry;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ycpk.sculkandjaw.SculkAndJaw;
import com.ycpk.sculkandjaw.server.commands.SpreadSculkCommand;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommands {
    public static void registerModCommands() {
        SculkAndJaw.LOGGER.info("Registering Commands for Mod " + SculkAndJaw.MOD_ID);
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        /*event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("ycpk")
                        .then(SpreadSculkCommand.register())
        );*/
        SpreadSculkCommand.register(event.getDispatcher());
    }
}
