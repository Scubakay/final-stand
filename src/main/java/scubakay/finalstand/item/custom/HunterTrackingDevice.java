package scubakay.finalstand.item.custom;

import net.minecraft.client.item.TooltipContext;
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

    PlayerEntity trackedPlayer;
    public HunterTrackingDevice(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) {
            getTrackedPlayerFromNbt(world, user, hand);
            if(trackedPlayer != null) {
                setCooldown(user);
                double distance = getDistanceToTarget(user);
                world.playSound(null, user.getBlockPos(), ModSounds.HUNTER_TRACKING_DEVICE, SoundCategory.PLAYERS, 1f, 1f);
                user.sendMessage(Text.translatable("item.finalstand.hunter_tracking_device_target_distance", Math.round(distance))
                        .fillStyle(Style.EMPTY.withColor(Formatting.RED)), true);
            } else {
                user.sendMessage(Text.translatable("item.finalstand.hunter_tracking_device_no_player_found_error")
                        .fillStyle(Style.EMPTY.withColor(Formatting.RED)), false);
            }
        }

        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.finalstand.hunter_tracking_device_tooltip").formatted(Formatting.BLUE));
    }

    private void setCooldown(PlayerEntity user) {
        user.getItemCooldownManager().set(this, cooldown);
    }

    private double getDistanceToTarget(PlayerEntity user) {
        Vec3d trackedPlayerPosition = trackedPlayer.getPos();
        Vec3d playerPosition = user.getPos();
        return Point2D.distance(playerPosition.getX(), playerPosition.getZ(), trackedPlayerPosition.getX(), trackedPlayerPosition.getZ());
    }

    private void getTrackedPlayerFromNbt(World world, PlayerEntity user, Hand hand) {
        String targetUuid = user.getStackInHand(hand).getNbt().get("target").asString();
        this.trackedPlayer = world.getPlayers().stream().filter((PlayerEntity player) -> player.getUuidAsString().equals(targetUuid)).findFirst().orElse(null);
    }
}
