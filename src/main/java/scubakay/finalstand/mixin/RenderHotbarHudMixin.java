package scubakay.finalstand.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scubakay.finalstand.event.callback.HotbarRenderCallback;

@Mixin(InGameHud.class)
public class RenderHotbarHudMixin {
    @Inject(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(Lnet/minecraft/client/gui/DrawContext;F)V"))
    public void injectRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        HotbarRenderCallback.EVENT.invoker().onHudRender(context, tickDelta);
    }
}
