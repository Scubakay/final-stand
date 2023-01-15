package scubakay.laststand.util;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import scubakay.laststand.LastStand;
import scubakay.laststand.commands.EndSessionCommand;
import scubakay.laststand.commands.InitSessionCommand;
import scubakay.laststand.commands.ResetSessionCommand;
import scubakay.laststand.commands.StartSessionCommand;

public class ModCommandRegister {
    public static void registerCommands() {
        System.out.printf("[%s] Registering commands", LastStand.MOD_ID);
        CommandRegistrationCallback.EVENT.register(StartSessionCommand::register);
        CommandRegistrationCallback.EVENT.register(ResetSessionCommand::register);
        CommandRegistrationCallback.EVENT.register(EndSessionCommand::register);
        CommandRegistrationCallback.EVENT.register(InitSessionCommand::register);
    }
}
