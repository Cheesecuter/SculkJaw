package ycpk.sculkandjaw.world.item.alchemy;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class PotionBrewingRecipe implements IBrewingRecipe {
    private final Potion inputPotion;
    private final Item ingredient;
    private final Potion outputPotion;

    public PotionBrewingRecipe(Potion inputPotion, Item ingredient, Potion outputPotion) {
        this.inputPotion = inputPotion;
        this.ingredient = ingredient;
        this.outputPotion = outputPotion;
    }

    @Override
    public boolean isInput(ItemStack inputPotion) {
        return inputPotion.is(Items.POTION) && PotionUtils.getPotion(inputPotion) == this.inputPotion;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.is(this.ingredient);
    }

    @Override
    public ItemStack getOutput(ItemStack inputPotion, ItemStack ingredient) {
        if (!isInput(inputPotion) || !isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }
        return PotionUtils.setPotion(
                new ItemStack(Items.POTION),
                outputPotion
        );
    }
}
