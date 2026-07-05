package ycpk.sculkandjaw.client.mixin;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface GuiAccessor {
    @Accessor("lastHealth")
    public int getLastHealth();
    @Accessor("displayHealth")
    public int getDisplayHealth();
    @Accessor("healthBlinkTime")
    public long getHealthBlinkTime();
    @Accessor("tickCount")
    public int getTickCount();
    @Accessor("lastHealthTime")
    public void setLastHealthTime(long time);
    @Accessor("lastHealthTime")
    public long getLastHealthTime();
}
