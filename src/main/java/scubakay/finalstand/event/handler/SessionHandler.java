package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.networking.ModMessages;
import scubakay.finalstand.util.ChestPlacer;
import scubakay.finalstand.util.ModGameruleRegister;

/**
 * Used to automate sessions based on ticks
 */
public class SessionHandler implements ServerTickEvents.StartTick {
    private static int hunterTick = -1;
    private static boolean huntersAnnounced = false;
    private static int chestTick = -1;
    private static boolean chestAnnounced = false;
    private static int sessionTick = -1;
    private static boolean sessionEndAnnounced = false;

    private final static int TICKS_TO_MINUTES = 20*60;

    public static int getHunterTicksLeft(MinecraftServer server) {
        return hunterTick - server.getTicks();
    }

    public static int getChestTicksLeft(MinecraftServer server) {
        return chestTick - server.getTicks();
    }

    public static int getSessionTicksLeft(MinecraftServer server) {
        return sessionTick - server.getTicks();
    }

    /**
     * Starts a session and timers for selecting hunters/placing chests and session end
     */
    public static void StartSession(MinecraftServer server) {
        HuntersState.reset(server);
        int currentTick = server.getTicks();
        hunterTick = currentTick + server.getGameRules().getInt(ModGameruleRegister.SESSION_HUNTER_SELECTION_TIME) * TICKS_TO_MINUTES;
        chestTick = currentTick + server.getGameRules().getInt(ModGameruleRegister.SESSION_TREASURE_CHEST_TIME) * TICKS_TO_MINUTES;
        sessionTick = currentTick + server.getGameRules().getInt(ModGameruleRegister.SESSION_TIME) * TICKS_TO_MINUTES;
        resetAnnouncements();
    }

    /**
     * Reset the session and timers for selecting hunters/placing chests and session end
     */
    public static void ResetSession(MinecraftServer server) {
        HuntersState.reset(server);
        resetTicks();
        resetAnnouncements();
    }

    public static void EndSession(MinecraftServer server) {
        HuntersState.punishHunters(server.getPlayerManager().getPlayerList());
        resetTicks();
        resetAnnouncements();
        server.getPlayerManager().broadcast(Text.translatable("session.finalstand.session_ended"), false);
    }

    /**
     * Execute selecting hunters/placing chests and ending session after given amount of ticks
     */
    @Override
    public void onStartTick(MinecraftServer server) {
        int currentTick = server.getTicks();
        handleChestPlacement(server, currentTick);
        handleSessionTime(server, currentTick);
        handleHunterSelection(server, currentTick);
    }

    private static void handleHunterSelection(MinecraftServer server, int currentTick) {
        // Select hunters after x minutes
        if (!huntersAnnounced && hunterTick != -1 && currentTick > hunterTick - TICKS_TO_MINUTES) {
            huntersAnnounced = true;
            server.getPlayerManager().broadcast(Text.translatable("session.finalstand.hunter_chosen_in_one_minute").formatted(Formatting.DARK_RED), false);
        }
        if (hunterTick != -1 && currentTick > hunterTick) {
            HuntersState.selectHunters(server);
            hunterTick = -1;
        }
    }

    private static void handleChestPlacement(MinecraftServer server, int currentTick) {
        // Place chest after x minutes
        if (!chestAnnounced && chestTick != -1 && currentTick > chestTick - TICKS_TO_MINUTES) {
            chestAnnounced = true;
            server.getPlayerManager().broadcast(Text.translatable("session.finalstand.chest_placed_in_one_minute").formatted(Formatting.BLUE), false);
        }
        if (chestTick != -1 && currentTick > chestTick) {
            ChestPlacer.placeChestRandomly(server.getOverworld());
            chestTick = -1;
        }
    }

    private static void handleSessionTime(MinecraftServer server, int currentTick) {
        // End session after x minutes
        if (!sessionEndAnnounced && sessionTick != -1 && currentTick > sessionTick - TICKS_TO_MINUTES) {
            sessionEndAnnounced = true;
            server.getPlayerManager().broadcast(Text.translatable("session.finalstand.session_ending_in_one_minute").formatted(Formatting.GREEN), false);
        }
        if (sessionTick != -1 && currentTick > sessionTick) {
            SessionHandler.EndSession(server);
        }
        syncSessionTime(server);
    }

    private static void syncSessionTime(MinecraftServer server) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(getSessionTicksLeft(server));
        server.getPlayerManager().getPlayerList().forEach(p -> ServerPlayNetworking.send(p, ModMessages.SESSION_TIME_SYNC, buffer));

    }

    private static void resetTicks() {
        hunterTick = -1;
        chestTick = -1;
        sessionTick = -1;
    }

    private static void resetAnnouncements() {
        huntersAnnounced = false;
        chestAnnounced = false;
        sessionEndAnnounced = false;
    }
}
