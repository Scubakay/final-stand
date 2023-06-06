package scubakay.finalstand.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public class ServerLivingEntityEvents {
    /**
     * An event that is called at the start of the onDeath event of a living entity
     */
    public static final Event<StartAfterDeath> START_AFTER_DEATH = EventFactory.createArrayBacked(StartAfterDeath.class, callbacks -> (entity, damageSource) -> {
        for (StartAfterDeath callback : callbacks) {
            callback.startAfterDeath(entity, damageSource);
        }
    });

    @FunctionalInterface
    public interface StartAfterDeath {
        /**
         * Called when a living entity dies. The death cannot be canceled at this point.
         *
         * @param entity the entity
         * @param damageSource the source of the fatal damage
         */
        void startAfterDeath(LivingEntity entity, DamageSource damageSource);
    }
}
