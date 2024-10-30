package scubakay.finalstand.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import scubakay.finalstand.FinalStand;

public class ModItems {
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(FinalStand.MOD_ID, name), item);
    }

    public static void registerModItems() {
        FinalStand.LOGGER.info("Registering items");
    }
}
