package scubakay.laststand.util;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps list of current hunters.
 * TODO: Persist state
 * <a href="https://www.fabricmc.net/wiki/tutorial:persistent_states">Persistent states</a>
 */
public class HuntersState {
    private static List<HunterTarget> hunters = new ArrayList<>();

    public static void addHunter(HunterTarget hunterTarget) {
        hunters.add(hunterTarget);
    }

    public static boolean removeIfCompleted(ServerPlayerEntity player, ServerPlayerEntity target) {
        return hunters.removeIf(ht -> ht.hunter.equals(player) && ht.target.equals(target));
    }

    public static void reset() {
        hunters = new ArrayList<>();
    }
}
