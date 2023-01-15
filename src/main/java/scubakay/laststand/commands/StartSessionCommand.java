package scubakay.laststand.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import scubakay.laststand.LastStand;
import scubakay.laststand.item.ModItems;
import scubakay.laststand.util.ModGameruleRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartSessionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
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
        List<ServerPlayerEntity> players = new ArrayList<>(context.getSource().getWorld().getPlayers());

        int hunterAmount = context.getSource().getWorld().getGameRules().getInt(ModGameruleRegister.HUNTER_AMOUNT);

        // Check if there are enough players
        if (players.size() <= 1 || players.size() < hunterAmount) {
            context.getSource().sendFeedback(Text.literal("Not enough players to start session"), true);
            return 1;
        }

        context.getSource().getServer().getPlayerManager().broadcast(Text.literal("The bounty hunters have been selected!"), false);
        selectHunters(players, hunterAmount);
        return 1;
    }

    /**
     * Select X amount of random hunters
     */
    private static void selectHunters(List<ServerPlayerEntity> players, int amount) {
        Random rand = new Random();

        List<ServerPlayerEntity> hunters = new ArrayList<>();

        while(amount > 0) {
            int hunterIndex = rand.nextInt(players.size());
            ServerPlayerEntity hunter = players.get(hunterIndex);

            if (!hunters.contains(hunter)) {
                hunters.add(hunter);
                amount--;
                ServerPlayerEntity target = selectTarget(players, hunter);
                giveHunterDevice(hunter, target);
            }
        }
    }

    /**
     * Give the hunter a Hunter Tracking Device
     */
    private static void giveHunterDevice(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        ItemStack itemStack = new ItemStack(ModItems.HUNTER_TRACKING_DEVICE);
        itemStack.setCount(1);
        NbtCompound nbtData = new NbtCompound();
        nbtData.putString("target", target.getUuidAsString());
        itemStack.setNbt(nbtData);
        hunter.getInventory().insertStack(itemStack);
        hunter.sendMessage(Text.literal("You have been selected as a bounty hunter. Track down and kill your target!"));
    }

    /**
     * Select a random target for a hunter
     */
    private static ServerPlayerEntity selectTarget(List<ServerPlayerEntity> players, ServerPlayerEntity hunter) {
        // TODO: Use minecraft random?
        Random rand = new Random();

        ServerPlayerEntity target = null;
        players.remove(hunter);

        while(target == null) {
            int targetIndex = rand.nextInt(players.size());
            target = players.get(targetIndex);
        }

        return target;
    }
}
