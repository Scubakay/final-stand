package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.ArrayUtils;
import scubakay.finalstand.config.ModConfig;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.data.SessionState;
import scubakay.finalstand.networking.ModMessages;
import scubakay.finalstand.util.ChestPlacer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used to automate sessions based on ticks
 */
public class SessionHandler implements ServerTickEvents.StartTick {
    private final static int TICKS_TO_MINUTES = 20*60;

    public static int getSessionTicksLeft(MinecraftServer server) {
        SessionState serverState = SessionState.getServerState(server);
        return serverState.sessionTick - server.getTicks();
    }

    /**
     * Starts a session and timers for selecting hunters/placing chests and session end
     */
    public static void StartSession(MinecraftServer server) {
        SessionState serverState = SessionState.getServerState(server);
        HuntersState.reset(server);
        int currentTick = server.getTicks();
        serverState.hunterTick = currentTick + ModConfig.getSessionHunterSelectionTime() * TICKS_TO_MINUTES;
        List<Integer> chestTicks = new ArrayList<>();
        for(int i : ModConfig.getSessionTreasureChestTimes()) {
            chestTicks.add(currentTick + i * TICKS_TO_MINUTES);
        }
        serverState.chestTicks = chestTicks.stream().mapToInt(Integer::intValue).toArray();
        serverState.sessionTick = currentTick + ModConfig.getSessionTime() * TICKS_TO_MINUTES;
    }

    /**
     * Reset the session and timers for selecting hunters/placing chests and session end
     */
    public static void ResetSession(MinecraftServer server) {
        HuntersState.reset(server);
        reset(server);
    }

    public static void EndSession(MinecraftServer server) {
        HuntersState.punishHunters(server.getPlayerManager().getPlayerList());
        reset(server);
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
        SessionState serverState = SessionState.getServerState(server);
        // Select hunters after x minutes
        if (!serverState.huntersAnnounced && serverState.hunterTick != -1 && currentTick > serverState.hunterTick - TICKS_TO_MINUTES) {
            serverState.huntersAnnounced = true;
            server.getPlayerManager().broadcast(Text.translatable("session.finalstand.hunter_chosen_in_one_minute").formatted(Formatting.DARK_RED), false);
        }
        if (serverState.hunterTick != -1 && currentTick > serverState.hunterTick) {
            HuntersState.selectHunters(server);
            serverState.hunterTick = -1;
        }
    }

    private static void handleChestPlacement(MinecraftServer server, int currentTick) {
        SessionState serverState = SessionState.getServerState(server);
        List<Integer> newChestTicks = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(serverState.chestTicks)));
        List<Integer> newAnnouncedChests = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(serverState.announcedChests)));
        // Handle announcements
        for(int i = newChestTicks.size() - 1; i >= 0; i--) {
            if (currentTick > newChestTicks.get(i) - TICKS_TO_MINUTES) {
                newAnnouncedChests.add(newChestTicks.get(i));
                newChestTicks.remove(i);
                server.getPlayerManager().broadcast(Text.translatable("session.finalstand.chest_placed_in_one_minute").formatted(Formatting.BLUE), false);
            }
        }

        // Handle placements
        for (int i = newAnnouncedChests.size() - 1; i >= 0; i--) {
            if (currentTick > newAnnouncedChests.get(i)) {
                ChestPlacer.placeChestRandomly(server.getOverworld());
                newAnnouncedChests.remove(i);
            }
        }
        serverState.chestTicks = newChestTicks.stream().mapToInt(Integer::intValue).toArray();
        serverState.announcedChests = newAnnouncedChests.stream().mapToInt(Integer::intValue).toArray();
    }

    private static void handleSessionTime(MinecraftServer server, int currentTick) {
        SessionState serverState = SessionState.getServerState(server);
        // End session after x minutes
        if (!serverState.sessionEndAnnounced && serverState.sessionTick != -1 && currentTick > serverState.sessionTick - TICKS_TO_MINUTES) {
            serverState.sessionEndAnnounced = true;
            server.getPlayerManager().broadcast(Text.translatable("session.finalstand.session_ending_in_one_minute").formatted(Formatting.GREEN), false);
        }
        if (serverState.sessionTick != -1 && currentTick > serverState.sessionTick) {
            SessionHandler.EndSession(server);
        }
        syncSessionTime(server);
    }

    private static void syncSessionTime(MinecraftServer server) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(getSessionTicksLeft(server));
        server.getPlayerManager().getPlayerList().forEach(p -> ServerPlayNetworking.send(p, ModMessages.SESSION_TIME_SYNC, buffer));

    }

    private static void reset(MinecraftServer server) {
        SessionState serverState = SessionState.getServerState(server);
        serverState.hunterTick = -1;
        serverState.chestTicks = new int[]{};
        serverState.sessionTick = -1;
        serverState.huntersAnnounced = false;
        serverState.announcedChests = new int[]{};
        serverState.sessionEndAnnounced = false;
    }
}
