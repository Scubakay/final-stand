package scubakay.finalstand.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import scubakay.finalstand.sounds.ModSounds;

import java.awt.geom.Point2D;
import java.util.List;

public class HunterTrackingDevice extends Item {

    public static int cooldown = 0;
    private static final int TICKS_PER_SECOND = 20;
    private int usageTicksLeft = -1;
    private double lastDistanceToTarget = -1;
    private boolean used = false;
    private float animationCycle = 0f;

    public HunterTrackingDevice(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            startUsing(world, user, hand);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.finalstand.hunter_tracking_device_tooltip").formatted(Formatting.BLUE));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient() && this.used && entity instanceof PlayerEntity player) {
            this.animationCycle += 0.08f;
            if (this.animationCycle > 1) {
                this.animationCycle = 0f;
            }
            if (this.usageTicksLeft-- < 0) {
                sendDistanceMessage(player, lastDistanceToTarget);
                player.playSound(ModSounds.HUNTER_TRACKING_DEVICE, SoundCategory.BLOCKS, 1f, 1f);
                this.animationCycle = 0f;
                this.used = false;
            }
        }
    }

    public boolean wasUsed() {
        return this.used;
    }

    public float getAnimationCycle() {
        return this.animationCycle;
    }

    private void sendDistanceMessage(PlayerEntity player, double lastDistanceToTarget) {
        int secondsDelay = getDelayInSeconds(lastDistanceToTarget);
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
        player.sendMessage(Text.translatable(message_id, Math.round(this.lastDistanceToTarget))
                .fillStyle(Style.EMPTY.withColor(color)), true);
    }

    private void startUsing(World world, PlayerEntity user, Hand hand) {
        setCooldown(user);
        PlayerEntity trackedPlayer = getTrackedPlayerFromNbt(world, user, hand);
        this.lastDistanceToTarget = getDistanceToTarget(user, trackedPlayer);
        int secondsDelay = this.getDelayInSeconds(this.lastDistanceToTarget);
        this.usageTicksLeft = secondsDelay * TICKS_PER_SECOND;
        this.used = true;
        playScanningSound(user, secondsDelay);
    }

    private void playScanningSound(PlayerEntity player, int secondsDelay) {
        // player.playSound(ModSounds.HUNTER_TRACKING_DEVICE, SoundCategory.BLOCKS, 1f, calculatePitch(this.lastDistanceToTarget));
        // TODO: Determine what sound file to play based on seconds delay
    }

    private int getDelayInSeconds(double distance) {
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

    private double getDistanceToTarget(PlayerEntity user, PlayerEntity trackedPlayer) {
        if (trackedPlayer != null) {
            Vec3d trackedPlayerPosition = trackedPlayer.getPos();
            Vec3d playerPosition = user.getPos();
            return Point2D.distance(playerPosition.getX(), playerPosition.getZ(), trackedPlayerPosition.getX(), trackedPlayerPosition.getZ());
        }
        return 9999999;
    }

    private PlayerEntity getTrackedPlayerFromNbt(World world, PlayerEntity user, Hand hand) {
        String targetUuid = user.getStackInHand(hand).getNbt().get("target").asString();
        return world.getPlayers().stream().filter((PlayerEntity player) -> player.getUuidAsString().equals(targetUuid)).findFirst().orElse(null);
    }
}
