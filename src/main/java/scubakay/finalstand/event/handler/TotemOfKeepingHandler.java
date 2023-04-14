package scubakay.finalstand.event.handler;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import scubakay.finalstand.item.ModItems;

public class TotemOfKeepingHandler implements ServerPlayerEvents.CopyFrom  {
    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        handleTotemOfKeeping(oldPlayer, newPlayer);
    }

    private static void handleTotemOfKeeping(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer) {
        if (oldPlayer.getStackInHand(Hand.OFF_HAND).isOf(ModItems.TOTEM_OF_KEEPING) || oldPlayer.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.TOTEM_OF_KEEPING)) {
            // Remove item
            if (oldPlayer.getStackInHand(Hand.OFF_HAND).isOf(ModItems.TOTEM_OF_KEEPING)) {
                oldPlayer.getStackInHand(Hand.OFF_HAND).setCount(0);
            } else {
                oldPlayer.getStackInHand(Hand.MAIN_HAND).setCount(0);
            }

            newPlayer.getInventory().clone(oldPlayer.getInventory());
        }
    }
}
