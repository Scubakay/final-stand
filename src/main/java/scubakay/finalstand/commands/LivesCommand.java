package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.data.LivesData;
import scubakay.finalstand.util.IEntityDataSaver;

import java.util.Collection;

public class LivesCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(CommandManager
            .literal(FinalStand.COMMAND_ROOT)
            .requires(source -> source.hasPermissionLevel(4))
            .then(CommandManager
                .literal("lives")
                .then(CommandManager
                    .literal("add")
                    .then(CommandManager
                        .argument("players", EntityArgumentType.players())
                        .executes(ctx -> add(ctx, EntityArgumentType.getPlayers(ctx, "players")))
                    )
                )
                .then(CommandManager
                    .literal("remove")
                    .then(CommandManager
                        .argument("players", EntityArgumentType.players())
                        .executes(ctx -> remove(ctx, EntityArgumentType.getPlayers(ctx, "players")))
                    )
                )
                .then(CommandManager
                    .literal("set")
                    .then(CommandManager
                        .argument("players", EntityArgumentType.players())
                        .then(CommandManager
                            .argument("amount", IntegerArgumentType.integer())
                            .executes(ctx -> set(ctx, EntityArgumentType.getPlayers(ctx, "players"), IntegerArgumentType.getInteger(ctx, "amount")))
                        )
                    )
                )
            )
        );
    }

    public static int add(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
        players.forEach(p -> {
            int lives = LivesData.addLives((IEntityDataSaver) p, 1);
            context.getSource().sendFeedback(Text.translatable("lives.finalstand.player_has_x_lives", p.getDisplayName(), lives), true);
        });
        return 1;
    }

    public static int remove(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
        players.forEach(p -> {
            int lives = LivesData.removeLives((IEntityDataSaver) p, 1);
            context.getSource().sendFeedback(Text.translatable("lives.finalstand.player_has_x_lives", p.getDisplayName(), lives), true);
        });
        return 1;
    }

    public static int set(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players, int amount) {
        players.forEach(p -> {
            int lives = LivesData.setLives((IEntityDataSaver) p, amount);
            context.getSource().sendFeedback(Text.translatable("lives.finalstand.player_has_x_lives", p.getDisplayName(), lives), true);
        });
        return 1;
    }
}
