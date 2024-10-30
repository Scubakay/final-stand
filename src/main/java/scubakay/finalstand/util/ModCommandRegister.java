package scubakay.finalstand.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.commands.*;

public class ModCommandRegister {
    public static void registerCommands() {
        FinalStand.LOGGER.info("Registering commands");
        CommandRegistrationCallback.EVENT.register(SessionCommand::register);
        CommandRegistrationCallback.EVENT.register(PlaceChestCommand::register);
        CommandRegistrationCallback.EVENT.register(LivesCommand::register);
    }
}
