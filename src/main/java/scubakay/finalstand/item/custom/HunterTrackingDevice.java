package scubakay.finalstand.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import scubakay.finalstand.config.ModConfig;
import scubakay.finalstand.sounds.ModSounds;

import java.util.List;

public class HunterTrackingDevice extends Item {
    private static final String SCAN_DISTANCE_KEY = "bounty-distance";
    public static final String SCAN_TARGET_KEY = "bounty-target";
    public static int cooldown = 0;
    private static final int TICKS_PER_SECOND = 20;

    public HunterTrackingDevice(Item.Settings settings) {
        super(settings);
        cooldown = ModConfig.Hunters.hunterTrackingDeviceCooldown;
    }

    /**
     * Get the tracked player from NBT and set the scan distance in NBT to indicate scanning
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        setCooldown(user);
        PlayerEntity trackedPlayer = getTrackedPlayerFromNbt(world, user, hand);
        double distance = getDistanceToTarget(user, trackedPlayer);
        setScanDistance(stack, distance);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    /**
     * Play scanning sound every second of usage
     */
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient && user instanceof PlayerEntity player && remainingUseTicks % TICKS_PER_SECOND == 0) {
            player.playSound(ModSounds.TRACKING_DEVICE_SEARCHING, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    /**
     * Reset scan distance when player stops scanning
     */
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        resetScanDistance(stack);
    }

    /**
     * Play success sound and show distance on completed use
     */
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            double distance = getScanDistance(stack);
            sendDistanceMessage(player, distance);
            player.playSound(ModSounds.TRACKING_DEVICE_CONFIRM, SoundCategory.BLOCKS, 1f, 1f);
            resetScanDistance(stack);
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return HunterTrackingDevice.getScanTime(stack);
    }

    /**
     * Retrieves scan distance from NBT and converts it to seconds
     */
    public static int getScanTime(ItemStack stack) {
        double distance = getScanDistance(stack);
        return getDelayInSeconds(distance) * TICKS_PER_SECOND;
    }

    /**
     * Write scan distance to NBT
     */
    public static void setScanDistance(ItemStack stack, double distance) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putDouble(SCAN_DISTANCE_KEY, distance);
    }

    /**
     * Get scan distance from NBT
     */
    public static double getScanDistance(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        return nbtCompound != null ? nbtCompound.getDouble(SCAN_DISTANCE_KEY) : 99999;
    }

    /**
     * Remove scan distance from NBT
     */
    public static void resetScanDistance(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.remove(SCAN_DISTANCE_KEY);
    }

    /**
     * Check if scan distance has been set in NBT
     */
    public static boolean isScanning(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        return nbtCompound.contains(SCAN_DISTANCE_KEY);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.finalstand.hunter_tracking_device_tooltip").formatted(Formatting.BLUE));
    }

    /**
     * Send message to player indicating how far their target approximately is.
     */
    private void sendDistanceMessage(PlayerEntity player, double distance) {
        int secondsDelay = getDelayInSeconds(distance);
        String message_id = switch (secondsDelay) {
            case 1 -> "item.finalstand.hunter_tracking_device_target_is_here";
            case 2 -> "item.finalstand.hunter_tracking_device_target_is_close";
            case 3 -> "item.finalstand.hunter_tracking_device_getting_closer";
            case 4 -> "item.finalstand.hunter_tracking_device_far_away";
            default -> "item.finalstand.hunter_tracking_device_very_far_away";
        };
        Formatting color = switch (secondsDelay) {
            case 1 -> Formatting.GREEN;
            case 2 -> Formatting.DARK_GREEN;
            case 3 -> Formatting.YELLOW;
            case 4 -> Formatting.RED;
            default -> Formatting.DARK_RED;
        };
        int roundedDistance = (int) Math.floor(distance / 10) * 10;
        player.sendMessage(Text.translatable(message_id, roundedDistance)
                .fillStyle(Style.EMPTY.withColor(color)), true);
    }

    /**
     * Get scan delay in seconds based on distance
     */
    private static int getDelayInSeconds(double distance) {
        int seconds;
        if (distance > 200) {
            seconds = 5;
        } else if (distance > 100) {
            seconds = 4;
        } else if (distance > 50) {
            seconds = 3;
        } else if (distance > 20) {
            seconds = 2;
        } else {
            seconds = 1;
        }
        return seconds;
    }

    private void setCooldown(PlayerEntity user) {
        user.getItemCooldownManager().set(this, cooldown);
    }

    /**
     * Calculates distance to target
     */
    private double getDistanceToTarget(PlayerEntity user, PlayerEntity trackedPlayer) {
        if (trackedPlayer != null) {
            Vec3d trackedPlayerPosition = trackedPlayer.getPos();
            Vec3d playerPosition = user.getPos();
            return distance(playerPosition.getX(), playerPosition.getZ(), trackedPlayerPosition.getX(), trackedPlayerPosition.getZ());
        }
        return 9999999;
    }

    /**
     * Get tracked player from NBT
     */
    private PlayerEntity getTrackedPlayerFromNbt(World world, PlayerEntity user, Hand hand) {
        NbtCompound nbtCompound = user.getStackInHand(hand).getNbt();
        if (nbtCompound == null) {
            return null;
        }
        String targetUuid = nbtCompound.getString(SCAN_TARGET_KEY);
        return world.getPlayers().stream().filter((PlayerEntity player) -> player.getUuidAsString().equals(targetUuid)).findFirst().orElse(null);
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
}
