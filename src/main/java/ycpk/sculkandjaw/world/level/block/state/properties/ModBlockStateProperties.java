package ycpk.sculkandjaw.world.level.block.state.properties;

import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ModBlockStateProperties {
    public static final EnumProperty<SculkJawBiteState> BITE_STATE;
    public static final EnumProperty<TunedSculkJawIOState> IO_STATE;

    public ModBlockStateProperties() {
    }

    static {
        BITE_STATE = EnumProperty.create("bite_state", SculkJawBiteState.class);
        IO_STATE = EnumProperty.create("io_state", TunedSculkJawIOState.class);
    }
}
