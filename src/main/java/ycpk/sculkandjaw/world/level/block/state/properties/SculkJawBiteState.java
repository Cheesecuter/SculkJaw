package ycpk.sculkandjaw.world.level.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum SculkJawBiteState implements StringRepresentable {
    NOT_BITE("not_bite"),
    BEFORE_BITE("before_bite"),
    ON_BITE("on_bite"),
    AFTER_BITE("after_bite");

    private String name;

    private SculkJawBiteState(final String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
