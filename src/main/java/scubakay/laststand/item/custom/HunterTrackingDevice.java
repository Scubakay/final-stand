package scubakay.laststand.item.custom;

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
import scubakay.laststand.sounds.ModSounds;
import scubakay.laststand.util.ModGameruleRegister;

import java.awt.geom.Point2D;

public class HunterTrackingDevice extends Item {
    PlayerEntity trackedPlayer;
    public HunterTrackingDevice(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()) {
            getTrackedPlayerFromNbt(world, user, hand);
            if(trackedPlayer != null) {
                setCooldown(world, user);
                double distance = getDistanceToTarget(user);
                world.playSound(null, user.getBlockPos(), ModSounds.HUNTER_TRACKING_DEVICE, SoundCategory.PLAYERS, 1f, 1f);
                user.sendMessage(Text.translatable("item.laststand.hunter_tracking_device_target_distance", Math.round(distance))
                        .fillStyle(Style.EMPTY.withColor(Formatting.RED)), true);
            } else {
                user.sendMessage(Text.translatable("item.laststand.hunter_tracking_device_no_player_found_error")
                        .fillStyle(Style.EMPTY.withColor(Formatting.RED)), false);
            }
        }

        return super.use(world, user, hand);
    }

    private void setCooldown(World world, PlayerEntity user) {
        int cooldown = world.getGameRules().getInt(ModGameruleRegister.HUNTER_TRACKING_DEVICE_COOLDOWN);
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
