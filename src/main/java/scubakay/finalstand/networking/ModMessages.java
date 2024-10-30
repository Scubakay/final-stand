package scubakay.finalstand.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.networking.packet.LivesSyncPacket;
import scubakay.finalstand.networking.packet.SessionTimeSyncPacket;

public class ModMessages {
    public static void registerServerReceivers() {
        FinalStand.LOGGER.info("Registering server packet receivers");
    }

    public static void registerClientReceivers() {
        FinalStand.LOGGER.info("Registering client packet receivers");
        ClientPlayNetworking.registerGlobalReceiver(LivesSyncPacket.ID, LivesSyncPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SessionTimeSyncPacket.ID, SessionTimeSyncPacket::receive);
    }

    public static void registerPayloads() {
        // Play: Server to Client
        PayloadTypeRegistry.playS2C().register(LivesSyncPacket.ID, LivesSyncPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SessionTimeSyncPacket.ID, SessionTimeSyncPacket.PACKET_CODEC);
    }
}
