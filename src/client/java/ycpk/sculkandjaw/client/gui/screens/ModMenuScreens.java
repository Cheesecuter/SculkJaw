package ycpk.sculkandjaw.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.client.gui.screens.inventory.SculkTransporterScreen;
import ycpk.sculkandjaw.client.gui.screens.inventory.TunedSculkJawScreen;
import ycpk.sculkandjaw.world.inventory.ModMenuType;

@Environment(EnvType.CLIENT)
public class ModMenuScreens {
    public static void registerModMenuScreens(){
        SculkAndJaw.LOGGER.info("Registering Menu Screens for Mod " + SculkAndJaw.MOD_ID);
    }

    public ModMenuScreens() {
    }

    static {
        MenuScreens.register(ModMenuType.SCULK_TRANSPORTER_MENU, SculkTransporterScreen::new);
        MenuScreens.register(ModMenuType.TUNED_SCULK_JAW, TunedSculkJawScreen::new);
    }
}
