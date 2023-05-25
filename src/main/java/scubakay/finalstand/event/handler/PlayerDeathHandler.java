package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.data.LivesData;

public class PlayerDeathHandler implements ServerLivingEntityEvents.AfterDeath {
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity instanceof ServerPlayerEntity target) {
            LivesData.removeLives((IEntityDataSaver) target, 1);
            handleBounties(target, damageSource);
        }
    }

    private static void handleBounties(ServerPlayerEntity target, DamageSource damageSource) {
        if (damageSource.getAttacker() instanceof ServerPlayerEntity hunter) {
            HuntersState.removeIfBountyCompleted(hunter, target);
        }
    }
}
