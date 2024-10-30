package scubakay.finalstand.util;

import net.minecraft.util.Identifier;
import scubakay.finalstand.FinalStand;

public class ModLootTables {
    public static final Identifier FINAL_STAND_TREASURE_CHEST = registerLootTable("chests/final_stand_treasure_chest");

    private static Identifier registerLootTable(String name) {
        return Identifier.of(FinalStand.MOD_ID, name);
    }

    public static void registerLootTables() {
        FinalStand.LOGGER.info("Registering loot tables");
    }
}
