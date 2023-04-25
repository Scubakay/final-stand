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

import java.util.*;

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
        reset(server);
        serverState.hunterTicksLeft = ModConfig.getSessionHunterSelectionTime() * TICKS_TO_MINUTES;
        serverState.shouldSelectHunters = true;
        serverState.chestTicksLeft = Arrays.stream(ModConfig.getSessionTreasureChestTimes()).map(ct -> ct * TICKS_TO_MINUTES).toArray();
        serverState.sessionTicksLeft = ModConfig.getSessionTime() * TICKS_TO_MINUTES;
        lastTick = server.getTicks();
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

        int currentTick = server.getTicks();
        int ticksPassed = currentTick - lastTick;
        lastTick = currentTick;

        handleSessionTime(server, ticksPassed);
        handleChestPlacement(server, ticksPassed);
        handleHunterSelection(server, ticksPassed);

        serverState.markDirty();
    }

    private static void handleHunterSelection(MinecraftServer server, int ticksPassed) {
        SessionState serverState = SessionState.getServerState(server);
        serverState.hunterTicksLeft = serverState.hunterTicksLeft - ticksPassed;

        // Select hunters after x minutes
        if (serverState.shouldSelectHunters && !serverState.huntersAnnounced && serverState.hunterTicksLeft < TICKS_TO_MINUTES) {
            serverState.huntersAnnounced = true;
            server.getPlayerManager().broadcast(Text.translatable("session.finalstand.hunter_chosen_in_one_minute").formatted(Formatting.DARK_RED), false);
        }
        if (serverState.shouldSelectHunters && serverState.hunterTicksLeft < 0) {
            serverState.shouldSelectHunters = false;
            HuntersState.selectHunters(server);
        }
    }

    private static void handleChestPlacement(MinecraftServer server, int ticksPassed) {
        SessionState serverState = SessionState.getServerState(server);
        List<Integer> newChestTicks = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(serverState.chestTicksLeft)));
        List<Integer> newAnnouncedChests = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(serverState.announcedChests)));
        // Handle announcements
        for(int i = newChestTicks.size() - 1; i >= 0; i--) {
            newChestTicks.set(i, newChestTicks.get(i) - ticksPassed);
            if (newChestTicks.get(i) < TICKS_TO_MINUTES) {
                newAnnouncedChests.add(newChestTicks.get(i));
                newChestTicks.remove(i);
                server.getPlayerManager().broadcast(Text.translatable("session.finalstand.chest_placed_in_one_minute").formatted(Formatting.BLUE), false);
            }
        }

        // Handle placements
        for (int i = newAnnouncedChests.size() - 1; i >= 0; i--) {
            newAnnouncedChests.set(i, newAnnouncedChests.get(i) - ticksPassed);
            if (newAnnouncedChests.get(i) < 0) {
                ChestPlacer.placeChestRandomly(server.getOverworld());
                newAnnouncedChests.remove(i);
            }
        }
        serverState.chestTicksLeft = newChestTicks.stream().mapToInt(Integer::intValue).toArray();
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
        serverState.sessionTicksLeft = -1;
        serverState.sessionEndAnnounced = false;
        serverState.hunterTicksLeft = -1;
        serverState.shouldSelectHunters = false;
        serverState.huntersAnnounced = false;
        serverState.chestTicksLeft = new int[]{};
        serverState.announcedChests = new int[]{};
        serverState.markDirty();
    }
}
