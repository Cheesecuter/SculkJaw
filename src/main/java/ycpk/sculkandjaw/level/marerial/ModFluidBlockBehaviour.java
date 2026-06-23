package ycpk.sculkandjaw.level.marerial;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface ModFluidBlockBehaviour {
    public void entityInside(Level level, BlockPos blockPos, Entity entity);
}
