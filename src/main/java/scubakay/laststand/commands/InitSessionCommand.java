package scubakay.laststand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import scubakay.laststand.LastStand;
import scubakay.laststand.networking.ModMessages;

public class InitSessionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(LastStand.COMMAND_ROOT)
                        .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                        .then(
                                CommandManager.literal("init")
                                        .executes(InitSessionCommand::run)
                        )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        ClientPlayNetworking.send(ModMessages.RANDOMIZE_LIVES, PacketByteBufs.create());
        return 1;
    }
}
