package scubakay.finalstand.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;

public interface HotbarRenderCallback {
    Event<HotbarRenderCallback> EVENT = EventFactory.createArrayBacked(HotbarRenderCallback.class, (listeners) -> (matrixStack, delta) -> {
        for (HotbarRenderCallback event : listeners) {
            event.onHudRender(matrixStack, delta);
        }
    });

    /**
     * Called after rendering the whole hud, which is displayed in game, in a world.
     *
     * @param matrixStack the matrixStack
     * @param tickDelta Progress for linearly interpolating between the previous and current game state
     */
    void onHudRender(MatrixStack matrixStack, float tickDelta);
}