package scubakay.laststand.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import scubakay.laststand.LastStand;
import scubakay.laststand.event.handler.ClientPlayConnectionJoin;
import scubakay.laststand.event.handler.CompleteBountyHunt;
import scubakay.laststand.event.handler.PlayerRespawnEvent;
import scubakay.laststand.event.handler.SwitchGamemodeOnLastDeath;

public class ModEvents {
    public static void registerEvents() {
        ClientPlayConnectionEvents.JOIN.register(new ClientPlayConnectionJoin());
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerRespawnEvent());
        ServerLivingEntityEvents.AFTER_DEATH.register(new SwitchGamemodeOnLastDeath());
        ServerLivingEntityEvents.AFTER_DEATH.register(new CompleteBountyHunt());

        System.out.printf("[%s] Registering events%n", LastStand.MOD_ID);
    }
}
