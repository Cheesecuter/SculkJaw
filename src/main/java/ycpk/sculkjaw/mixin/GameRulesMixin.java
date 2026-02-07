package ycpk.sculkjaw.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkjaw.world.level.ModGameRules;

@Mixin(GameRules.class)
public abstract class GameRulesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onStaticInit(CallbackInfo ci) {
        GameRules.Key<GameRules.BooleanValue> key2 = new GameRules.Key("sculkAcidSourceConversion", GameRules.Category.UPDATES);
        GameRulesAccessor.getGameRuleTypes().put(key2, GameRules.BooleanValue.create(false));
        ModGameRules.RULE_SCULK_ACID_SOURCE_CONVERSION = key2;
    }
}
