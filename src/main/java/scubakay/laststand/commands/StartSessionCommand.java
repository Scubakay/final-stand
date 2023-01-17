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
import scubakay.laststand.util.HunterTarget;
import scubakay.laststand.util.HuntersState;
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
        List<ServerPlayerEntity> players = new ArrayList<>(context.getSource().getWorld().getPlayers());

        int hunterAmount = context.getSource().getWorld().getGameRules().getInt(ModGameruleRegister.HUNTER_AMOUNT);

        // Check if there are enough players
        if (players.size() <= 1 || players.size() < hunterAmount) {
            context.getSource().sendFeedback(Text.translatable("item.laststand.not_enough_players"), true);
            return 1;
        }

        context.getSource().getServer().getPlayerManager().broadcast(Text.translatable("item.laststand.bounty_hunters_selected"), false);
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
        // Create device
        ItemStack itemStack = new ItemStack(ModItems.HUNTER_TRACKING_DEVICE);
        itemStack.setCount(1);

        // Save target to device NBT
        NbtCompound nbtData = new NbtCompound();
        nbtData.putString("target", target.getUuidAsString());
        itemStack.setNbt(nbtData);

        // Add device to hunter inventory
        hunter.getInventory().insertStack(itemStack);
        hunter.sendMessage(Text.translatable("item.laststand.you_are_hunter"));

        // Add hunter and target to list
        HuntersState.addHunter(new HunterTarget(hunter, target));
    }

    /**
     * Select a random target for a hunter
     */
    private static ServerPlayerEntity selectTarget(List<ServerPlayerEntity> players, ServerPlayerEntity hunter) {
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
