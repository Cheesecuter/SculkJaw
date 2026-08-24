package ycpk.sculkandjaw.world.level.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum TransferAmount implements StringRepresentable {
    ONE("one"),
    HALF_STACK("half_stack"),
    FULL_STACK("full_stack");

    private String name;

    private TransferAmount(final String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
