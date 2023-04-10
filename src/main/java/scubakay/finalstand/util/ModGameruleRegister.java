package scubakay.finalstand.util;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.networking.ModMessages;

import java.util.Iterator;

public class ModGameruleRegister {
    public static final GameRules.Key<GameRules.IntRule> HUNTER_AMOUNT = GameRuleRegistry.register("hunterAmount", Category.MISC, GameRuleFactory.createIntRule(1, 1));
    public static final GameRules.Key<GameRules.IntRule> HUNTER_TRACKING_DEVICE_COOLDOWN = GameRuleRegistry.register("hunterTrackingDeviceCooldown", Category.MISC, GameRuleFactory.createIntRule(1000, 1, (server, rule) -> {
        Iterator<ServerPlayerEntity> var2 = server.getPlayerManager().getPlayerList().iterator();
        int hunterTrackingDeviceCooldown = rule.get();

        while(var2.hasNext()) {
            ServerPlayerEntity serverPlayerEntity = var2.next();
            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeInt(hunterTrackingDeviceCooldown);
            ServerPlayNetworking.send(serverPlayerEntity, ModMessages.SYNC_HUNTER_TRACKING_DEVICE_COOLDOWN, buffer);
        }
    }));
    public static final GameRules.Key<GameRules.IntRule> MAX_LIVES = GameRuleRegistry.register("maxLives", Category.MISC, GameRuleFactory.createIntRule(6, 1));
    public static final GameRules.Key<GameRules.IntRule> MIN_LIVES = GameRuleRegistry.register("minLives", Category.MISC, GameRuleFactory.createIntRule(3, 1));
    public static final GameRules.Key<GameRules.BooleanRule> PREVENT_RED_LIFE_TARGET = GameRuleRegistry.register("preventRedLifeTarget", Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> PREVENT_RED_LIFE_HUNTER = GameRuleRegistry.register("preventRedLifeHunter", Category.MISC, GameRuleFactory.createBooleanRule(true));
    public static final GameRules.Key<GameRules.BooleanRule> BOUNTY_REWARD = GameRuleRegistry.register("bountyReward", Category.MISC, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.IntRule> SESSION_HUNTER_SELECTION_TIME = GameRuleRegistry.register("hunterSelectionTime", Category.MISC, GameRuleFactory.createIntRule(10, 0));
    public static final GameRules.Key<GameRules.IntRule> SESSION_TREASURE_CHEST_TIME = GameRuleRegistry.register("treasureChestTime", Category.MISC, GameRuleFactory.createIntRule(15, 0));
    public static final GameRules.Key<GameRules.IntRule> SESSION_TIME = GameRuleRegistry.register("sessionTime", Category.MISC, GameRuleFactory.createIntRule(120, -1));

    public static void registerGamerules() {
        System.out.printf("[%s] Registering gamerules", FinalStand.MOD_ID);
    }
}
