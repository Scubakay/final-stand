package scubakay.finalstand.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.config.ModConfig;
import scubakay.finalstand.item.ModItems;
import scubakay.finalstand.item.custom.HunterTrackingDevice;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.util.IServerPlayerEntity;

import java.util.*;

/**
 * Keeps track of current hunters using player NBT data
 */
public class HuntersState {
    private static final String TARGET_NBT_KEY = "finalstand.target";

    /**
     * Select X amount of random hunters
     */
    public static void selectHunters(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList().stream().filter(p -> ((IServerPlayerEntity) p).fs_isSurvival()).toList();

        Random randHunterAmount = new Random();
        int amount = randHunterAmount.nextInt(ModConfig.Hunters.max + 1 - ModConfig.Hunters.min) + ModConfig.Hunters.min;
        FinalStand.LOGGER.info(String.format("Picking %d hunters", amount));

        // Prevent red lives from being hunter if preventRedLifeHunter is true
        List<ServerPlayerEntity> possibleHunters = new ArrayList<>(players);
        if(ModConfig.Hunters.preventRedLifeHunter) {
            possibleHunters = possibleHunters.stream().filter(player -> ((IEntityDataSaver) player).fs_getPersistentData().getInt("lives") > 1).toList();
        }

        // Prevent red life targets if preventRedLifeTarget is true
        List<ServerPlayerEntity> validTargets = new ArrayList<>(players);
        if(ModConfig.Hunters.preventRedLifeTarget) {
            validTargets = validTargets.stream().filter(player -> ((IEntityDataSaver) player).fs_getPersistentData().getInt("lives") > 1).toList();
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

                // Send messages
                FinalStand.LOGGER.info(String.format("%s is hunting %s", hunter.getDisplayName(), target.getDisplayName()));
                hunter.sendMessage(Text.translatable("session.finalstand.you_are_hunter").formatted(Formatting.RED));
                if (ModConfig.Hunters.announceBeingHunted) {
                    target.sendMessage(Text.translatable("session.finalstand.you_are_being_hunted").formatted(Formatting.RED));
                }

                // Remove target and hunter from valid picks lists
                validTargets = validTargets.stream().filter(h -> !h.equals(target)).toList();
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
        if (validTargets.isEmpty()) {
            return null;
        }

        // Select a target from valid targets
        Random rand = new Random();
        int targetIndex = rand.nextInt(validTargets.size());
        return validTargets.get(targetIndex);
    }

    /**
     * Add a hunter
     */
    public static void addHunter(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        setTarget(hunter, target);
        addHunterTrackingDevice(hunter, target);
    }

    /**
     * Remove the bounty if a hunter killed their target
     */
    public static boolean removeIfBountyCompleted(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        if (isPlayerTarget(hunter, target)) {
            completeBounty(hunter);
            FinalStand.LOGGER.info(String.format("%s completed their bounty by killing %s", hunter.getDisplayName(), target.getDisplayName()));
            hunter.sendMessage(Text.translatable("session.finalstand.bounty_completed").formatted(Formatting.GREEN));
            target.sendMessage(Text.translatable("session.finalstand.no_longer_being_hunted").formatted(Formatting.GREEN));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reset all bounties
     */
    public static void reset(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList().stream().filter(p -> ((IServerPlayerEntity) p).fs_isSurvival()).toList();
        players.forEach(HuntersState::completeBounty);
    }

    /**
     * Remove a life for all hunters that failed to complete their bounty
     */
    public static void punishHunters(List<ServerPlayerEntity> players) {
        players.stream()
                .filter(HuntersState::isHunter)
                .forEach(h -> {
                    completeBounty(h);
                    LivesData.removeLives((IEntityDataSaver) h, 1);
                    h.sendMessage(Text.translatable("session.finalstand.bounty_failed").formatted(Formatting.RED));
                });
    }

    /**
     * Give a player a hunter tracking device
     */
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
    }

    /**
     * Remove the target UUID and tracking device from a hunter
     */
    public static void completeBounty(ServerPlayerEntity hunter) {
        removeTarget(hunter);
        while(hunter.getInventory().containsAny(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE))) {
            hunter.getInventory().remove(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE), 1, hunter.getInventory());
        }
    }

    /**
     * Check if a certain player is the hunter's target
     */
    private static boolean isPlayerTarget(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        ServerPlayerEntity hunterTarget = getTarget(hunter);
        if (hunterTarget == null) {
            return false;
        }
        return target.getUuidAsString().equals(hunterTarget.getUuidAsString());
    }

    /**
     * Set a hunter's target
     */
    private static void setTarget(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        ((IEntityDataSaver) hunter).fs_getPersistentData().putString(TARGET_NBT_KEY, target.getUuidAsString());
    }

    /**
     * Get the ServerPlayerEntity of a hunter's target
     */
    private static ServerPlayerEntity getTarget(ServerPlayerEntity player) {
        UUID targetUUID = getTargetUUID(player);
        Optional<ServerPlayerEntity> target = player.getServer().getPlayerManager().getPlayerList().stream()
                .filter(p -> p.getUuid().equals(targetUUID)).findFirst();
        return target.orElse(null);
    }

    /**
     * Get the UUID of a hunter's target
     */
    private static UUID getTargetUUID(ServerPlayerEntity hunter) {
        try {
            String targetString = ((IEntityDataSaver) hunter).fs_getPersistentData().getString(TARGET_NBT_KEY);
            return UUID.fromString(targetString);
        } catch(IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Removes target NBT from hunter
     */
    private static void removeTarget(ServerPlayerEntity hunter) {
        ((IEntityDataSaver) hunter).fs_getPersistentData().remove(TARGET_NBT_KEY);
    }

    /**
     * If this player is a hunter
     */
    private static boolean isHunter(ServerPlayerEntity hunter) {
        return getTarget(hunter) != null;
    }

    /**
     * Persists the players target after respawning
     */
    public static void persistTarget(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        UUID target = getTargetUUID(oldPlayer);
        if (target != null) {
            ((IEntityDataSaver) newPlayer).fs_getPersistentData().putString(TARGET_NBT_KEY, target.toString());
        }
    }
}
