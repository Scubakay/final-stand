package scubakay.finalstand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.data.HuntersState;

public class CheckBountyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(FinalStand.COMMAND_ROOT)
                .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                .then(CommandManager.literal("checkBounty")
                    .then(CommandManager.argument("hunter", EntityArgumentType.player())
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                            .executes(ctx -> run(ctx, EntityArgumentType.getPlayer(ctx, "hunter"), EntityArgumentType.getPlayer(ctx, "target")))
                        )
                    )
                )
        );
    }

    public static int run(CommandContext<ServerCommandSource> ignoredContext, ServerPlayerEntity hunter, ServerPlayerEntity target) {
        boolean correctTarget = HuntersState.removeIfBountyCompleted(hunter, target);
        if (!correctTarget) {
            hunter.sendMessage(Text.translatable("session.finalstand.killed_wrong_player").formatted(Formatting.DARK_RED));
        }
        return 1;
    }
}
