package aldrigos.mc.alchemy.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.*;

public class SimpleBrewingRecipe implements BrewingRecipe {
    private final ItemStack ingredient, toBrew, result;
    private final boolean strict;

    public SimpleBrewingRecipe(@NotNull ItemStack ingredient, @NotNull ItemStack toBrew, @NotNull ItemStack result){
        this.ingredient = ingredient;
        this.toBrew = toBrew;
        this.result = result;
        strict = true;
    }

    public SimpleBrewingRecipe(Material mat, @NotNull ItemStack toBrew, @NotNull ItemStack result){
        this.ingredient = new ItemStack(mat);
        this.toBrew = toBrew;
        this.result = result;
        strict = false;
    }

    @Override
    public ItemStack getIngredient() {
        return ingredient;
    }

    @Override
    public boolean matches(@NotNull ItemStack ingredient, @NotNull ItemStack toBrew) {
        if(strict)
            return this.ingredient.isSimilar(ingredient) && this.toBrew.isSimilar(toBrew);
        return this.ingredient.getType() == ingredient.getType() && this.toBrew.isSimilar(toBrew);
    }

    @Override
    public ItemStack brew(@NotNull ItemStack toBrew) {
        return result;
    }

    @Override
    public String toString(){
        return String.format("Ingredient: %s\ntoBrew: %s\nResult: %s", ingredient, toBrew, result);
    }
}
