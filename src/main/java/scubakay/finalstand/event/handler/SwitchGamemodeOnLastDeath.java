package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.data.LivesData;
import scubakay.finalstand.util.ModGameruleRegister;

public class SwitchGamemodeOnLastDeath implements ServerLivingEntityEvents.AfterDeath {
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity instanceof ServerPlayerEntity target) {
            int lives = LivesData.removeLives((IEntityDataSaver) target, 1);
            if(entity.getWorld().getGameRules().getBoolean(ModGameruleRegister.BOUNTY_REWARD)
                    && damageSource.getAttacker() instanceof ServerPlayerEntity hunter) {
                HuntersState.rewardHunter(hunter, target);
            }

            if(lives == 0) {
                setGamemodeToSpectator(target);
                target.sendMessage(Text.translatable("item.finalstand.game_over"));
            } else {
                target.sendMessage(Text.translatable("item.finalstand.lives_left", lives));
            }
        }
    }

    private static void setGamemodeToSpectator(ServerPlayerEntity player) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("playerGameType", 3);
        player.setGameMode(nbt);
    }
}
