package aldrigos.mc.alchemy.recipes;

import org.bukkit.inventory.ItemStack;

public interface BrewingRecipe {
    ItemStack getIngredient();
    boolean matches(ItemStack ingredient, ItemStack toBrew);
    ItemStack brew(ItemStack toBrew);
}
