package ycpk.sculkandjaw.world.level.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import ycpk.sculkandjaw.registry.ModSoundEvents;

public class ModSoundType {
    public static final SoundType SCULK_TELEPORTER;

    public ModSoundType() {
    }

    static {
        SCULK_TELEPORTER = new SoundType(1.0F, 1.0F, ModSoundEvents.SCULK_TELEPORTER_BREAK, SoundEvents.SCULK_BLOCK_STEP, ModSoundEvents.SCULK_TELEPORTER_PLACE, SoundEvents.SCULK_BLOCK_HIT, SoundEvents.SCULK_BLOCK_FALL);
    }
}
