package scubakay.finalstand;

import net.fabricmc.api.ModInitializer;
import scubakay.finalstand.event.ModEvents;
import scubakay.finalstand.item.ModItems;
import scubakay.finalstand.networking.ModMessages;
import scubakay.finalstand.sounds.ModSounds;
import scubakay.finalstand.util.ModCommandRegister;
import scubakay.finalstand.util.ModGameruleRegister;

public class FinalStand implements ModInitializer {
    public static final String MOD_ID = "finalstand";

    public static final String COMMAND_ROOT = "fs";

    @Override
    public void onInitialize() {
        System.out.println("Initializing Final Stand");

        ModItems.registerModItems();
        ModSounds.registerSounds();

        ModCommandRegister.registerCommands();
        ModMessages.registerC2SPackets();
        ModGameruleRegister.registerGamerules();

        ModEvents.registerServerEvents();
    }
}
