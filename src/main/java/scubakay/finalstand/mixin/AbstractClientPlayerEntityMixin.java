package scubakay.finalstand.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import scubakay.finalstand.util.IAbstractClientPlayerEntityMixin;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin implements IAbstractClientPlayerEntityMixin {
    @Shadow()
    protected abstract PlayerListEntry getPlayerListEntry();

    @Override
    public boolean isSurvival() {
        PlayerListEntry playerListEntry = this.getPlayerListEntry();
        return playerListEntry != null && playerListEntry.getGameMode() == GameMode.SURVIVAL;
    }
}
