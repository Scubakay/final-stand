package scubakay.finalstand.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import scubakay.finalstand.item.ModItems;
import scubakay.finalstand.item.custom.HunterTrackingDevice;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.util.IServerPlayerEntity;
import scubakay.finalstand.util.ModGameruleRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Keeps track of current hunters using player NBT data
 */
public class HuntersState {
    private static final String TARGET_NBT_KEY = "finalstand.target";

    /**
     * Select X amount of random hunters
     */
    public static void selectHunters(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList().stream().filter(p -> ((IServerPlayerEntity) p).isSurvival()).toList();

        int amount = server.getGameRules().getInt(ModGameruleRegister.HUNTER_AMOUNT);
        boolean preventRedLifeHunter = server.getGameRules().getBoolean(ModGameruleRegister.PREVENT_RED_LIFE_HUNTER);
        boolean preventRedLifeTarget = server.getGameRules().getBoolean(ModGameruleRegister.PREVENT_RED_LIFE_TARGET);

        // Prevent red lives from being hunter if preventRedLifeHunter is true
        List<ServerPlayerEntity> possibleHunters = new ArrayList<>(players);
        if(preventRedLifeHunter) {
            possibleHunters = possibleHunters.stream().filter(player -> ((IEntityDataSaver) player).getPersistentData().getInt("lives") > 1).toList();
        }

        // Prevent red life targets if preventRedLifeTarget is true
        List<ServerPlayerEntity> validTargets = new ArrayList<>(players);
        if(preventRedLifeTarget) {
            validTargets = validTargets.stream().filter(player -> ((IEntityDataSaver) player).getPersistentData().getInt("lives") > 1).toList();
        }

        server.getPlayerManager().broadcast(Text.translatable("session.finalstand.bounty_hunters_selected").formatted(Formatting.DARK_RED), false);

        // Select hunters
        Random rand = new Random();
        while(amount > 0 && possibleHunters.size() > 0 && validTargets.size() > 0) {
            // Pick random hunter and target
            int hunterIndex = rand.nextInt(possibleHunters.size());
            ServerPlayerEntity hunter = possibleHunters.get(hunterIndex);
            ServerPlayerEntity target = selectTarget(validTargets, hunter);

            // Don't be hunter if there's no players left with > 1 life
            if(target != null) {
                amount--;
                addHunter(hunter, target);
                possibleHunters = possibleHunters.stream().filter(h -> !h.equals(hunter)).toList();
            } else {
                break;
            }
        }
    }

    /**
     * Select a random target for a hunter
     */
    private static ServerPlayerEntity selectTarget(List<ServerPlayerEntity> players, ServerPlayerEntity hunter) {
        List<ServerPlayerEntity> validTargets = new ArrayList<>(players);
        validTargets.remove(hunter);

        // Select a target from valid targets
        Random rand = new Random();
        ServerPlayerEntity target = null;
        while(target == null && validTargets.size() > 0) {
            int targetIndex = rand.nextInt(validTargets.size());
            target = validTargets.get(targetIndex);
        }

        target.sendMessage(Text.translatable("session.finalstand.you_are_being_hunted").formatted(Formatting.RED));

        return target;
    }

    public static void addHunter(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        setTarget(hunter, target);
        addHunterTrackingDevice(hunter, target);
    }

    public static void removeIfPlayerWasHunter(ServerPlayerEntity player) {
        ServerPlayerEntity target = getTarget(player);
        if(target != null){
            removeHunterTrackingDevice(player);
            player.sendMessage(Text.translatable("session.finalstand.bounty_completed").formatted(Formatting.GREEN));
            target.sendMessage(Text.translatable("session.finalstand.no_longer_being_hunted").formatted(Formatting.GREEN));
        }
    }

    public static void removeIfPlayerWasTarget(ServerPlayerEntity player) {
        List<ServerPlayerEntity> hunters = getHunters(player);
        if (!hunters.isEmpty()) {
            player.sendMessage(Text.translatable("session.finalstand.no_longer_being_hunted").formatted(Formatting.GREEN));
            hunters.forEach(h -> {
                removeHunterTrackingDevice(h);
                h.sendMessage(Text.translatable("session.finalstand.bounty_completed").formatted(Formatting.GREEN));
            });
        }
    }

    public static void reset(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList().stream().filter(p -> ((IServerPlayerEntity) p).isSurvival()).toList();
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
                    h.sendMessage(Text.translatable("session.finalstand.bounty_failed").formatted(Formatting.RED));
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

        hunter.sendMessage(Text.translatable("session.finalstand.you_are_hunter").formatted(Formatting.RED));
    }

    private static void removeHunterTrackingDevice(ServerPlayerEntity hunter) {
        removeTarget(hunter);
        while(hunter.getInventory().containsAny(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE))) {
            hunter.getInventory().remove(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE), 1, hunter.getInventory());
        }
    }

    private static List<ServerPlayerEntity> getHunters(ServerPlayerEntity target) {
        List<ServerPlayerEntity> players = target.getServer().getPlayerManager().getPlayerList();
        return players.stream()
                .filter(p -> getTargetUUID(p).equals(target.getUuidAsString()))
                .toList();
    }

    private static void setTarget(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        ((IEntityDataSaver) hunter).getPersistentData().putString(TARGET_NBT_KEY, target.getUuidAsString());
    }

    private static String getTargetUUID(ServerPlayerEntity hunter) {
        return ((IEntityDataSaver) hunter).getPersistentData().getString(TARGET_NBT_KEY);
    }

    private static ServerPlayerEntity getTarget(ServerPlayerEntity player) {
        String targetUUID = getTargetUUID(player);
        Optional<ServerPlayerEntity> target = player.getServer().getPlayerManager().getPlayerList().stream()
                .filter(p -> p.getUuidAsString().equals(targetUUID)).findFirst();
        return target.orElse(null);
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
