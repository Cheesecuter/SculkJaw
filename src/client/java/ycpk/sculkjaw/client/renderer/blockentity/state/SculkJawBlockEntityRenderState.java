package ycpk.sculkjaw.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

@Environment(EnvType.CLIENT)
public class SculkJawBlockEntityRenderState extends BlockEntityRenderState {
    public int tickCount;
    public boolean hasCombined;

    public SculkJawBlockEntityRenderState() {
    }
}
