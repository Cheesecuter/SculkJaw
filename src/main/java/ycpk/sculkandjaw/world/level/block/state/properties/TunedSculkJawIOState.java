package ycpk.sculkandjaw.world.level.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum TunedSculkJawIOState implements StringRepresentable {
    INPUT("input"),
    OUTPUT("output");

    private String name;

    private TunedSculkJawIOState(final String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
