package scubakay.finalstand.event;

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
        HotbarRenderCallback.EVENT.register(new LivesHudOverlay());
        HotbarRenderCallback.EVENT.register(new CountdownOverlay());

        FinalStand.LOGGER.info("Registering client events");
    }
    public static void registerServerEvents() {
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerRespawnEvent());
        ServerLivingEntityEvents.START_AFTER_DEATH.register(new PlayerDeathHandlerAfterDeath());
        ServerPlayConnectionEvents.INIT.register(new SyncSessionTimeOnJoin());
        ServerPlayConnectionEvents.JOIN.register(new SyncLivesOnJoin());
        ServerLifecycleEvents.SERVER_STARTED.register(new CreateTeamsOnServerStart());
        ServerTickEvents.START_SERVER_TICK.register(new SessionHandler());

        FinalStand.LOGGER.info("Registering server events");
    }
}
