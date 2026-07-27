package ycpk.sculkandjaw.mixin;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.*;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ycpk.sculkandjaw.util.datafix.schemas.V1051;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

@Mixin(DataFixers.class)
public abstract class DataFixersMixin {
    @Shadow
    @Final
    private static BiFunction<Integer, Schema, Schema> SAME_NAMESPACED;

    @Shadow
    private static UnaryOperator<String> createRenamer(String string, String string2) {
        return (string3) -> {
            return Objects.equals(NamespacedSchema.ensureNamespaced(string3), string) ? string2 : string3;
        };
    }

    @Inject(at = @At(value = "TAIL"), method = "addFixers")
    private static void addModDataFixers(DataFixerBuilder dataFixerBuilder, CallbackInfo cir) {
        Schema modSchema1 = dataFixerBuilder.addSchema(4662, V1051::new);
        dataFixerBuilder.addFixer(new AddNewChoices(modSchema1, "Added sculk aggregator", References.BLOCK_ENTITY));
        Schema modSchema2 = dataFixerBuilder.addSchema(4663, SAME_NAMESPACED);
        dataFixerBuilder.addFixer(BlockRenameFix.create(
                modSchema2,
                "Rename concentrated sculk to sculk aggregator",
                createRenamer("ycpk:concentrated_sculk", "ycpk:sculk_aggregator")
        ));
        dataFixerBuilder.addFixer(ItemRenameFix.create(
                modSchema2,
                "Rename concentrated sculk to sculk aggregator",
                createRenamer("ycpk:concentrated_sculk", "ycpk:sculk_aggregator")
        ));
        dataFixerBuilder.addFixer(BlockEntityRenameFix.create(
                modSchema2,
                "Rename concentrated sculk to sculk aggregator",
                createRenamer("ycpk:concentrated_sculk", "ycpk:sculk_aggregator")
        ));
        Schema modSchema3 = dataFixerBuilder.addSchema(4664, SAME_NAMESPACED);
        dataFixerBuilder.addFixer(BlockRenameFix.create(
                modSchema3,
                "Rename acidophilic cordyceps to acidcoil cattail",
                createRenamer("ycpk:acidophilic_cordyceps", "ycpk:acidcoil_cattail")
        ));
        dataFixerBuilder.addFixer(ItemRenameFix.create(
                modSchema3,
                "Rename acidophilic cordyceps to acidcoil cattail",
                createRenamer("ycpk:acidophilic_cordyceps", "ycpk:acidcoil_cattail")
        ));
    }
}
