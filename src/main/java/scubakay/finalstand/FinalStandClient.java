package scubakay.finalstand;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import scubakay.finalstand.event.ModEvents;
import scubakay.finalstand.networking.ModMessages;
import scubakay.finalstand.util.ModModelPredicateProviderRegistry;

@Environment(EnvType.CLIENT)
public class FinalStandClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMessages.registerS2CPackets();
        ModEvents.registerClientEvents();
        ModModelPredicateProviderRegistry.registerModelPredicates();
    }
}
