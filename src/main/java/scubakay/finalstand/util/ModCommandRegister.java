package scubakay.finalstand.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.commands.EndSessionCommand;
import scubakay.finalstand.commands.InitSessionCommand;
import scubakay.finalstand.commands.ResetSessionCommand;
import scubakay.finalstand.commands.StartSessionCommand;

public class ModCommandRegister {
    public static void registerCommands() {
        System.out.printf("[%s] Registering commands", FinalStand.MOD_ID);
        CommandRegistrationCallback.EVENT.register(StartSessionCommand::register);
        CommandRegistrationCallback.EVENT.register(ResetSessionCommand::register);
        CommandRegistrationCallback.EVENT.register(EndSessionCommand::register);
        CommandRegistrationCallback.EVENT.register(InitSessionCommand::register);
    }
}
