package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.border.WorldBorder;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.data.LivesData;
import scubakay.finalstand.data.TeamState;
import scubakay.finalstand.event.handler.SessionHandler;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.util.IServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collection;

public class SessionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(CommandManager
            .literal(FinalStand.COMMAND_ROOT)
            .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
            .then(CommandManager
                .literal("session")
                .then(CommandManager
                    .literal("init")
                    .then(CommandManager
                        .argument("players", EntityArgumentType.players())
                        .executes(ctx -> init(ctx, EntityArgumentType.getPlayers(ctx, "players")))
                    )
                    .executes(ctx -> init(ctx, new ArrayList<>()))
                )
                .then(CommandManager
                    .literal("start")
                    .executes(SessionCommand::start)
                )
                .then(CommandManager
                    .literal("pause")
                    .executes(SessionCommand::pause)
                )
                .then(CommandManager
                    .literal("resume")
                    .executes(SessionCommand::resume)
                )
                .then(CommandManager
                    .literal("reset")
                    .executes(SessionCommand::reset)
                )
                .then(CommandManager
                    .literal("end")
                    .executes(SessionCommand::end)
                )
            )
        );
    }

    public static int init(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
        TeamState.createTeams(context.getSource().getServer().getScoreboard());
        resetWorld(context);
        initPlayers(context, players);
        return 1;
    }

    public static int start(CommandContext<ServerCommandSource> context) {
        SessionHandler.StartSession(context.getSource().getServer());
        context.getSource().sendFeedback(() -> Text.translatable("session.finalstand.starting_session"), true);
        return 1;
    }

    public static int pause(CommandContext<ServerCommandSource> context) {
        SessionHandler.PauseSession(context.getSource().getServer());
        context.getSource().getServer().getPlayerManager().broadcast(Text.translatable("session.finalstand.pausing"), false);
        return 1;
    }

    public static int resume(CommandContext<ServerCommandSource> context) {
        SessionHandler.ResumeSession(context.getSource().getServer());
        context.getSource().getServer().getPlayerManager().broadcast(Text.translatable("session.finalstand.resuming"), false);
        return 1;
    }

    public static int reset(CommandContext<ServerCommandSource> context) {
        SessionHandler.ResetSession(context.getSource().getServer());
        context.getSource().sendFeedback(() -> Text.translatable("session.finalstand.resetting_session"), true);
        return 1;
    }

    public static int end(CommandContext<ServerCommandSource> context) {
        SessionHandler.EndSession(context.getSource().getServer());
        return 1;
    }

    private static void initPlayers(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
        if (players.isEmpty()) {
            // If no players are provided in the argument, get all players in the world.
            players = context.getSource().getServer().getPlayerManager().getPlayerList();
        }

        players.stream().filter(p -> ((IServerPlayerEntity) p).fs_isSurvival()).forEach(player -> {
            resetPlayer(player);
            int lives = LivesData.randomizeLives((IEntityDataSaver) player);
            player.sendMessage(Text.translatable("session.finalstand.amount_of_lives", lives));
        });
    }

    private static void resetWorld(CommandContext<ServerCommandSource> context) {
        // Reset time of day
        context.getSource().getWorld().setTimeOfDay(0L);
        killHostiles(context.getSource().getServer().getOverworld());
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

    private static void killHostiles(ServerWorld world) {
        // Kill off all hostile entities
        WorldBorder border = world.getWorldBorder();
        Box box = new Box(
                border.getBoundWest(), -64, border.getBoundNorth(),
                border.getBoundEast(), 320, border.getBoundSouth()
        );
        try {
            for (HostileEntity hostileEntity : world.getEntitiesByClass(HostileEntity.class, box, entity -> true)) {
                hostileEntity.remove(net.minecraft.entity.Entity.RemovalReason.KILLED);
            }
            System.out.println("Hostile entities removed successfully.");
        } catch (Exception e) {
            System.out.println("Error during hostile entity removal: " + e.getMessage());
        }
    }
}
