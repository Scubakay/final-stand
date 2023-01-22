package scubakay.finalstand.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import scubakay.finalstand.networking.ModMessages;
import scubakay.finalstand.util.IEntityDataSaver;
import scubakay.finalstand.util.ModGameruleRegister;

import java.util.Random;

public class LivesData {
    public static int addLives(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int lives = nbt.getInt("lives");

        int maxLives = ((ServerPlayerEntity) player).getWorld().getGameRules().getInt(ModGameruleRegister.MAX_LIVES);
        if(lives + amount >= maxLives) {
            lives = maxLives;
        } else {
            lives += amount;
        }

        nbt.putInt("lives", lives);
        syncLives(lives, (ServerPlayerEntity) player);
        return lives;
    }

    public static int removeLives(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int lives = nbt.getInt("lives");

        lives -= amount;

        if(lives < 0) {
            lives = 0;
        }

        nbt.putInt("lives", lives);
        syncLives(lives, (ServerPlayerEntity) player);
        return lives;
    }

    public static int randomizeLives(IEntityDataSaver player) {
        NbtCompound nbt = player.getPersistentData();

        int lives = determineRandomLives(((ServerPlayerEntity) player).getWorld());

        nbt.putInt("lives", lives);
        syncLives(lives, (ServerPlayerEntity) player);
        return lives;
    }

    public static void setLives(IEntityDataSaver player, int lives) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putInt("lives", lives);
        syncLives(lives, (ServerPlayerEntity) player);
    }

    public static void syncLives(int lives, ServerPlayerEntity player) {
        TeamState.setPlayerTeam(lives, player);

        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(lives);
        ServerPlayNetworking.send(player, ModMessages.LIVES_SYNC, buffer);
    }

    private static int determineRandomLives(World world) {
        int minLives = world.getGameRules().getInt(ModGameruleRegister.MIN_LIVES);
        int maxLives = world.getGameRules().getInt(ModGameruleRegister.MAX_LIVES);

        if(minLives > maxLives) {
            minLives = maxLives;
        }

        Random random = new Random();
        int lives = random.nextInt(maxLives - minLives + 1);
        lives += minLives;

        return lives;
    }
}
