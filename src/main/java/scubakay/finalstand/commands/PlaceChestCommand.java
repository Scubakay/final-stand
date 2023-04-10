package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.util.ChestPlacer;

public class PlaceChestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(FinalStand.COMMAND_ROOT)
                        .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                        .then(
                                CommandManager.literal("placeChest")
                                        .executes(PlaceChestCommand::run)
                        )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        ChestPlacer.placeChestRandomly(context.getSource().getWorld());
        return 1;
    }
}
