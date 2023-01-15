package scubakay.laststand;

import net.fabricmc.api.ModInitializer;
import scubakay.laststand.event.ModEvents;
import scubakay.laststand.item.ModItems;
import scubakay.laststand.networking.ModMessages;
import scubakay.laststand.sounds.ModSounds;
import scubakay.laststand.util.ModCommandRegister;
import scubakay.laststand.util.ModGameruleRegister;

public class LastStand implements ModInitializer {
    public static final String MOD_ID = "laststand";
    public static final int HUNTER_DEVICE_COOLDOWN = 10;

    public static final String COMMAND_ROOT = "ls";

    public static final int MAX_LIVES = 10;
    public static final int MIN_LIVES = 1;

    @Override
    public void onInitialize() {
        System.out.println("Initializing Last Stand");

        ModItems.registerModItems();
        ModSounds.registerSounds();

        ModCommandRegister.registerCommands();
        ModMessages.registerC2SPackets();
        ModMessages.registerS2CPackets();
        ModGameruleRegister.registerGamerules();

        ModEvents.registerEvents();
    }
}
