package com.ycpk.sculkandjaw.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.BooleanValue.class)
public interface GameRulesBooleanValueInvoker {
    @Invoker("create")
    public static GameRules.Type<GameRules.BooleanValue> invokeCreate(boolean bl) {
        throw new AssertionError();
    }
}
