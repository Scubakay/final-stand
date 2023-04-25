package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import scubakay.finalstand.config.ModConfig;
import scubakay.finalstand.networking.ModMessages;

public class SyncHunterTrackingDeviceCooldownOnJoin implements ServerPlayConnectionEvents.Init {

    @Override
    public void onPlayInit(ServerPlayNetworkHandler handler, MinecraftServer server) {
        int hunterTrackingDeviceCooldown = ModConfig.getHunterTrackingDeviceCooldown();
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(hunterTrackingDeviceCooldown);
        ServerPlayNetworking.send(handler.getPlayer(), ModMessages.SYNC_HUNTER_TRACKING_DEVICE_COOLDOWN, buffer);
    }
}
