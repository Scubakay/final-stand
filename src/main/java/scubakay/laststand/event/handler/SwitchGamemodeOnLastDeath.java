package scubakay.laststand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import scubakay.laststand.util.IEntityDataSaver;
import scubakay.laststand.util.LivesData;

public class SwitchGamemodeOnLastDeath implements ServerLivingEntityEvents.AfterDeath {
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity instanceof ServerPlayerEntity player) {
            int lives = LivesData.removeLives((IEntityDataSaver) player, 1);
            if(lives == 0) {
                setGamemodeToSpectator(player);
                player.sendMessage(Text.translatable("item.laststand.game_over"));
            } else {
                player.sendMessage(Text.translatable("item.laststand.lives_left", lives));
            }
        }
    }

    private static void setGamemodeToSpectator(ServerPlayerEntity player) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("playerGameType", 3);
        player.setGameMode(nbt);
    }
}
