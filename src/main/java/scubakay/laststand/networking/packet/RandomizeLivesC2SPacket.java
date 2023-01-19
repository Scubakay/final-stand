package scubakay.laststand.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import scubakay.laststand.util.IEntityDataSaver;
import scubakay.laststand.util.LivesData;

public class RandomizeLivesC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity ignoredPlayer, ServerPlayNetworkHandler ignoredHandler, PacketByteBuf ignoredBuf, PacketSender ignoredResponseSender) {
        server.getPlayerManager().getPlayerList().stream().filter(p -> !p.isSpectator() && !p.isCreative()).forEach(serverPlayer -> {
            int lives = LivesData.randomizeLives((IEntityDataSaver) serverPlayer);
            serverPlayer.sendMessage(Text.translatable("item.laststand.amount_of_lives", lives));
        });
    }
}
