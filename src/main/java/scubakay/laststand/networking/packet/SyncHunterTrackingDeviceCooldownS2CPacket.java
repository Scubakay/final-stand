package scubakay.laststand.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import scubakay.laststand.item.custom.HunterTrackingDevice;

public class SyncHunterTrackingDeviceCooldownS2CPacket {
    public static void receive(MinecraftClient ignoredClient, ClientPlayNetworkHandler ignoredHandler, PacketByteBuf buf, PacketSender ignoredResponseSender) {
        HunterTrackingDevice.cooldown = buf.readInt();
    }
}
