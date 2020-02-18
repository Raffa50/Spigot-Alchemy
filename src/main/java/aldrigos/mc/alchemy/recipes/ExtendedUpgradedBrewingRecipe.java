package aldrigos.mc.alchemy.recipes;

import aldrigos.mc.alchemy.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class ExtendedUpgradedBrewingRecipe implements BrewingRecipe {
    private transient final Alchemy api;
    private final static ItemStack ingredient = new ItemStack(Material.GOLD_NUGGET);

    public ExtendedUpgradedBrewingRecipe(){
        var plugin = (AlchemyPlugin) Bukkit.getPluginManager().getPlugin("Alchemy");
        this.api = plugin.getApi();
    }

    @Override
    public ItemStack getIngredient() {
        return ingredient;
    }

    @Override
    public boolean matches(ItemStack ingredient, ItemStack toBrew) {
        if(ingredient.getType() != Material.GOLD_NUGGET || toBrew.getType() != Material.POTION || !toBrew.hasItemMeta())
            return false;

        var pot = (PotionMeta) toBrew.getItemMeta();
        var pd = pot.getBasePotionData();

        return !pot.hasCustomEffects() && (pd.isUpgraded() ^ pd.isExtended()); //supporting only basic potion extension for now
    }

    @Override
    public ItemStack brew(ItemStack toBrew) {
        var pot = (PotionMeta) toBrew.getItemMeta();
        var pd = pot.getBasePotionData();
        return /*pot.hasCustomEffects() ?
            api.createExtendedUpgradedPotion(Utils.toArray(pot.getCustomEffects())) :*/
            api.createExtendedUpgradedPotion(pd.getType());
    }
}
