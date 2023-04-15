package scubakay.finalstand.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import scubakay.finalstand.client.CountdownOverlay;

public class SessionTimeSync {
    public static void receive(MinecraftClient ignoredClient, ClientPlayNetworkHandler ignoredHandler, PacketByteBuf buf, PacketSender ignoredResponseSender) {
        CountdownOverlay.setSessionTicksLeft(buf.readInt());
    }
}
