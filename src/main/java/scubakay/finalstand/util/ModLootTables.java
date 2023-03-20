package scubakay.finalstand.util;

import net.minecraft.util.Identifier;
import scubakay.finalstand.FinalStand;

public class ModLootTables {
    public static final Identifier FINAL_STAND_TREASURE_CHEST = registerLootTable("chests/final_stand_treasure_chest");

    private static Identifier registerLootTable(String name) {
        return new Identifier(FinalStand.MOD_ID, name);
    }

    public static void registerLootTables() {
        System.out.printf("[%s] Registering loot tables", FinalStand.MOD_ID);
    }
}
