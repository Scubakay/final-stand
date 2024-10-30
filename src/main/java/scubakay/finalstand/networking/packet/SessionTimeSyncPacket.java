package scubakay.finalstand.networking.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.client.CountdownOverlay;

public record SessionTimeSyncPacket(int time) implements CustomPayload {
    public static final Identifier SESSION_TIME_SYNC = new Identifier(FinalStand.MOD_ID, "finalstand.session_time_sync");

    public static final CustomPayload.Id<SessionTimeSyncPacket> ID = new CustomPayload.Id<>(SESSION_TIME_SYNC);
    public static final PacketCodec<RegistryByteBuf, SessionTimeSyncPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, SessionTimeSyncPacket::time, SessionTimeSyncPacket::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(ServerPlayerEntity player, int time) {
        ServerPlayNetworking.send(player, new SessionTimeSyncPacket(time));
    }

    public static void receive(SessionTimeSyncPacket payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> CountdownOverlay.setSessionTicksLeft(payload.time()));
    }
}
