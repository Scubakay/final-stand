package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import scubakay.finalstand.data.TeamState;

public class CreateTeamsOnServerStart implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        TeamState.createTeams(server.getScoreboard());
    }
}
