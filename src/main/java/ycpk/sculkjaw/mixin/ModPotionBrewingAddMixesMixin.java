package ycpk.sculkjaw.mixin;

import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkjaw.registry.ModItems;
import ycpk.sculkjaw.world.item.alchemy.ModPotions;

@Mixin(PotionBrewing.class)
public class ModPotionBrewingAddMixesMixin {
    @Inject(at = @At(value = "TAIL"), method = "addVanillaMixes")
    private static void addModMixes(PotionBrewing.Builder builder, CallbackInfo cir) {
        builder.addMix(Potions.WATER, ModItems.SCULK_JAW, ModPotions.SCULK_ACID);
    }
}
