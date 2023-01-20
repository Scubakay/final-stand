package scubakay.laststand.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scubakay.laststand.util.IEntityDataSaver;

@Mixin(ClientWorld.Properties.class)
public class PropertiesMixin {
    @Inject(method = "isHardcore", at = @At("HEAD"), cancellable = true)
    public void isHardcore(CallbackInfoReturnable<Boolean> cir) {
        IEntityDataSaver player = (IEntityDataSaver) MinecraftClient.getInstance().player;
        if(player != null && player.getPersistentData().getInt("lives") == 0) {
            cir.setReturnValue(true);
        }
    }
}
