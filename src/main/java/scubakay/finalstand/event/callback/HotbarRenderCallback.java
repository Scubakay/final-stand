package scubakay.finalstand.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;

public interface HotbarRenderCallback {
    Event<HotbarRenderCallback> EVENT = EventFactory.createArrayBacked(HotbarRenderCallback.class, (listeners) -> (context) -> {
        for (HotbarRenderCallback event : listeners) {
            event.onHudRender(context);
        }
    });

    /**
     * Called after rendering the whole hud, which is displayed in game, in a world.
     *
     * @param context the DrawContext
     */
    void onHudRender(DrawContext context);
}