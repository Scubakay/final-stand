package scubakay.finalstand.mixin;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scubakay.finalstand.data.LivesData;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "sendPlayerStatus", at = @At("RETURN"))
    public void injectSendPlayerStatus(ServerPlayerEntity player, CallbackInfo ci) {
        LivesData.syncLives(player);
    }
}
