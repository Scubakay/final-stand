package scubakay.finalstand.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import scubakay.finalstand.item.ModItems;
import scubakay.finalstand.item.custom.HunterTrackingDevice;
import scubakay.finalstand.util.IEntityDataSaver;

import java.util.List;

/**
 * Keeps track of current hunters using player NBT data
 */
public class HuntersState {
    private static final String TARGET_NBT_KEY = "finalstand.target";

    public static void addHunter(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        setTarget(hunter, target);
        addHunterTrackingDevice(hunter, target);
    }

    public static void removeIfPlayerWasHunter(ServerPlayerEntity player) {
        if(isHunter(player)){
            removeHunterTrackingDevice(player);
            player.sendMessage(Text.translatable("item.finalstand.bounty-completed").formatted(Formatting.GREEN));
        }
    }

    public static void removeIfPlayerWasTarget(ServerPlayerEntity player) {
        List<ServerPlayerEntity> players = player.getWorld().getPlayers();
        players.stream()
                .filter(p -> getTarget(p).equals(player.getUuidAsString()))
                .forEach(HuntersState::removeIfPlayerWasHunter);
        player.sendMessage(Text.translatable("item.finalstand.no-longer-being-hunted").formatted(Formatting.GREEN));
    }

    public static void reset(List<ServerPlayerEntity> players) {
        players.forEach(HuntersState::removeHunterTrackingDevice);
    }

    /**
     * Reward a bounty hunter with a life if they successfully kill their target
     */
    public static void rewardHunter(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        if(isPlayerTarget(hunter, target)) {
            removeHunterTrackingDevice(hunter);
            LivesData.addLives((IEntityDataSaver) hunter, 1);
        }
    }

    public static void punishHunters(List<ServerPlayerEntity> players) {
        players.stream()
                .filter(HuntersState::isHunter)
                .forEach(h -> {
                    removeHunterTrackingDevice(h);
                    LivesData.removeLives((IEntityDataSaver) h, 1);
                    h.sendMessage(Text.translatable("item.finalstand.bounty-failed").formatted(Formatting.RED));
                });
    }

    private static void addHunterTrackingDevice(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        // Create device
        ItemStack itemStack = new ItemStack(ModItems.HUNTER_TRACKING_DEVICE);
        itemStack.setCount(1);

        // Save target to device NBT
        NbtCompound nbtData = new NbtCompound();
        nbtData.putString(HunterTrackingDevice.SCAN_TARGET_KEY, target.getUuidAsString());
        itemStack.setNbt(nbtData);

        // Add device to hunter inventory
        if(hunter.getInventory().getEmptySlot() != -1) {
            hunter.getInventory().insertStack(itemStack);
        } else {
            hunter.dropItem(itemStack, false);
        }

        hunter.sendMessage(Text.translatable("item.finalstand.you_are_hunter").formatted(Formatting.DARK_RED));
    }

    private static void removeHunterTrackingDevice(ServerPlayerEntity hunter) {
        removeTarget(hunter);
        while(hunter.getInventory().containsAny(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE))) {
            hunter.getInventory().remove(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE), 1, hunter.getInventory());
        }
    }

    private static void setTarget(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        ((IEntityDataSaver) hunter).getPersistentData().putString(TARGET_NBT_KEY, target.getUuidAsString());
    }

    private static String getTarget(ServerPlayerEntity hunter) {
        return ((IEntityDataSaver) hunter).getPersistentData().getString(TARGET_NBT_KEY);
    }

    private static boolean isPlayerTarget(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        return ((IEntityDataSaver) hunter).getPersistentData().getString(TARGET_NBT_KEY).equals(target.getUuidAsString());
    }

    private static void removeTarget(ServerPlayerEntity hunter) {
        ((IEntityDataSaver) hunter).getPersistentData().remove(TARGET_NBT_KEY);
    }
    private static boolean isHunter(ServerPlayerEntity hunter) {
        return ((IEntityDataSaver) hunter).getPersistentData().contains(TARGET_NBT_KEY);
    }
}
