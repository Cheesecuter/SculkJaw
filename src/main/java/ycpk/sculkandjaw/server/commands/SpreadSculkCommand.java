package ycpk.sculkandjaw.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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
    private static final SimpleCommandExceptionType ERROR_FAILD = new SimpleCommandExceptionType(Component.translatable("command.spreadsculk.failed"));
    public static final int PERMISSION_LEVEL_2 = 2;

    public SpreadSculkCommand() {

    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("spreadsculk").requires((commandSourceStack) -> {
                    return commandSourceStack.hasPermission(PERMISSION_LEVEL_2);
                })).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(
                        (commandContext -> {
                            return spreadSculk(
                                    (CommandSourceStack) commandContext.getSource(),
                                    BlockPosArgument.getBlockPos(commandContext, "pos"),
                                    1,
                                    false
                            );
                        })
                ).then(Commands.argument("xp", IntegerArgumentType.integer(1)).executes(
                        (commandContext -> {
                            return spreadSculk(
                                    (CommandSourceStack) commandContext.getSource(),
                                    BlockPosArgument.getBlockPos(commandContext, "pos"),
                                    IntegerArgumentType.getInteger(commandContext, "xp"),
                                    false
                            );
                        })
                ).then(Commands.argument("shriekers_can_summon", BoolArgumentType.bool()).executes(
                        (commandContext -> {
                            return spreadSculk(
                                    (CommandSourceStack) commandContext.getSource(),
                                    BlockPosArgument.getBlockPos(commandContext, "pos"),
                                    IntegerArgumentType.getInteger(commandContext, "xp"),
                                    BoolArgumentType.getBool(commandContext, "shriekers_can_summon")
                            );
                        })
                ))))
        );
    }

    private static int spreadSculk(CommandSourceStack commandSourceStack, BlockPos pos, int xp, boolean shriekersCanSummon) throws CommandSyntaxException {
        ServerLevel serverLevel = commandSourceStack.getLevel();
        if (serverLevel.isDebug()) {
            throw ERROR_FAILD.create();
        }
        else {
            SculkSpreader sculkSpreader = SculkSpreader.createWorldGenSpreader();
            if (shriekersCanSummon) {
                sculkSpreader = SculkSpreader.createWorldGenSpreader();
            }
            int spreadRounds = 1;
            for(int j = 0; j < spreadRounds; ++j) {
                for(int k = 0; k < 6; ++k) {
                    sculkSpreader.addCursors(pos, xp);
                }
                for(int l = 0; l < xp; ++l) {
                    sculkSpreader.updateCursors(serverLevel, pos, serverLevel.getRandom(), true);
                }
                sculkSpreader.clear();
            }
        }
        logSpreadSculk(commandSourceStack, pos, xp);
        return 1;
    }

    private static void logSpreadSculk(CommandSourceStack commandSourceStack, BlockPos pos, int xp) {
        commandSourceStack.sendSuccess(() -> {
            return Component.translatable("commands.spreadsculk.success", new Object[]{pos.getX(), pos.getY(), pos.getZ(), xp});
        }, true);
    }
}
