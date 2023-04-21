package scubakay.finalstand.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public interface IPlayerEntityMixin {
    ItemStack getStackInHand(Hand hand);
}
