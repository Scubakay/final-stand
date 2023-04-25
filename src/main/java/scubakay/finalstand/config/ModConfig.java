package scubakay.finalstand.config;

import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.data.ConfigOptions;
import scubakay.finalstand.FinalStand;

public class ModConfig extends Config {
    public ModConfig() {
        super(ConfigOptions
                .mod(FinalStand.MOD_ID)
                .fileHeader("Config for the Final Stand Mod")
        );
    }

    // Lives settings
    @ConfigEntry(comment = "The minimum amount of lives")
    @ConfigEntry.BoundedInteger(min = 1)
    private static int minLives = 3;
    @ConfigEntry(comment = "The maximum amount of lives")
    @ConfigEntry.BoundedInteger(min = 1)
    private static int maxLives = 6;

    // Hunter settings
    @ConfigEntry(comment = "Minimum of hunters chosen each session")
    @ConfigEntry.BoundedInteger(min = 1)
    private static int minHunterAmount = 1;
    @ConfigEntry(comment = "Maximum amount of hunters chosen each session")
    @ConfigEntry.BoundedInteger(min = 1)
    private static int maxHunterAmount = 3;
    @ConfigEntry(comment = "Prevent red lives from becoming target")
    private static boolean preventRedLifeTarget = true;
    @ConfigEntry(comment = "Prevent red lives from becoming hunter")
    private static boolean preventRedLifeHunter = true;
    @ConfigEntry(comment = "Reward successful bounty with a life")
    private static boolean bountyReward = false;
    @ConfigEntry(comment = "The cooldown in ticks for the hunter tracking device")
    @ConfigEntry.BoundedInteger(min = 1)
    private static int hunterTrackingDeviceCooldown = 600;

    // Session settings
    @ConfigEntry(comment = "The duration of a session in minutes. To have manual session ending, set this to -1")
    @ConfigEntry.BoundedInteger(min = -1)
    private static int sessionTime = 120;
    @ConfigEntry(comment = "The time in minutes after which the hunters will be chosen after session start")
    @ConfigEntry.BoundedInteger(min = 0)
    private static int sessionHunterSelectionTime = 10;
    @ConfigEntry(comment = "The time in minutes after which a treasure chest will be placed after session start")
    private static int[] sessionTreasureChestTimes = {15, 60};

    public static int getMinLives() {
        return minLives;
    }

    public static int getMaxLives() {
        return maxLives;
    }

    public static int getMinHunterAmount() {
        return minHunterAmount;
    }

    public static int getMaxHunterAmount() {
        return maxHunterAmount;
    }

    public static boolean isPreventRedLifeTarget() {
        return preventRedLifeTarget;
    }

    public static boolean isPreventRedLifeHunter() {
        return preventRedLifeHunter;
    }

    public static boolean isBountyReward() {
        return bountyReward;
    }

    public static int getHunterTrackingDeviceCooldown() {
        return hunterTrackingDeviceCooldown;
    }

    public static int getSessionTime() {
        return sessionTime;
    }

    public static int getSessionHunterSelectionTime() {
        return sessionHunterSelectionTime;
    }

    public static int[] getSessionTreasureChestTimes() {
        return sessionTreasureChestTimes;
    }
}
