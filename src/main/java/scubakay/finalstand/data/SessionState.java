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
    private static final String HUNTER_TICK_NBT = "hunterTick";
    private static final String HUNTERS_ANNOUNCED_NBT = "huntersAnnounced";
    private static final String CHEST_TICK_NBT = "chestTick";
    private static final String CHEST_ANNOUNCED_NBT = "chestAnnounced";
    private static final String SESSION_TICK_NBT = "sessionTick";
    private static final String SESSION_END_ANNOUNCED_NBT = "sessionEndAccounced";

    private int hunterTick = -1;
    private boolean huntersAnnounced = false;
    private int chestTick = -1;
    private boolean chestAnnounced = false;
    public int sessionTick = -1;
    private boolean sessionEndAnnounced = false;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt(HUNTER_TICK_NBT, hunterTick);
        nbt.putBoolean(HUNTERS_ANNOUNCED_NBT, huntersAnnounced);
        nbt.putInt(CHEST_TICK_NBT, chestTick);
        nbt.putBoolean(CHEST_ANNOUNCED_NBT, chestAnnounced);
        nbt.putInt(SESSION_TICK_NBT, sessionTick);
        nbt.putBoolean(SESSION_END_ANNOUNCED_NBT, sessionEndAnnounced);
        return null;
    }

    public static SessionState createFromNbt(NbtCompound tag) {
        SessionState sessionState = new SessionState();
        sessionState.hunterTick = tag.getInt(HUNTER_TICK_NBT);
        sessionState.huntersAnnounced = tag.getBoolean(HUNTERS_ANNOUNCED_NBT);
        sessionState.chestTick = tag.getInt(CHEST_TICK_NBT);
        sessionState.chestAnnounced = tag.getBoolean(CHEST_ANNOUNCED_NBT);
        sessionState.sessionTick = tag.getInt(SESSION_TICK_NBT);
        sessionState.sessionEndAnnounced = tag.getBoolean(SESSION_END_ANNOUNCED_NBT);
        return sessionState;
    }

    public static SessionState getServerState(MinecraftServer server) {
        // First we get the persistentStateManager for the OVERWORLD
        PersistentStateManager persistentStateManager = server
                .getWorld(World.OVERWORLD).getPersistentStateManager();

        // Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
        // You need to use a unique string as the key. You should already have a MODID variable defined by you somewhere in your code. Use that.
        return persistentStateManager.getOrCreate(
                SessionState::createFromNbt,
                SessionState::new,
                FinalStand.MOD_ID);
    }
}
