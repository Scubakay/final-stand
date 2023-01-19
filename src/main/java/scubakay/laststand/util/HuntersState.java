package scubakay.laststand.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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

    public static void addHunter(HunterTarget ht) {
        hunters.add(ht);
        addHunterTrackingDevice(ht);
    }

    public static void removeIfPlayerWasHunter(ServerPlayerEntity player) {
        Optional<HunterTarget> hunterTarget = hunters.stream().filter(ht -> ht.hunter.equals(player)).findFirst();
        hunterTarget.ifPresent(HuntersState::removeHunterTrackingDevice);
    }

    public static void removeIfPlayerWasTarget(ServerPlayerEntity player) {
        List<HunterTarget> hts = hunters.stream().filter(ht -> ht.target.equals(player)).toList();
        hts.forEach(HuntersState::removeHunterTrackingDevice);
    }

    public static void reset(List<ServerPlayerEntity> players) {
        players.forEach((ServerPlayerEntity player) -> {
            while(player.getInventory().containsAny(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE))) {
                player.getInventory().remove(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE), 1, player.getInventory());
            }
        });
        hunters = new ArrayList<>();
    }

    public static void punishHunters() {
        hunters.forEach(ht -> {
            ht.hunter.getInventory().remove(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE), 1, ht.hunter.getInventory());
            LivesData.removeLives((IEntityDataSaver) ht.hunter, 1);
            ht.hunter.sendMessage(Text.translatable("item.laststand.bounty-failed"));
        });
        hunters = new ArrayList<>();
    }

    private static void addHunterTrackingDevice(HunterTarget ht) {
        // Create device
        ItemStack itemStack = new ItemStack(ModItems.HUNTER_TRACKING_DEVICE);
        itemStack.setCount(1);

        // Save target to device NBT
        NbtCompound nbtData = new NbtCompound();
        nbtData.putString("target", ht.target.getUuidAsString());
        itemStack.setNbt(nbtData);

        // Add device to hunter inventory
        ht.hunter.getInventory().insertStack(itemStack);
        ht.hunter.sendMessage(Text.translatable("item.laststand.you_are_hunter"));
    }

    private static void removeHunterTrackingDevice(HunterTarget ht) {
        ht.target.sendMessage(Text.translatable("item.laststand.no-longer-being-hunted"));
        ht.hunter.sendMessage(Text.translatable("item.laststand.bounty-completed"));
        ht.hunter.getInventory().remove(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE), 1, ht.hunter.getInventory());
        hunters.remove(ht);
    }
}
