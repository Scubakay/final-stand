package scubakay.laststand.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import scubakay.laststand.LastStand;
import scubakay.laststand.networking.packet.LivesSyncDataS2CPacket;
import scubakay.laststand.networking.packet.RequestLivesSyncS2CPacket;
import scubakay.laststand.networking.packet.SyncHunterTrackingDeviceCooldownS2CPacket;

public class ModMessages {
    public static final Identifier LIVES_SYNC = new Identifier(LastStand.MOD_ID, "laststand.lives_sync");
    public static final Identifier REQUEST_LIVES_SYNC = new Identifier(LastStand.MOD_ID, "laststand.reqeust_lives_sync");
    public static final Identifier SYNC_HUNTER_TRACKING_DEVICE_COOLDOWN = new Identifier(LastStand.MOD_ID, "laststand.sync_hunter_tracking_device_cooldown");

    public static void registerC2SPackets() {
        System.out.printf("[%s] Registering C2S packets", LastStand.MOD_ID);
        ServerPlayNetworking.registerGlobalReceiver(REQUEST_LIVES_SYNC, RequestLivesSyncS2CPacket::receive);
    }

    public static void registerS2CPackets() {
        System.out.printf("[%s] Registering S2C packets", LastStand.MOD_ID);
        ClientPlayNetworking.registerGlobalReceiver(LIVES_SYNC, LivesSyncDataS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SYNC_HUNTER_TRACKING_DEVICE_COOLDOWN, SyncHunterTrackingDeviceCooldownS2CPacket::receive);
    }
}
