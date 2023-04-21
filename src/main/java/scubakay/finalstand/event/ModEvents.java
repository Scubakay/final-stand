package scubakay.finalstand.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.event.handler.*;

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
        //ServerPlayerEvents.COPY_FROM.register(new TotemOfKeepingHandler());
        ServerTickEvents.START_SERVER_TICK.register(new SessionHandler());

        System.out.printf("[%s] Registering events%n", FinalStand.MOD_ID);
    }
}
