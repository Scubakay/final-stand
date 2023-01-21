package scubakay.laststand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.laststand.util.IEntityDataSaver;
import scubakay.laststand.data.LivesData;

public class PlayerRespawnEvent implements ServerPlayerEvents.AfterRespawn {
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        removeLife((IEntityDataSaver) oldPlayer, (IEntityDataSaver) newPlayer);
    }

    private static void removeLife(IEntityDataSaver oldPlayer, IEntityDataSaver newPlayer) {
        int lives = oldPlayer.getPersistentData().getInt("lives");
        LivesData.setLives(newPlayer, lives);
    }
}
