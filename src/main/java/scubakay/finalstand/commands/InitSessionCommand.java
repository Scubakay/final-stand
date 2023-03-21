package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.util.IServerPlayerEntity;
import scubakay.finalstand.data.LivesData;

import java.util.ArrayList;
import java.util.Collection;

public class InitSessionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(FinalStand.COMMAND_ROOT)
                        .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                        .then(CommandManager.literal("init")
                            .then(CommandManager.argument("players", EntityArgumentType.players())
                                    .executes(ctx -> run(ctx, EntityArgumentType.getPlayers(ctx, "players")))
                            )
                            .executes(ctx -> run(ctx, new ArrayList<>()))
                        )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
        if (players.size() == 0) {
            // If no players are provided in the argument, get all players in the world.
            players = context.getSource().getWorld().getPlayers();
        }
        players.stream().filter(p -> ((IServerPlayerEntity) p).isSurvival()).forEach(player -> {
            int lives = LivesData.randomizeLives((IEntityDataSaver) player);
            player.sendMessage(Text.translatable("item.finalstand.amount_of_lives", lives));
        });
        return 1;
    }
}
