package scubakay.laststand.util;

import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public abstract class TitleHelper {
    public static void broadcastTitle(List<ServerPlayerEntity> players, Text title) {
        try {
            players.forEach(p -> p.networkHandler.sendPacket(new TitleS2CPacket(title)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
