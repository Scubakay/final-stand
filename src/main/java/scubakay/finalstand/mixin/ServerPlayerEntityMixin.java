package scubakay.finalstand.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scubakay.finalstand.event.ServerLivingEntityEvents;
import scubakay.finalstand.util.IServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements IServerPlayerEntity {
    @Shadow()
    @Final()
    public ServerPlayerInteractionManager interactionManager;

    public boolean fs_isSurvival() {
        return this.interactionManager.getGameMode() == GameMode.SURVIVAL;
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void notifyDeath(DamageSource source, CallbackInfo ci) {
        ServerLivingEntityEvents.START_AFTER_DEATH.invoker().startAfterDeath((ServerPlayerEntity) (Object) this, source);
    }
}
