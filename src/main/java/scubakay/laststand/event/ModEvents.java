package scubakay.laststand.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import scubakay.laststand.LastStand;
import scubakay.laststand.event.handler.*;

public class ModEvents {
    public static void registerEvents() {
        ClientPlayConnectionEvents.JOIN.register(new ClientPlayConnectionJoin());
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerRespawnEvent());
        ServerLivingEntityEvents.AFTER_DEATH.register(new SwitchGamemodeOnLastDeath());
        ServerLivingEntityEvents.AFTER_DEATH.register(new CompleteBountyHunt());
        ServerPlayConnectionEvents.INIT.register(new SyncHunterTrackingDeviceCooldownOnJoin());

        System.out.printf("[%s] Registering events%n", LastStand.MOD_ID);
    }
}
