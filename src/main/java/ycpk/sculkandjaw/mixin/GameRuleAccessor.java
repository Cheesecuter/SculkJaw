package ycpk.sculkandjaw.mixin;

import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.class)
public interface GameRuleAccessor {
    @Invoker("registerBoolean")
    public static GameRule<Boolean> invokeRegisterBoolean(String string, GameRuleCategory gameRuleCategory, boolean bl) {
        throw new AssertionError();
    }
}
