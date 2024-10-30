package scubakay.finalstand.event.handler;

import scubakay.finalstand.event.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.data.LivesData;

public class PlayerDeathHandlerAfterDeath implements ServerLivingEntityEvents.StartAfterDeath {
    @Override
    public void startAfterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity instanceof ServerPlayerEntity target) {
            LivesData.removeLives((IEntityDataSaver) target, 1);
        }
    }
}
