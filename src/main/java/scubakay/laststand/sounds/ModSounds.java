package scubakay.laststand.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import scubakay.laststand.LastStand;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

public class ModSounds {
    public static SoundEvent HUNTER_TRACKING_DEVICE = registerSoundEvent("hunter_tracking_device");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(LastStand.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        System.out.printf("[%s] Registering sounds", LastStand.MOD_ID);
    }
}
