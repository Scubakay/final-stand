package scubakay.finalstand.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scubakay.finalstand.event.callback.HotbarRenderCallback;

@Mixin(InGameHud.class)
public class RenderHotbarHudMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V"))
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo callbackInfo) {
        HotbarRenderCallback.EVENT.invoker().onHudRender(matrixStack, tickDelta);
    }
}
