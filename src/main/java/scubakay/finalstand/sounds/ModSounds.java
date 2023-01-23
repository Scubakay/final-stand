package scubakay.finalstand.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import scubakay.finalstand.FinalStand;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class ModSounds {
    public static SoundEvent TRACKING_DEVICE_SEARCHING = registerSoundEvent("tracking_device_searching");
    public static SoundEvent TRACKING_DEVICE_CONFIRM = registerSoundEvent("tracking_device_confirm");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(FinalStand.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        System.out.printf("[%s] Registering sounds", FinalStand.MOD_ID);
    }
}
