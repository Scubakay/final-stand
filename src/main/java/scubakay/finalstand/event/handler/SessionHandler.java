package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import scubakay.finalstand.config.ModConfig;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.networking.ModMessages;
import scubakay.finalstand.util.ChestPlacer;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to automate sessions based on ticks
 */
public class SessionHandler implements ServerTickEvents.StartTick {
    private static int hunterTick = -1;
    private static boolean huntersAnnounced = false;
    private final static List<Integer> chestTicks = new ArrayList<>();
    private final static List<Integer> announcedChests = new ArrayList<>();
    private static int sessionTick = -1;
    private static boolean sessionEndAnnounced = false;

    private final static int TICKS_TO_MINUTES = 20*60;

    public static int getSessionTicksLeft(MinecraftServer server) {
        return sessionTick - server.getTicks();
    }

    /**
     * Starts a session and timers for selecting hunters/placing chests and session end
     */
    public static void StartSession(MinecraftServer server) {
        HuntersState.reset(server);
        int currentTick = server.getTicks();
        hunterTick = currentTick + ModConfig.getSessionHunterSelectionTime() * TICKS_TO_MINUTES;
        for(int i : ModConfig.getSessionTreasureChestTimes()) {
            chestTicks.add(currentTick + i * TICKS_TO_MINUTES);
        }
        sessionTick = currentTick + ModConfig.getSessionTime() * TICKS_TO_MINUTES;
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
        // Handle announcements
        for(int i = chestTicks.size() - 1; i >= 0; i--) {
            if (currentTick > chestTicks.get(i) - TICKS_TO_MINUTES) {
                announcedChests.add(chestTicks.get(i));
                chestTicks.remove(i);
                server.getPlayerManager().broadcast(Text.translatable("session.finalstand.chest_placed_in_one_minute").formatted(Formatting.BLUE), false);
            }
        }

        // Handle placements
        for (int i = announcedChests.size() - 1; i >= 0; i--) {
            if (currentTick > announcedChests.get(i)) {
                ChestPlacer.placeChestRandomly(server.getOverworld());
                announcedChests.remove(i);
            }
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
        chestTicks.clear();
        sessionTick = -1;
    }

    private static void resetAnnouncements() {
        huntersAnnounced = false;
        announcedChests.clear();
        sessionEndAnnounced = false;
    }
}
