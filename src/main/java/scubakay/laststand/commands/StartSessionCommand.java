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

        selectHunters(context.getSource().getWorld().getPlayers(), hunterAmount, preventRedLifeHunter, preventRedLifeTarget);
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

        // Select hunters
        Random rand = new Random();
        while(amount > 0 && possibleHunters.size() > 0) {
            // Pick random hunter and target
            int hunterIndex = rand.nextInt(possibleHunters.size());
            ServerPlayerEntity hunter = possibleHunters.get(hunterIndex);
            ServerPlayerEntity target = selectTarget(players, hunter, preventRedLifeTarget);

            // Don't be hunter if there's no players left with > 1 life
            if(target != null) {
                amount--;
                giveHunterDevice(hunter, target);
                possibleHunters = possibleHunters.stream().filter(h -> !h.equals(hunter)).toList();
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
    private static ServerPlayerEntity selectTarget(List<ServerPlayerEntity> players, ServerPlayerEntity hunter, boolean preventRedLifeTarget) {
        // Prevent red life targets if preventRedLifeTarget is true
        List<ServerPlayerEntity> validTargets = new ArrayList<>(players);
        validTargets.remove(hunter);
        if(preventRedLifeTarget) {
            validTargets = validTargets.stream().filter(player -> ((IEntityDataSaver) player).getPersistentData().getInt("lives") > 1).toList();
        }

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
