package scubakay.finalstand.event.handler;

import scubakay.finalstand.event.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.data.LivesData;

public class PlayerDeathHandlerAfterDeath implements ServerLivingEntityEvents.StartAfterDeath {
    @Override
    public void startAfterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity instanceof ServerPlayerEntity target) {
            LivesData.removeLives((IEntityDataSaver) target, 1);
            handleBounties(target, damageSource);
        }
    }

    private static void handleBounties(ServerPlayerEntity target, DamageSource damageSource) {
        if (damageSource.getAttacker() instanceof ServerPlayerEntity hunter) {
            HuntersState.removeIfBountyCompleted(hunter, target);
            FinalStand.LOGGER.info("Damage source attacker is player");
        } else if (target.getPrimeAdversary() instanceof ServerPlayerEntity hunter) {
            HuntersState.removeIfBountyCompleted(hunter, target);
            FinalStand.LOGGER.info("Prime adversary is player");
        } else {
            FinalStand.LOGGER.info("None of those");
        }
    }
}
