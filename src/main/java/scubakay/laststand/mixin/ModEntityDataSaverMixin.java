package scubakay.laststand.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scubakay.laststand.util.IEntityDataSaver;

@Mixin(Entity.class)
public class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistenData;
    @Override
    public NbtCompound getPersistentData() {
        if(this.persistenData == null) {
            this.persistenData = new NbtCompound();
        }
        return this.persistenData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if(persistenData != null) {
            nbt.put("laststand.data", persistenData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if(nbt.contains("laststand.data", 10)) {
            persistenData = nbt.getCompound("laststand.data");
        }
    }
}
