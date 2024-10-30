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
import scubakay.finalstand.client.LivesHudOverlay;

public record LivesSyncPacket(int lives) implements CustomPayload {
    public static final Identifier LIVES_SYNC = new Identifier(FinalStand.MOD_ID, "finalstand.lives_sync");

    public static final CustomPayload.Id<LivesSyncPacket> ID = new CustomPayload.Id<>(LIVES_SYNC);
    public static final PacketCodec<RegistryByteBuf, LivesSyncPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, LivesSyncPacket::lives, LivesSyncPacket::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(ServerPlayerEntity player, int lives) {
        ServerPlayNetworking.send(player, new LivesSyncPacket(lives));
    }

    public static void receive(LivesSyncPacket payload, ClientPlayNetworking.Context context) {
        context.client().execute(() -> LivesHudOverlay.setLives(payload.lives()));
    }
}
