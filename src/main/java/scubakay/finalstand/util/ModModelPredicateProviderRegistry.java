package scubakay.finalstand.util;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.item.ModItems;
import scubakay.finalstand.item.custom.HunterTrackingDevice;

public class ModModelPredicateProviderRegistry {
    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(ModItems.HUNTER_TRACKING_DEVICE, new Identifier("scanning"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity != null && itemStack.getItem() instanceof HunterTrackingDevice item) {
                return item.wasUsed() ? 1 : 0;
            } else {
                return 0.0F;
            }
        });

        ModelPredicateProviderRegistry.register(ModItems.HUNTER_TRACKING_DEVICE, new Identifier("scan"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity != null && itemStack.getItem() instanceof HunterTrackingDevice item) {
                return item.getAnimationCycle();
            } else {
                return 0.0F;
            }
        });

        System.out.printf("[%s] Registering gamerules", FinalStand.MOD_ID);
    }
}
