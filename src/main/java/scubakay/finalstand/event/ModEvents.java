package scubakay.finalstand.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.client.CountdownOverlay;
import scubakay.finalstand.client.LivesHudOverlay;
import scubakay.finalstand.event.callback.HotbarRenderCallback;
import scubakay.finalstand.event.handler.*;


public class ModEvents {
    public static void registerClientEvents() {
        ClientPlayConnectionEvents.JOIN.register(new ClientPlayConnectionJoin());
        HotbarRenderCallback.EVENT.register(new LivesHudOverlay());
        HotbarRenderCallback.EVENT.register(new CountdownOverlay());

        FinalStand.LOGGER.info("Registering client events");
    }
    public static void registerServerEvents() {
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerRespawnEvent());
        ServerLivingEntityEvents.AFTER_DEATH.register(new PlayerDeathHandler());
        ServerPlayConnectionEvents.INIT.register(new SyncHunterTrackingDeviceCooldownOnJoin());
        ServerPlayConnectionEvents.INIT.register(new SyncSessionTimeOnJoin());
        ServerLifecycleEvents.SERVER_STARTED.register(new CreateTeamsOnServerStart());
        ServerTickEvents.START_SERVER_TICK.register(new SessionHandler());

        FinalStand.LOGGER.info("Registering server events");
    }
}
