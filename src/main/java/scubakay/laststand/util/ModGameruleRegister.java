package scubakay.laststand.util;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;
import scubakay.laststand.LastStand;

public class ModGameruleRegister {
    public static final GameRules.Key<GameRules.IntRule> HUNTER_AMOUNT = GameRuleRegistry.register("hunterAmount", Category.MISC, GameRuleFactory.createIntRule(1, 1));
    public static final GameRules.Key<GameRules.IntRule> HUNTER_TRACKING_DEVICE_COOLDOWN = GameRuleRegistry.register("hunterTrackingDeviceCooldown", Category.MISC, GameRuleFactory.createIntRule(1000, 1));

    public static void registerGamerules() {
        System.out.printf("[%s] Registering gamerules", LastStand.MOD_ID);
    }
}
