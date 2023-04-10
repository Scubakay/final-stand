package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.data.HuntersState;

public class SelectHuntersCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(FinalStand.COMMAND_ROOT)
                        .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                        .then(
                                CommandManager.literal("selectHunters")
                                        .executes(SelectHuntersCommand::run)
                        )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        HuntersState.reset(context.getSource().getServer());
        HuntersState.selectHunters(context.getSource().getServer());
        return 1;
    }
}
