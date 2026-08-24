package ycpk.sculkandjaw.client.gui.screens.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import ycpk.sculkandjaw.SculkAndJaw;
import ycpk.sculkandjaw.world.inventory.SculkTransporterMenu;

@Environment(EnvType.CLIENT)
public class SculkTransporterScreen extends AbstractContainerScreen<SculkTransporterMenu> {
    private static final Identifier SCULK_TRANSPORTER_LOCATION = Identifier.fromNamespaceAndPath(SculkAndJaw.MOD_ID, "textures/gui/container/sculk_transporter.png");

    public SculkTransporterScreen(SculkTransporterMenu sculkTransporterMenu, Inventory inventory, Component component) {
        super(sculkTransporterMenu, inventory, component);
    }

    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        this.renderTooltip(guiGraphics, i, j);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SCULK_TRANSPORTER_LOCATION, k, l, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}
