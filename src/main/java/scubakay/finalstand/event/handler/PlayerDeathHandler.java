package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import scubakay.finalstand.config.ModConfig;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.data.LivesData;

public class PlayerDeathHandler implements ServerLivingEntityEvents.AfterDeath {
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity instanceof ServerPlayerEntity target) {
            int lives = LivesData.removeLives((IEntityDataSaver) target, 1);
            handleBounties(target, damageSource);
            handleLastLive(target, lives);
        }
    }

    private static void handleLastLive(ServerPlayerEntity target, int lives) {
        if(lives == 0) {
            dropEverything(target);
            setGamemodeToSpectator(target);
            target.sendMessage(Text.translatable("session.finalstand.game_over"));
        } else {
            target.sendMessage(Text.translatable("session.finalstand.lives_left", lives));
        }
    }

    private static void dropEverything(ServerPlayerEntity target) {
        target.getInventory().dropAll();
    }

    private static void handleBounties(ServerPlayerEntity target, DamageSource damageSource) {
        if(ModConfig.Hunters.bountyReward && damageSource.getAttacker() instanceof ServerPlayerEntity hunter) {
            HuntersState.rewardHunter(hunter, target);
        }
        HuntersState.removeIfPlayerWasHunter(target);
        HuntersState.removeIfPlayerWasTarget(target);
    }

    private static void setGamemodeToSpectator(ServerPlayerEntity player) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("playerGameType", 3);
        player.setGameMode(nbt);
    }
}
