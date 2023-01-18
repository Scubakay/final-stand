package scubakay.laststand.event.handler;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import scubakay.laststand.networking.ModMessages;
import scubakay.laststand.util.ModGameruleRegister;

public class SyncHunterTrackingDeviceCooldownOnJoin implements ServerPlayConnectionEvents.Init {

    @Override
    public void onPlayInit(ServerPlayNetworkHandler handler, MinecraftServer server) {
        int hunterTrackingDeviceCooldown = server.getGameRules().getInt(ModGameruleRegister.HUNTER_TRACKING_DEVICE_COOLDOWN);
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(hunterTrackingDeviceCooldown);
        ServerPlayNetworking.send(handler.getPlayer(), ModMessages.SYNC_HUNTER_TRACKING_DEVICE_COOLDOWN, buffer);
    }
}
