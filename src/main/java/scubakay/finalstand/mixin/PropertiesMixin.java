package scubakay.finalstand.mixin;

import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scubakay.finalstand.client.LivesHudOverlay;

@Mixin(ClientWorld.Properties.class)
public class PropertiesMixin {
    @Inject(method = "isHardcore", at = @At("HEAD"), cancellable = true)
    public void injectIsHardcore(CallbackInfoReturnable<Boolean> cir) {
        if (LivesHudOverlay.getLives() == 0) {
            cir.setReturnValue(true);
        }
    }
}
