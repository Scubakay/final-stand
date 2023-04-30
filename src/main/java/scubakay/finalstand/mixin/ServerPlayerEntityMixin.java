package scubakay.finalstand.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import scubakay.finalstand.util.IServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements IServerPlayerEntity {
    @Shadow()
    @Final()
    public ServerPlayerInteractionManager interactionManager;

    public boolean fs_isSurvival() {
        return this.interactionManager.getGameMode() == GameMode.SURVIVAL;
    }
}
