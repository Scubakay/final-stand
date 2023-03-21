package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class TotemOfKeepingHandler implements ServerPlayerEvents.CopyFrom  {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        handleTotemOfKeeping(oldPlayer, newPlayer);
    }

    private static void handleTotemOfKeeping(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        if (oldPlayer.getStackInHand(Hand.OFF_HAND).isOf(Items.PAPER) || oldPlayer.getStackInHand(Hand.MAIN_HAND).isOf(Items.PAPER)) {
            newPlayer.getInventory().clone(oldPlayer.getInventory());
        }
    }
}
