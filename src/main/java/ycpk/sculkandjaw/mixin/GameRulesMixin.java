package ycpk.sculkandjaw.mixin;

import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkandjaw.world.level.ModGameRules;

@Mixin(GameRules.class)
public abstract class GameRulesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onStaticInit(CallbackInfo ci) {
        ModGameRules.RULE_SCULK_ACID_SOURCE_CONVERSION = GameRuleAccessor.invokeRegisterBoolean("sculk_acid_source_conversion", GameRuleCategory.UPDATES, false);
    }
}
