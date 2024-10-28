package scubakay.finalstand;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scubakay.finalstand.config.ModConfig;
import scubakay.finalstand.event.ModEvents;
import scubakay.finalstand.item.ModItems;
import scubakay.finalstand.networking.ModMessages;
import scubakay.finalstand.sounds.ModSounds;
import scubakay.finalstand.util.ModCommandRegister;
import scubakay.finalstand.util.ModGameruleRegister;
import scubakay.finalstand.util.ModLootTables;

public class FinalStand implements ModInitializer {
    public static final String MOD_ID = "finalstand";

    public static final String COMMAND_ROOT = "fs";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Final Stand");

//        ModConfig config = new ModConfig();
//        config.load();

        ModItems.registerModItems();
        ModSounds.registerSounds();

        ModCommandRegister.registerCommands();
        ModMessages.registerC2SPackets();
        ModGameruleRegister.registerGamerules();
        ModLootTables.registerLootTables();

        ModEvents.registerServerEvents();
    }
}
