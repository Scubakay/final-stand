package scubakay.finalstand.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.data.LivesData;

public class RequestLivesSyncS2CPacket {
    public static void receive(MinecraftServer ignoredServer, ServerPlayerEntity player, ServerPlayNetworkHandler ignoredHandler, PacketByteBuf ignoredBuf, PacketSender ignoredResponseSender) {
        LivesData.syncLives(((IEntityDataSaver) player).fs_getPersistentData().getInt("lives"), player);
    }
}
