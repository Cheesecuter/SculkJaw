package ycpk.sculkandjaw.world.food;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public ModFoods() {
    }

    public static final FoodProperties ANTACID_DROPLET = (new FoodProperties.Builder())
            .nutrition(2)
            .saturationModifier(0.2F)
            .alwaysEdible()
            .build();
}
