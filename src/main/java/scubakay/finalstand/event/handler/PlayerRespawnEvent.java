package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.finalstand.data.HuntersState;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.data.LivesData;

public class PlayerRespawnEvent implements ServerPlayerEvents.AfterRespawn {
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        persistLives((IEntityDataSaver) oldPlayer, (IEntityDataSaver) newPlayer);
        HuntersState.persistTarget(oldPlayer, newPlayer);
    }

    private static void persistLives(IEntityDataSaver oldPlayer, IEntityDataSaver newPlayer) {
        int lives = oldPlayer.fs_getPersistentData().getInt("lives");
        LivesData.setLives(newPlayer, lives);
    }
}
