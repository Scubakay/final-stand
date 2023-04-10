package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.data.HuntersState;

public class EndSessionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(FinalStand.COMMAND_ROOT)
                        .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                        .then(
                                CommandManager.literal("end")
                                        .executes(EndSessionCommand::run)
                        )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        HuntersState.punishHunters(context.getSource().getWorld().getPlayers());
        context.getSource().getServer().getPlayerManager().broadcast(Text.translatable("session.finalstand.session_ended"), false);
        return 1;
    }
}
