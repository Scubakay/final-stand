package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.util.IServerPlayerEntity;
import scubakay.finalstand.data.LivesData;

public class InitSessionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(FinalStand.COMMAND_ROOT)
                        .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                        .then(
                                CommandManager.literal("init")
                                        .executes(InitSessionCommand::run)
                        )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        context.getSource().getWorld().getPlayers(p -> ((IServerPlayerEntity) p).isSurvival()).forEach(player -> {
            int lives = LivesData.randomizeLives((IEntityDataSaver) player);
            player.sendMessage(Text.translatable("item.finalstand.amount_of_lives", lives));
        });
        return 1;
    }
}
