package scubakay.laststand.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import scubakay.laststand.LastStand;
import scubakay.laststand.networking.packet.LivesSyncDataS2CPacket;
import scubakay.laststand.networking.packet.RandomizeLivesC2SPacket;
import scubakay.laststand.networking.packet.RemoveLifeC2SPacket;
import scubakay.laststand.networking.packet.RequestLivesSyncS2CPacket;

public class ModMessages {
    public static final Identifier RANDOMIZE_LIVES = new Identifier(LastStand.MOD_ID, "laststand.randomize_lives");
    public static final Identifier REMOVE_LIFE = new Identifier(LastStand.MOD_ID, "laststand.remove_life");
    public static final Identifier LIVES_SYNC = new Identifier(LastStand.MOD_ID, "laststand.lives_sync");
    public static final Identifier REQUEST_LIVES_SYNC = new Identifier(LastStand.MOD_ID, "laststand.reqeust_lives_sync");

    public static void registerC2SPackets() {
        System.out.printf("[%s] Registering C2S packets", LastStand.MOD_ID);
        ServerPlayNetworking.registerGlobalReceiver(RANDOMIZE_LIVES, RandomizeLivesC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(REMOVE_LIFE, RemoveLifeC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(REQUEST_LIVES_SYNC, RequestLivesSyncS2CPacket::receive);
    }

    public static void registerS2CPackets() {
        System.out.printf("[%s] Registering S2C packets", LastStand.MOD_ID);
        ClientPlayNetworking.registerGlobalReceiver(LIVES_SYNC, LivesSyncDataS2CPacket::receive);
    }
}
