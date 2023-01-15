package scubakay.laststand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import scubakay.laststand.LastStand;

public class EndSessionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(LastStand.COMMAND_ROOT)
                        .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                        .then(
                                CommandManager.literal("end")
                                        .executes(EndSessionCommand::run)
                        )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("Ending session: Not implemented"), true);
        return 1;
    }
}
