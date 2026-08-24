package ycpk.sculkandjaw.world.inventory;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import ycpk.sculkandjaw.SculkAndJaw;

public class ModMenuType<T extends AbstractContainerMenu> implements FeatureElement {
    public static void registerModMenuTypes(){
        SculkAndJaw.LOGGER.info("Registering Menu Types for Mod " + SculkAndJaw.MOD_ID);
    }

    public static final MenuType<SculkTransporterMenu> SCULK_TRANSPORTER_MENU = register("sculk_transporter", SculkTransporterMenu::new);
    public static final MenuType<TunedSculkJawMenu> TUNED_SCULK_JAW = register("tuned_sculk_jaw", TunedSculkJawMenu::new);

    public ModMenuType() {
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String string, MenuType.MenuSupplier<T> menuSupplier) {
        return (MenuType) Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, string), new MenuType(menuSupplier, FeatureFlags.VANILLA_SET));
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String string, MenuType.MenuSupplier<T> menuSupplier, FeatureFlag... featureFlags) {
        return (MenuType) Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, string), new MenuType(menuSupplier, FeatureFlags.REGISTRY.subset(featureFlags)));
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return null;
    }
}
