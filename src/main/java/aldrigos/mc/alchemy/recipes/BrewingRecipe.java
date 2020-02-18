package aldrigos.mc.alchemy.recipes;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.*;

public interface BrewingRecipe {
    @NotNull ItemStack getIngredient();
    boolean matches(@NotNull ItemStack ingredient, @NotNull ItemStack toBrew);
    @NotNull ItemStack brew(@NotNull ItemStack toBrew);
}
