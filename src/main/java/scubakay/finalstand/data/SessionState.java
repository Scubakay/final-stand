package scubakay.finalstand.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import scubakay.finalstand.FinalStand;

/**
 * https://fabricmc.net/wiki/tutorial:persistent_states
 */
public class SessionState extends PersistentState {
    public static final String IN_SESSION_NBT = "inSession";
    public static final String CHEST_TICK_NBT = "chestTick";
    public static final String CHEST_ANNOUNCED_NBT = "chestAnnounced";
    public static final String SESSION_TICK_NBT = "sessionTick";
    public static final String SESSION_END_ANNOUNCED_NBT = "sessionEndAccounced";
    public static final String IS_SESSION_PAUSED = "isSessionPaused";

    public boolean inSession = false;
    public int[] chestTicksLeft = {};
    public int[] announcedChests = {};
    public int sessionTicksLeft = -1;
    public boolean sessionEndAnnounced = false;
    public boolean sessionPaused = false;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putBoolean(IN_SESSION_NBT, inSession);
        nbt.putIntArray(CHEST_TICK_NBT, chestTicksLeft);
        nbt.putIntArray(CHEST_ANNOUNCED_NBT, announcedChests);
        nbt.putInt(SESSION_TICK_NBT, sessionTicksLeft);
        nbt.putBoolean(SESSION_END_ANNOUNCED_NBT, sessionEndAnnounced);
        nbt.putBoolean(IS_SESSION_PAUSED, sessionPaused);
        return nbt;
    }

    public static SessionState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        SessionState sessionState = new SessionState();
        sessionState.inSession = tag.getBoolean(IN_SESSION_NBT);
        sessionState.chestTicksLeft = tag.getIntArray(CHEST_TICK_NBT);
        sessionState.announcedChests = tag.getIntArray(CHEST_ANNOUNCED_NBT);
        sessionState.sessionTicksLeft = tag.getInt(SESSION_TICK_NBT);
        sessionState.sessionEndAnnounced = tag.getBoolean(SESSION_END_ANNOUNCED_NBT);
        sessionState.sessionPaused = tag.getBoolean(IS_SESSION_PAUSED);
        return sessionState;
    }

    private static Type<SessionState> type = new Type<>(
            SessionState::new, // If there's no 'StateSaverAndLoader' yet create one
            SessionState::createFromNbt, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );

    public static SessionState getServerState(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // The first time the following 'getOrCreate' function is called, it creates a brand new 'StateSaverAndLoader' and
        // stores it inside the 'PersistentStateManager'. The subsequent calls to 'getOrCreate' pass in the saved
        // 'StateSaverAndLoader' NBT on disk to our function 'StateSaverAndLoader::createFromNbt'.
        SessionState state = persistentStateManager.getOrCreate(type, FinalStand.MOD_ID);

        // If state is not marked dirty, when Minecraft closes, 'writeNbt' won't be called and therefore nothing will be saved.
        // Technically it's 'cleaner' if you only mark state as dirty when there was actually a change, but the vast majority
        // of mod writers are just going to be confused when their data isn't being saved, and so it's best just to 'markDirty' for them.
        // Besides, it's literally just setting a bool to true, and the only time there's a 'cost' is when the file is written to disk when
        // there were no actual change to any of the mods state (INCREDIBLY RARE).
        state.markDirty();

        return state;
    }
}
