package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.HungerManager;
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
            players = context.getSource().getServer().getPlayerManager().getPlayerList();
            resetWorld(context);
        }
        players.stream().filter(p -> ((IServerPlayerEntity) p).fs_isSurvival()).forEach(player -> {
            resetPlayer(player);
            int lives = LivesData.randomizeLives((IEntityDataSaver) player);
            player.sendMessage(Text.translatable("session.finalstand.amount_of_lives", lives));
        });
        return 1;
    }

    private static void resetWorld(CommandContext<ServerCommandSource> context) {
        // Reset time of day
        context.getSource().getWorld().setTimeOfDay(0L);
    }

    /**
     * Resets health/hunger/inventory
     */
    private static void resetPlayer(ServerPlayerEntity player) {
        // Reset health
        player.setHealth(player.getMaxHealth());

        // Reset hunger
        HungerManager hungerManager = player.getHungerManager();
        hungerManager.setFoodLevel(20);
        hungerManager.setSaturationLevel(5.0f);

        // Clear inventory
        player.getInventory().clear();
    }
}
