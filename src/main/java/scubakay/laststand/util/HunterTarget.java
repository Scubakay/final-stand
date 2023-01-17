package scubakay.laststand.util;

import net.minecraft.server.network.ServerPlayerEntity;

public class HunterTarget {
    public ServerPlayerEntity hunter;
    public ServerPlayerEntity target;

    public HunterTarget(ServerPlayerEntity hunter, ServerPlayerEntity target) {
        this.hunter = hunter;
        this.target = target;
    }
}
