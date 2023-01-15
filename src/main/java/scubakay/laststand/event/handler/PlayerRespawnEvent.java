package scubakay.laststand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.laststand.util.IEntityDataSaver;
import scubakay.laststand.util.LivesData;

public class PlayerRespawnEvent implements ServerPlayerEvents.AfterRespawn {
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        int lives = ((IEntityDataSaver) oldPlayer).getPersistentData().getInt("lives");
        LivesData.setLives(((IEntityDataSaver) newPlayer), lives);
    }
}
