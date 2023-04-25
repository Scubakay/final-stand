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
    private static int lastTick = 0;
    private final static int TICKS_TO_MINUTES = 20*60;

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
        serverState.sessionTicksLeft = currentTick + ModConfig.getSessionTime() * TICKS_TO_MINUTES;
        serverState.inSession = true;
        serverState.markDirty();
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
        SessionState serverState = SessionState.getServerState(server);

        if (serverState.inSession) {
            int currentTick = server.getTicks();
            int ticksPassed = currentTick - lastTick;
            lastTick = currentTick;

            handleSessionTime(server, ticksPassed);
            handleChestPlacement(server, currentTick);
            handleHunterSelection(server, currentTick);

            serverState.markDirty();
        }
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

    private static void handleSessionTime(MinecraftServer server, int ticksPassed) {
        SessionState serverState = SessionState.getServerState(server);
        // End session after x minutes
        serverState.sessionTicksLeft = serverState.sessionTicksLeft - ticksPassed;
        if (!serverState.sessionEndAnnounced && serverState.inSession && serverState.sessionTicksLeft < TICKS_TO_MINUTES) {
            serverState.sessionEndAnnounced = true;
            server.getPlayerManager().broadcast(Text.translatable("session.finalstand.session_ending_in_one_minute").formatted(Formatting.GREEN), false);
        }
        if (serverState.inSession && serverState.sessionTicksLeft < 0) {
            SessionHandler.EndSession(server);
        }
        syncSessionTime(server);
    }

    private static void syncSessionTime(MinecraftServer server) {
        SessionState serverState = SessionState.getServerState(server);
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(serverState.sessionTicksLeft);
        server.getPlayerManager().getPlayerList().forEach(p -> ServerPlayNetworking.send(p, ModMessages.SESSION_TIME_SYNC, buffer));
    }

    private static void reset(MinecraftServer server) {
        SessionState serverState = SessionState.getServerState(server);
        serverState.inSession = false;
        serverState.hunterTick = -1;
        serverState.chestTicks = new int[]{};
        serverState.sessionTicksLeft = -1;
        serverState.huntersAnnounced = false;
        serverState.announcedChests = new int[]{};
        serverState.sessionEndAnnounced = false;
        serverState.markDirty();
    }
}
