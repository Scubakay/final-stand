package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.util.ChestPlacer;
import scubakay.finalstand.util.ModGameruleRegister;

/**
 * Used to automate sessions based on ticks
 */
public class SessionHandler implements ServerTickEvents.StartTick {
    private static int hunterTick = -1;
    private static int chestTick = -1;
    private static int sessionTick = -1;

    private final static int TICKS_TO_MINUTES = 20*60;

    /**
     * Starts a session and timers for selecting hunters/placing chests and session end
     */
    public static void StartSession(MinecraftServer server) {
        HuntersState.reset(server);
        int currentTick = server.getTicks();
        hunterTick = currentTick + server.getGameRules().getInt(ModGameruleRegister.SESSION_HUNTER_SELECTION_TIME) * TICKS_TO_MINUTES;
        chestTick = currentTick + server.getGameRules().getInt(ModGameruleRegister.SESSION_TREASURE_CHEST_TIME) * TICKS_TO_MINUTES;
        sessionTick = currentTick + server.getGameRules().getInt(ModGameruleRegister.SESSION_TIME) * TICKS_TO_MINUTES;
    }

    /**
     * Reset the session and timers for selecting hunters/placing chests and session end
     */
    public static void ResetSession(MinecraftServer server) {
        HuntersState.reset(server);
        hunterTick = -1;
        chestTick = -1;
        sessionTick = -1;
    }

    public static void EndSession(MinecraftServer server) {
        HuntersState.punishHunters(server.getPlayerManager().getPlayerList());
        hunterTick = -1;
        chestTick = -1;
        sessionTick = -1;
        server.getPlayerManager().broadcast(Text.translatable("session.finalstand.session_ended"), false);
    }

    /**
     * Execute selecting hunters/placing chests and ending session after given amount of ticks
     */
    @Override
    public void onStartTick(MinecraftServer server) {
        int currentTick = server.getTicks();
        // Select hunters after x minutes
        if (hunterTick != -1 && currentTick > hunterTick) {
            HuntersState.selectHunters(server);
            hunterTick = -1;
        }
        // Place chest after x minutes
        if (chestTick != -1 && currentTick > chestTick) {
            ChestPlacer.placeChestRandomly(server.getOverworld());
            chestTick = -1;
        }
        // End session after x minutes
        if (sessionTick != -1 && currentTick > sessionTick) {
            SessionHandler.EndSession(server);
        }
    }
}
