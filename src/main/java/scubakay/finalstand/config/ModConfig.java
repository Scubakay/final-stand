package scubakay.finalstand.config;

import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;
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
    
    @Transitive
    @ConfigEntries(includeAll = true)
    public static class Lives implements ConfigGroup {
        @ConfigEntry(comment = "The minimum amount of lives")
        @ConfigEntry.BoundedInteger(min = 1)
        public static int min = 3;
        @ConfigEntry(comment = "The maximum amount of lives")
        @ConfigEntry.BoundedInteger(min = 1)
        public static int max = 6;
        
        @Override
        public String getComment() {
            return "The min and max amount of lives";
        }
    }

    @Transitive
    @ConfigEntries(includeAll = true)
    public static class Hunters implements ConfigGroup {
        @ConfigEntry(comment = "The minimum amount of hunters")
        @ConfigEntry.BoundedInteger(min = 1)
        public static int min = 1;
        @ConfigEntry(comment = "The maximum amount of hunters")
        @ConfigEntry.BoundedInteger(min = 1)
        public static int max = 3;
        @ConfigEntry(comment = "Prevent red lives from becoming target")
        public static boolean preventRedLifeTarget = true;
        @ConfigEntry(comment = "Prevent red lives from becoming hunter")
        public static boolean preventRedLifeHunter = true;
        @ConfigEntry(comment = "Reward successful bounty with a life")
        public static boolean bountyReward = false;
        @ConfigEntry(comment = "The cooldown in ticks for the hunter tracking device")
        @ConfigEntry.BoundedInteger(min = 1)
        public static int hunterTrackingDeviceCooldown = 600;
    }

    @Transitive
    @ConfigEntries(includeAll = true)
    public static class Session implements ConfigGroup {
        @ConfigEntry(comment = "The duration of a session in minutes. To have manual session ending, set this to -1")
        @ConfigEntry.BoundedInteger(min = -1)
        public static int duration = 120;
        @ConfigEntry(comment = "The time in minutes after which the hunters will be chosen after session start")
        @ConfigEntry.BoundedInteger(min = 0)
        public static int hunterSelectionTime = 10;
        @ConfigEntry(comment = "The time in minutes after which a treasure chest will be placed after session start")
        public static int[] treasureChestTimes = {15, 60};
    }
}
