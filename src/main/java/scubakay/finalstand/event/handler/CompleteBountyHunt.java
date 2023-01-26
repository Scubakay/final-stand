package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.finalstand.data.HuntersState;

public class CompleteBountyHunt implements ServerLivingEntityEvents.AllowDeath {
    @Override
    public boolean allowDeath(LivingEntity entity, DamageSource damageSource, float damageAmount) {
        if(entity instanceof ServerPlayerEntity target) {
            HuntersState.removeIfPlayerWasHunter(target);
            HuntersState.removeIfPlayerWasTarget(target);
        }
        return true;
    }
}
