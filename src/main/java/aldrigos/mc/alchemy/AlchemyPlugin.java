package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.listeners.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

public class AlchemyPlugin extends JavaPlugin {
    private Recipe getFusePotionRecipe(){
        var r = new ShapelessRecipe(
            new NamespacedKey(this, "fusedpotion"),
            new ItemStack(Material.POTION)
        );
        r.addIngredient(2, Material.POTION);

        return r;
    }

    @Override
    public void onEnable(){
        getServer().addRecipe(getFusePotionRecipe());

        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
    }
}
