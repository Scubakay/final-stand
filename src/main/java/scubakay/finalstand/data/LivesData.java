package scubakay.finalstand.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import scubakay.finalstand.config.ModConfig;
import scubakay.finalstand.networking.packet.LivesSyncPacket;
import scubakay.finalstand.util.IEntityDataSaver;

import java.util.Random;

public class LivesData {
    public static int addLives(IEntityDataSaver player, int amount) {
        return setLives(player, getLives(player) + amount);
    }

    public static int removeLives(IEntityDataSaver player, int amount) {
        int lives = setLives(player, getLives(player) - amount);
        handleLastLive((ServerPlayerEntity) player, lives);
        return lives;
    }

    public static int randomizeLives(IEntityDataSaver player) {
        return setLives(player, getRandomLives());
    }

    public static int setLives(IEntityDataSaver player, int lives) {
        // Make sure lives doesn't go out of limits
        if (lives > ModConfig.Lives.max) {
            lives = ModConfig.Lives.max;
        } else if(lives < 0) {
            lives = 0;
        }

        // Set nbt & Sync
        NbtCompound nbt = player.fs_getPersistentData();
        nbt.putInt("lives", lives);
        syncLives((ServerPlayerEntity) player);

        // Handle game state stuff
        TeamState.setPlayerTeam(lives, (ServerPlayerEntity) player);

        return lives;
    }

    public static void syncLives(ServerPlayerEntity player) {
        NbtCompound nbt = ((IEntityDataSaver) player).fs_getPersistentData();
        int lives = nbt.getInt("lives");
        LivesSyncPacket.send(player, lives);
    }

    private static int getLives(IEntityDataSaver player) {
        NbtCompound nbt = player.fs_getPersistentData();
        return nbt.getInt("lives");
    }

    private static int getRandomLives() {
        int minLives = ModConfig.Lives.min;
        int maxLives = ModConfig.Lives.max;

        if(minLives > maxLives) {
            minLives = maxLives;
        }

        Random random = new Random();
        int lives = random.nextInt(maxLives - minLives + 1);
        lives += minLives;

        return lives;
    }

    private static void handleLastLive(ServerPlayerEntity player, int lives) {
        if(lives == 0) {
            dropEverything(player);
            setGamemodeToSpectator(player);
            player.sendMessage(Text.translatable("session.finalstand.game_over"));
        } else {
            player.sendMessage(Text.translatable("session.finalstand.lives_left", lives));
        }
    }

    private static void dropEverything(ServerPlayerEntity target) {
        target.getInventory().dropAll();
    }

    private static void setGamemodeToSpectator(ServerPlayerEntity player) {
        player.changeGameMode(GameMode.SPECTATOR);
    }
}
