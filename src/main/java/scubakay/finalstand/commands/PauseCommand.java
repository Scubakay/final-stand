package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.event.handler.SessionHandler;

public class PauseCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(CommandManager
            .literal(FinalStand.COMMAND_ROOT)
            .requires(source -> source.hasPermissionLevel(4))
            .then(
                CommandManager
                    .literal("pause")
                    .executes(PauseCommand::pauseSession)
            )
            .then(
                CommandManager
                    .literal("resume")
                    .executes(PauseCommand::resumeSession)
            )
        );
    }

    public static int pauseSession(CommandContext<ServerCommandSource> context) {
        SessionHandler.PauseSession(context.getSource().getServer());
        context.getSource().getServer().getPlayerManager().broadcast(Text.translatable("session.finalstand.pausing"), false);
        return 1;
    }

    public static int resumeSession(CommandContext<ServerCommandSource> context) {
        SessionHandler.ResumeSession(context.getSource().getServer());
        context.getSource().getServer().getPlayerManager().broadcast(Text.translatable("session.finalstand.resuming"), false);
        return 1;
    }
}