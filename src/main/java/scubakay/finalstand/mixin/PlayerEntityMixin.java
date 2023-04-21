package scubakay.finalstand.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scubakay.finalstand.data.LivesData;
import scubakay.finalstand.item.ModItems;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.util.IPlayerEntityMixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerEntityMixin {
    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;dropInventory()V", shift = At.Shift.BY, by = 1), cancellable = true)
    private void injectTotemOfKeepingHandler(CallbackInfo ci) {
        if (LivesData.getLives((IEntityDataSaver) this) > 1 && (this.getStackInHand(Hand.OFF_HAND).isOf(ModItems.TOTEM_OF_KEEPING) || this.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.TOTEM_OF_KEEPING))) {
            ci.cancel();
        }
    }
}
