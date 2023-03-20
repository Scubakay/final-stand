package scubakay.finalstand.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import scubakay.finalstand.FinalStand;

public class ModItemGroup {
    public static final ItemGroup FINAL_STAND = FabricItemGroup.builder(new Identifier(FinalStand.MOD_ID, "final_stand"))
            .displayName(Text.literal("Final Stand"))
            .icon(() -> new ItemStack(ModItems.HUNTER_TRACKING_DEVICE))
            .entries(((enabledFeatures, entries, operatorEnabled) -> {
                entries.add(ModItems.HUNTER_TRACKING_DEVICE);
            }))
            .build();
}
