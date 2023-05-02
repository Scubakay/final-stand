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
import scubakay.finalstand.data.HuntersState;

import java.util.Collection;

public class CompleteBountyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(FinalStand.COMMAND_ROOT)
                .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                .then(CommandManager.literal("completeBounty")
                    .then(CommandManager.argument("players", EntityArgumentType.players())
                        .executes(ctx -> run(ctx, EntityArgumentType.getPlayers(ctx, "players")))
                    )
                )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context,  Collection<ServerPlayerEntity> players) {
        players.forEach(p -> {
            HuntersState.completeBounty(p);
            context.getSource().sendFeedback(Text.translatable("session.finalstand.completed_players_bounty", p.getDisplayName()), true);
            FinalStand.LOGGER.info(String.format("Set bounty complete for %s", p.getDisplayName()));
        });
        return 1;
    }
}
