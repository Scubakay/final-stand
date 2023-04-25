package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import scubakay.finalstand.networking.ModMessages;

public class SyncSessionTimeOnJoin implements ServerPlayConnectionEvents.Init {
    @Override
    public void onPlayInit(ServerPlayNetworkHandler handler, MinecraftServer server) {
        int sessionTicksLeft = SessionHandler.getSessionTicksLeft(server);
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(sessionTicksLeft);
        ServerPlayNetworking.send(handler.getPlayer(), ModMessages.SESSION_TIME_SYNC, buffer);
    }
}