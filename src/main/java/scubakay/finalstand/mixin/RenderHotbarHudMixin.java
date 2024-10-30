package scubakay.finalstand.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scubakay.finalstand.event.callback.HotbarRenderCallback;

@Mixin(InGameHud.class)
public class RenderHotbarHudMixin {
    @Inject(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
    public void injectRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        HotbarRenderCallback.EVENT.invoker().onHudRender(context);
    }
}
