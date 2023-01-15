package scubakay.laststand.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scubakay.laststand.event.callback.PlayerDeathCallback;

@Mixin(ServerPlayerEntity.class)
public class PlayerDeathMixin {
    @Inject(at = @At("HEAD"), method = "onDeath", cancellable = true)
    public void onDeath(DamageSource damageSource, CallbackInfo ci) {
        ActionResult result = PlayerDeathCallback.EVENT.invoker().interact((PlayerEntity) (Object) this, damageSource);

        if(result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
