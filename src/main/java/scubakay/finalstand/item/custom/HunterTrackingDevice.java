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
    private static final double MAX_PITCH_SHIFT_DISTANCE = 400;

    private static final int TICKS_PER_SECOND = 20;

    private int usageTicksLeft = -1;
    private double lastDistanceToTarget = -1;

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
        if (this.usageTicksLeft < 0) {
            return;
        }
        if (entity instanceof PlayerEntity player) {
            if (--this.usageTicksLeft == 0) {
                player.sendMessage(Text.translatable("item.finalstand.hunter_tracking_device_target_distance", Math.round(this.lastDistanceToTarget))
                        .fillStyle(Style.EMPTY.withColor(Formatting.RED)), true);
                player.playSound(ModSounds.HUNTER_TRACKING_DEVICE, SoundCategory.BLOCKS, 1f, calculatePitch(this.lastDistanceToTarget));
            }
        }
    }

    private void startUsing(World world, PlayerEntity user, Hand hand) {
        setCooldown(user);
        PlayerEntity trackedPlayer = getTrackedPlayerFromNbt(world, user, hand);
        this.lastDistanceToTarget = getDistanceToTarget(user, trackedPlayer);
        this.usageTicksLeft = 2 * TICKS_PER_SECOND;
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

    private float calculatePitch(double distance) {
        return (float) (1f - (distance / MAX_PITCH_SHIFT_DISTANCE));
    }
}
