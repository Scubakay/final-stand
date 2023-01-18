package scubakay.laststand.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import scubakay.laststand.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public static void removeIfPlayerWasHunter(ServerPlayerEntity player) {
        Optional<HunterTarget> hunterTarget = hunters.stream().filter(ht -> ht.hunter.equals(player)).findFirst();
        hunterTarget.ifPresent(HuntersState::removeHunterTrackingDevice);
    }

    public static void removeIfPlayerWasTarget(ServerPlayerEntity player) {
        List<HunterTarget> hts = hunters.stream().filter(ht -> ht.target.equals(player)).toList();
        hts.forEach(HuntersState::removeHunterTrackingDevice);
    }

    public static void reset() {
        hunters = new ArrayList<>();
    }

    private static void removeHunterTrackingDevice(HunterTarget ht) {
        ht.target.sendMessage(Text.translatable("item.laststand.no-longer-being-hunted"));
        ht.hunter.sendMessage(Text.translatable("item.laststand.bounty-completed"));
        ht.hunter.getInventory().remove(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE), 1, ht.hunter.getInventory());
        hunters.remove(ht);
    }
}
