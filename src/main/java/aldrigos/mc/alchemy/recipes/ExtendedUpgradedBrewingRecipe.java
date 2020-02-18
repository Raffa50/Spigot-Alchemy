package aldrigos.mc.alchemy.recipes;

import aldrigos.mc.alchemy.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.*;

public class ExtendedUpgradedBrewingRecipe implements BrewingRecipe {
    private transient final Alchemy api;
    private final static ItemStack ingredient = new ItemStack(Material.GOLD_NUGGET);

    public ExtendedUpgradedBrewingRecipe(Alchemy api){
        this.api = api;
    }

    @Override
    public ItemStack getIngredient() {
        return ingredient;
    }

    @Override
    public boolean matches(@NotNull ItemStack ingredient, @NotNull ItemStack toBrew) {
        if(ingredient.getType() != Material.GOLD_NUGGET || toBrew.getType() != Material.POTION || !toBrew.hasItemMeta())
            return false;

        var pot = (PotionMeta) toBrew.getItemMeta();
        var pd = pot.getBasePotionData();

        return !pot.hasCustomEffects() && (pd.isUpgraded() ^ pd.isExtended()); //supporting only basic potion extension for now
    }

    @Override
    public ItemStack brew(@NotNull ItemStack toBrew) {
        var pot = (PotionMeta) toBrew.getItemMeta();
        var pd = pot.getBasePotionData();
        return /*pot.hasCustomEffects() ?
            api.createExtendedUpgradedPotion(Utils.toArray(pot.getCustomEffects())) :*/
            api.createExtendedUpgradedPotion(pd.getType());
    }

    @Override
    public String toString(){ return ExtendedUpgradedBrewingRecipe.class.getName(); }
}
