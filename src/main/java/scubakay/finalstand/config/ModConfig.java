package scubakay.finalstand.config;

public class ModConfig {
    public static class Lives {
        public static int min = 3;
        public static int max = 6;
    }

    public static class Hunters {
        public static int min = 1;
        public static int max = 3;
        public static boolean preventRedLifeTarget = true;
        public static boolean preventRedLifeHunter = true;
        public static boolean bountyReward = false;
        public static int hunterTrackingDeviceCooldown = 600;
        public static boolean announceBeingHunted = false;
    }

    public static class Session {
        public static int duration = 120;
        public static int hunterSelectionTime = 10;
        public static int[] treasureChestTimes = {15, 60};
    }
}
