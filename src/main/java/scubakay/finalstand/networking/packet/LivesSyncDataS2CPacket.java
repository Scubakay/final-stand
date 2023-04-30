package scubakay.finalstand.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import scubakay.finalstand.util.IEntityDataSaver;

public class LivesSyncDataS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler ignoredHandler, PacketByteBuf buf, PacketSender ignoredResponseSender) {
        if (client.player != null) {
            ((IEntityDataSaver) client.player).fs_getPersistentData().putInt("lives", buf.readInt());
        }
    }
}
