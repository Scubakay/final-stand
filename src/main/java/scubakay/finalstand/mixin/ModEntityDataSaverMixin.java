package scubakay.finalstand.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scubakay.finalstand.util.IEntityDataSaver;

@Mixin(Entity.class)
public class ModEntityDataSaverMixin implements IEntityDataSaver {
    private final static String NBT_KEY = "finalstand.data";
    private NbtCompound persistentData;
    @Override
    public NbtCompound fs_getPersistentData() {
        if(this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return this.persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if(persistentData != null) {
            nbt.put(NBT_KEY, persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if(nbt.contains(NBT_KEY, 10)) {
            persistentData = nbt.getCompound(NBT_KEY);
        }
    }
}
