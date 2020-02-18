package aldrigos.mc.alchemy.recipes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class SimpleBrewingRecipe implements BrewingRecipe, Serializable {
    private final ItemStack ingredient, toBrew, result;
    private final boolean strict;

    public SimpleBrewingRecipe(ItemStack ingredient, ItemStack toBrew, ItemStack result){
        this.ingredient = ingredient;
        this.toBrew = toBrew;
        this.result = result;
        strict = true;
    }

    public SimpleBrewingRecipe(Material mat, ItemStack toBrew, ItemStack result){
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
    public boolean matches(ItemStack ingredient, ItemStack toBrew) {
        if(strict)
            return this.ingredient.isSimilar(ingredient);
        return this.ingredient.getType() == ingredient.getType();
    }

    @Override
    public ItemStack brew(ItemStack toBrew) {
        return result;
    }
}
