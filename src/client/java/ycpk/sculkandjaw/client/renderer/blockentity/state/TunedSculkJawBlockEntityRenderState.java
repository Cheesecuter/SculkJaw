package ycpk.sculkandjaw.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TunedSculkJawBlockEntityRenderState extends BlockEntityRenderState {
    @Nullable
    public ItemStackRenderState filterItem = new ItemStackRenderState();

    public TunedSculkJawBlockEntityRenderState() {
    }
}
