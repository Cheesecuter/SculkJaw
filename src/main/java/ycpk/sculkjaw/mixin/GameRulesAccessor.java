package ycpk.sculkjaw.mixin;

import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(GameRules.class)
public interface GameRulesAccessor {
    @Accessor("GAME_RULE_TYPES")
    public static Map<GameRules.Key<?>, GameRules.Type<?>> getGameRuleTypes() {
        throw new AssertionError();
    }
    @Accessor("GAME_RULE_TYPES")
    public static void setGameRuleTypes(Map<GameRules.Key<?>, GameRules.Type<?>> gameRuleTypes) {
        throw new AssertionError();
    }
}
