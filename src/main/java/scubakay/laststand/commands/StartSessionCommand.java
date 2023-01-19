package scubakay.laststand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import scubakay.laststand.LastStand;
import scubakay.laststand.util.HunterTarget;
import scubakay.laststand.util.HuntersState;
import scubakay.laststand.util.IEntityDataSaver;
import scubakay.laststand.util.ModGameruleRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartSessionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredCommandRegistryAccess, CommandManager.RegistrationEnvironment ignoredRegistrationEnvironment) {
        dispatcher.register(
                CommandManager.literal(LastStand.COMMAND_ROOT)
                .requires(source -> source.hasPermissionLevel(4)) // Must be OP to execute
                .then(
                        CommandManager.literal("start")
                                .executes(StartSessionCommand::run)
                )
        );
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        int hunterAmount = context.getSource().getWorld().getGameRules().getInt(ModGameruleRegister.HUNTER_AMOUNT);
        boolean preventRedLifeHunter = context.getSource().getWorld().getGameRules().getBoolean(ModGameruleRegister.PREVENT_RED_LIFE_HUNTER);
        boolean preventRedLifeTarget = context.getSource().getWorld().getGameRules().getBoolean(ModGameruleRegister.PREVENT_RED_LIFE_TARGET);

        List<ServerPlayerEntity> players = context.getSource().getWorld().getPlayers(p -> !p.isSpectator() && !p.isCreative());
        HuntersState.reset(players);
        selectHunters(players, hunterAmount, preventRedLifeHunter, preventRedLifeTarget);
        context.getSource().getServer().getPlayerManager().broadcast(Text.translatable("item.laststand.bounty_hunters_selected"), false);
        return 1;
    }

    /**
     * Select X amount of random hunters
     */
    private static void selectHunters(List<ServerPlayerEntity> players, int amount, boolean preventRedLifeHunter, boolean preventRedLifeTarget) {
        // Prevent red lives from being hunter if preventRedLifeHunter is true
        List<ServerPlayerEntity> possibleHunters = new ArrayList<>(players);
        if(preventRedLifeHunter) {
            possibleHunters = possibleHunters.stream().filter(player -> ((IEntityDataSaver) player).getPersistentData().getInt("lives") > 1).toList();
        }

        // Prevent red life targets if preventRedLifeTarget is true
        List<ServerPlayerEntity> validTargets = new ArrayList<>(players);
        if(preventRedLifeTarget) {
            validTargets = validTargets.stream().filter(player -> ((IEntityDataSaver) player).getPersistentData().getInt("lives") > 1).toList();
        }

        // Select hunters
        Random rand = new Random();
        while(amount > 0 && possibleHunters.size() > 0 && validTargets.size() > 0) {
            // Pick random hunter and target
            int hunterIndex = rand.nextInt(possibleHunters.size());
            ServerPlayerEntity hunter = possibleHunters.get(hunterIndex);
            ServerPlayerEntity target = selectTarget(validTargets, hunter);

            // Don't be hunter if there's no players left with > 1 life
            if(target != null) {
                amount--;
                HuntersState.addHunter(new HunterTarget(hunter, target));
                possibleHunters = possibleHunters.stream().filter(h -> !h.equals(hunter)).toList();
            } else {
                break;
            }
        }
    }

    /**
     * Select a random target for a hunter
     */
    private static ServerPlayerEntity selectTarget(List<ServerPlayerEntity> players, ServerPlayerEntity hunter) {
        List<ServerPlayerEntity> validTargets = new ArrayList<>(players);
        validTargets.remove(hunter);

        // Select a target from valid targets
        Random rand = new Random();
        ServerPlayerEntity target = null;
        while(target == null && validTargets.size() > 0) {
            int targetIndex = rand.nextInt(validTargets.size());
            target = validTargets.get(targetIndex);
        }

        return target;
    }
}
