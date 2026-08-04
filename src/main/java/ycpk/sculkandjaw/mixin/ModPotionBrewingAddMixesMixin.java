package ycpk.sculkandjaw.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkandjaw.registry.ModItems;
import ycpk.sculkandjaw.world.item.alchemy.ModPotions;

@Mixin(PotionBrewing.class)
public abstract class ModPotionBrewingAddMixesMixin {
    @Shadow
    public static void addMix(Potion potion, Item item, Potion potion2) {
    }

    @Inject(at = @At(value = "TAIL"), method = "bootStrap")
    private static void addModMixes(CallbackInfo cir) {
        addMix(Potions.AWKWARD, ModItems.SCULK_JAW, ModPotions.ACID_ETCHING);
        addMix(ModPotions.ACID_ETCHING, Items.REDSTONE, ModPotions.LONG_ACID_ETCHING);
        addMix(ModPotions.ACID_ETCHING, Items.GLOWSTONE_DUST, ModPotions.STRONG_ACID_ETCHING);
    }
}
