package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.util.ChestPlacer;
import scubakay.finalstand.util.ModGameruleRegister;

public class SessionHandler implements ServerTickEvents.StartTick {
    private static int hunterTick = -1;
    private static int chestTick = -1;

    public static void StartSession(MinecraftServer server) {
        HuntersState.reset(server);
        int currentTick = server.getTicks();
        hunterTick = currentTick + server.getGameRules().getInt(ModGameruleRegister.SESSION_HUNTER_SELECTION_TIME) * 20 * 60;
        chestTick = currentTick + server.getGameRules().getInt(ModGameruleRegister.SESSION_TREASURE_CHEST_TIME) * 20 * 60;
    }

    public static void ResetSession(MinecraftServer server) {
        HuntersState.reset(server);
        hunterTick = -1;
        chestTick = -1;
    }

    @Override
    public void onStartTick(MinecraftServer server) {
        int currentTick = server.getTicks();
        if (hunterTick != -1 && currentTick > hunterTick) {
            HuntersState.selectHunters(server);
            hunterTick = -1;
        }
        if (chestTick != -1 && currentTick > chestTick) {
            ChestPlacer.placeChestRandomly(server.getOverworld());
            chestTick = -1;
        }
    }
}
