package scubakay.finalstand.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.networking.packet.LivesSyncDataS2CPacket;
import scubakay.finalstand.networking.packet.RequestLivesSyncS2CPacket;
import scubakay.finalstand.networking.packet.SessionTimeSyncS2CPacket;
import scubakay.finalstand.networking.packet.HunterTrackingDeviceCooldownSyncS2CPacket;

public class ModMessages {
    public static final Identifier LIVES_SYNC = new Identifier(FinalStand.MOD_ID, "finalstand.lives_sync");
    public static final Identifier REQUEST_LIVES_SYNC = new Identifier(FinalStand.MOD_ID, "finalstand.reqeust_lives_sync");
    public static final Identifier HUNTER_TRACKING_DEVICE_COOLDOWN_SYNC = new Identifier(FinalStand.MOD_ID, "finalstand.sync_hunter_tracking_device_cooldown");
    public static final Identifier SESSION_TIME_SYNC = new Identifier(FinalStand.MOD_ID, "finalstand.session_time_sync");

    public static void registerC2SPackets() {
        System.out.printf("[%s] Registering C2S packets\n", FinalStand.MOD_ID);
        ServerPlayNetworking.registerGlobalReceiver(REQUEST_LIVES_SYNC, RequestLivesSyncS2CPacket::receive);
    }

    public static void registerS2CPackets() {
        System.out.printf("[%s] Registering S2C packets\n", FinalStand.MOD_ID);
        ClientPlayNetworking.registerGlobalReceiver(LIVES_SYNC, LivesSyncDataS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(HUNTER_TRACKING_DEVICE_COOLDOWN_SYNC, HunterTrackingDeviceCooldownSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SESSION_TIME_SYNC, SessionTimeSyncS2CPacket::receive);
    }
}
