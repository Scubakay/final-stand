package scubakay.laststand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.laststand.data.HuntersState;

public class CompleteBountyHunt implements ServerLivingEntityEvents.AfterDeath {
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity instanceof ServerPlayerEntity target) {
            HuntersState.removeIfPlayerWasHunter(target);
            HuntersState.removeIfPlayerWasTarget(target);
        }
    }
}
