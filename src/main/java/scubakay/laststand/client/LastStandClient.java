package scubakay.laststand.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import scubakay.laststand.item.ModItems;
import scubakay.laststand.networking.ModMessages;

@Environment(EnvType.CLIENT)
public class LastStandClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModItems.registerModItems();
        ModMessages.registerS2CPackets();

        HudRenderCallback.EVENT.register(new LivesHudOverlay());
    }
}
