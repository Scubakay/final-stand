package scubakay.laststand.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import scubakay.laststand.LastStand;

public class ModItemGroup {
    public static final ItemGroup HUNTER_TRACKING_DEVICE = FabricItemGroup.builder(new Identifier(LastStand.MOD_ID, "hunter_tracking_device"))
            .displayName(Text.literal("Last Stand"))
            .icon(() -> new ItemStack(ModItems.HUNTER_TRACKING_DEVICE))
            .entries(((enabledFeatures, entries, operatorEnabled) -> {
                entries.add(ModItems.HUNTER_TRACKING_DEVICE);
            }))
            .build();
}
