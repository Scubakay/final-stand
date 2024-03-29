package scubakay.finalstand.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import scubakay.finalstand.FinalStand;

/**
 * <a href="https://fabricmc.net/wiki/tutorial:persistent_states">...</a>
 */
public class SessionState extends PersistentState {
    public static final String IN_SESSION_NBT = "inSession";
    public static final String HUNTER_TICK_NBT = "hunterTick";
    public static final String HUNTERS_ANNOUNCED_NBT = "huntersAnnounced";
    public static final String HUNTERS_SELECTED_NBT = "huntersSelected";
    public static final String CHEST_TICK_NBT = "chestTick";
    public static final String CHEST_ANNOUNCED_NBT = "chestAnnounced";
    public static final String SESSION_TICK_NBT = "sessionTick";
    public static final String SESSION_END_ANNOUNCED_NBT = "sessionEndAccounced";
    public static final String IS_SESSION_PAUSED = "isSessionPaused";

    public boolean inSession = false;
    public int hunterTicksLeft = -1;
    public boolean huntersAnnounced = false;
    public boolean shouldSelectHunters = false;
    public int[] chestTicksLeft = {};
    public int[] announcedChests = {};
    public int sessionTicksLeft = -1;
    public boolean sessionEndAnnounced = false;
    public boolean sessionPaused = false;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean(IN_SESSION_NBT, inSession);
        nbt.putInt(HUNTER_TICK_NBT, hunterTicksLeft);
        nbt.putBoolean(HUNTERS_ANNOUNCED_NBT, huntersAnnounced);
        nbt.putBoolean(HUNTERS_SELECTED_NBT, shouldSelectHunters);
        nbt.putIntArray(CHEST_TICK_NBT, chestTicksLeft);
        nbt.putIntArray(CHEST_ANNOUNCED_NBT, announcedChests);
        nbt.putInt(SESSION_TICK_NBT, sessionTicksLeft);
        nbt.putBoolean(SESSION_END_ANNOUNCED_NBT, sessionEndAnnounced);
        nbt.putBoolean(IS_SESSION_PAUSED, sessionPaused);
        return nbt;
    }

    public static SessionState createFromNbt(NbtCompound tag) {
        SessionState sessionState = new SessionState();
        sessionState.inSession = tag.getBoolean(IN_SESSION_NBT);
        sessionState.hunterTicksLeft = tag.getInt(HUNTER_TICK_NBT);
        sessionState.huntersAnnounced = tag.getBoolean(HUNTERS_ANNOUNCED_NBT);
        sessionState.shouldSelectHunters = tag.getBoolean(HUNTERS_SELECTED_NBT);
        sessionState.chestTicksLeft = tag.getIntArray(CHEST_TICK_NBT);
        sessionState.announcedChests = tag.getIntArray(CHEST_ANNOUNCED_NBT);
        sessionState.sessionTicksLeft = tag.getInt(SESSION_TICK_NBT);
        sessionState.sessionEndAnnounced = tag.getBoolean(SESSION_END_ANNOUNCED_NBT);
        sessionState.sessionPaused = tag.getBoolean(IS_SESSION_PAUSED);
        return sessionState;
    }

    public static SessionState getServerState(MinecraftServer server) {
        // First we get the persistentStateManager for the OVERWORLD
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
        // You need to use a unique string as the key. You should already have a MODID variable defined by you somewhere in your code. Use that.
        return persistentStateManager.getOrCreate(SessionState::createFromNbt, SessionState::new, FinalStand.MOD_ID);
    }
}
