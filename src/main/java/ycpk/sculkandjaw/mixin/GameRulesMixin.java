package ycpk.sculkandjaw.mixin;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.mixin.gamerule.GameRulesBooleanRuleAccessor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkandjaw.world.level.ModGameRules;

import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(GameRules.class)
public abstract class GameRulesMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onStaticInit(CallbackInfo ci) {
        GameRules.Key<GameRules.BooleanValue> key2 = new GameRules.Key("sculkAcidSourceConversion", GameRules.Category.UPDATES);
        GameRulesAccessor.getGameRuleTypes().put(key2, GameRulesBooleanValueInvoker.invokeCreate(false));
        ModGameRules.RULE_SCULK_ACID_SOURCE_CONVERSION = key2;
    }
}
