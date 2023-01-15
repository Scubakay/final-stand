package scubakay.laststand.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.core.jmx.Server;
import scubakay.laststand.LastStand;
import scubakay.laststand.networking.ModMessages;

import java.util.Random;

public class LivesData {
    public static int addLives(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.getPersistentData();
        int lives = nbt.getInt("lives");

        if(lives + amount >= LastStand.MAX_LIVES) {
            lives = LastStand.MAX_LIVES;
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

        Random random = new Random();
        int lives = random.nextInt(LastStand.MAX_LIVES);

        if(lives < LastStand.MIN_LIVES) {
            lives = LastStand.MIN_LIVES;
        }

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
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(lives);
        ServerPlayNetworking.send(player, ModMessages.LIVES_SYNC, buffer);
    }
}
