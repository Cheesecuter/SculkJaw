package com.ycpk.sculkandjaw.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SculkSpreader;

public class SpreadSculkCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(Component.translatable("command.spreadsculk.failed"));
    public static final int PERMISSION_LEVEL_2 = 2;
    public SpreadSculkCommand() {

    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spreadsculk").requires((commandSourceStack) -> {
            return commandSourceStack.hasPermission(PERMISSION_LEVEL_2);
        }).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((commandContext -> {
            return spreadSculk((CommandSourceStack) commandContext.getSource(), BlockPosArgument.getBlockPos(commandContext, "pos"), 1);
        })).then(Commands.argument("experiences", IntegerArgumentType.integer(1)).executes((commandContext -> {
            return spreadSculk((CommandSourceStack) commandContext.getSource(), BlockPosArgument.getBlockPos(commandContext, "pos"), IntegerArgumentType.getInteger(commandContext, "experiences"));
        })))));
    }

    private static int spreadSculk(CommandSourceStack commandSourceStack, BlockPos blockPos, int experiences) throws CommandSyntaxException {
        ServerLevel serverLevel = commandSourceStack.getLevel();
        if (serverLevel.isDebug()) {
            throw ERROR_FAILED.create();
        }
        else {
            SculkSpreader sculkSpreader = SculkSpreader.createWorldGenSpreader();
            int spreadRounds = 1;
            for(int j = 0; j < spreadRounds; ++j) {
                for(int k = 0; k < 6; ++k) {
                    sculkSpreader.addCursors(blockPos, experiences);
                }
                for(int l = 0; l < experiences; ++l) {
                    sculkSpreader.updateCursors(serverLevel, blockPos, serverLevel.getRandom(), true);
                }
                sculkSpreader.clear();
            }
        }
        logSpreadSculk(commandSourceStack, blockPos, experiences);
        return 1;
    }

    private static void logSpreadSculk(CommandSourceStack commandSourceStack, BlockPos blockPos, int experiences) {
        commandSourceStack.sendSuccess(() -> {
            return Component.translatable("commands.spreadsculk.success", new Object[]{blockPos.getX(), blockPos.getY(), blockPos.getZ(), experiences});
        }, true);
    }
}
