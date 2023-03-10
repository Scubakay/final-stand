package scubakay.finalstand;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import scubakay.finalstand.client.LivesHudOverlay;
import scubakay.finalstand.event.ModEvents;
import scubakay.finalstand.event.callback.HotbarRenderCallback;
import scubakay.finalstand.networking.ModMessages;
import scubakay.finalstand.util.ModModelPredicateProviderRegistry;

@Environment(EnvType.CLIENT)
public class FinalStandClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMessages.registerS2CPackets();
        ModEvents.registerClientEvents();
        ModModelPredicateProviderRegistry.registerModelPredicates();

        HotbarRenderCallback.EVENT.register(new LivesHudOverlay());
    }
}
