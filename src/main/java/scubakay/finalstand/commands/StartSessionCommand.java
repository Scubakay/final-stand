package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.event.handler.SessionHandler;

public class StartSessionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(FinalStand.COMMAND_ROOT)
                .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                .then(
                        CommandManager.literal("start")
                                .executes(StartSessionCommand::run)
                )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        SessionHandler.StartSession(context.getSource().getServer());
        context.getSource().sendFeedback(Text.translatable("session.finalstand.starting_session"), true);
        return 1;
    }
}
