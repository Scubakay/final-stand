package scubakay.laststand.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import scubakay.laststand.LastStand;
import scubakay.laststand.item.custom.HunterTrackingDevice;

public class ModItems {
    public static final Item HUNTER_TRACKING_DEVICE =
            registerItem("hunter_tracking_device", new HunterTrackingDevice(
                    new FabricItemSettings()
                            .maxCount(1)
            ));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(LastStand.MOD_ID, name), item);
    }

    public static void registerModItems() {
        System.out.printf("[%s] Registering items", LastStand.MOD_ID);
    }
}
