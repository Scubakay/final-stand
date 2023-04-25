package scubakay.finalstand.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.data.SessionState;
import scubakay.finalstand.event.handler.*;
import scubakay.finalstand.networking.ModMessages;


public class ModEvents {
    public static void registerClientEvents() {
        ClientPlayConnectionEvents.JOIN.register(new ClientPlayConnectionJoin());
    }
    public static void registerServerEvents() {
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerRespawnEvent());
        ServerLivingEntityEvents.AFTER_DEATH.register(new SwitchGamemodeOnLastDeath());
        ServerLivingEntityEvents.ALLOW_DEATH.register(new CompleteBountyHunt());
        ServerPlayConnectionEvents.INIT.register(new SyncHunterTrackingDeviceCooldownOnJoin());
        ServerLifecycleEvents.SERVER_STARTED.register(new CreateTeamsOnServerStart());
        ServerTickEvents.START_SERVER_TICK.register(new SessionHandler());

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // You can see we use the function getServer() that's on the player.
            SessionState serverState = SessionState.getServerState(handler.player.world.getServer());

            // Sending the packet to the player (look at the networking page for more information)
            PacketByteBuf data = PacketByteBufs.create();
            data.writeInt(serverState.sessionTick);
            ServerPlayNetworking.send(handler.player, ModMessages.SESSION_TIME_SYNC, data);
        });

        System.out.printf("[%s] Registering events\n", FinalStand.MOD_ID);
    }
}
