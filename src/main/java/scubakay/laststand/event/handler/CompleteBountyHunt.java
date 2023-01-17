package scubakay.laststand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.laststand.item.ModItems;
import scubakay.laststand.util.HuntersState;

public class CompleteBountyHunt implements ServerLivingEntityEvents.AfterDeath {
    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        if(entity instanceof ServerPlayerEntity target) {
            if(damageSource.getAttacker() instanceof ServerPlayerEntity hunter) {
                if(HuntersState.removeIfCompleted(hunter, target)){
                    this.removeHunterTrackingDevice(hunter);
                }

            }
        }
    }

    /**
     * Remove the hunter's tracking device
     */
    private void removeHunterTrackingDevice(ServerPlayerEntity hunter) {
        hunter.getInventory().remove(stack -> stack.isOf(ModItems.HUNTER_TRACKING_DEVICE), 1, hunter.getInventory());
    }
}
