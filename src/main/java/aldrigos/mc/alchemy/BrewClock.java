package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.recipes.BrewingRecipe;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

class BrewClock extends BukkitRunnable
{
    private final BrewerInventory inventory;
    private final BrewingRecipe recipe;
    private final BrewingStand stand;
    private int time = 20;

    public BrewClock(BrewingRecipe recipe , BrewerInventory inventory) {
        this.recipe = recipe;
        this.inventory = inventory;
        this.stand = inventory.getHolder();
    }

    public void start(Plugin plugin){
        runTaskTimer(plugin, 1L, 1L);
    }

    @Override
    public void run() {
        if(time <= 0)
        {
            for(int i = 0; i < 3 ; i ++)
            {
                var toBrew = inventory.getItem(i);

                if(toBrew == null || toBrew.getType() == Material.AIR)
                    continue;

                if(recipe.matches(inventory.getIngredient(), toBrew))
                    inventory.setItem(i, recipe.brew(toBrew));
            }
            inventory.setIngredient(new ItemStack(Material.AIR));
            cancel();
            return;
        }
        if(!inventory.getIngredient().isSimilar(recipe.getIngredient()))
        {
            stand.setBrewingTime(400); //Resetting everything
            cancel();
            return;
        }
        //You should also add here a check to make sure that there are still items to brew
        time--;
        stand.setBrewingTime(time);
    }
}
