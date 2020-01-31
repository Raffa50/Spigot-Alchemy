package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.recipes.ExtendedUpgradedBrewingRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.*;

public final class Alchemy {
    public final AlchemyDb db;

    Alchemy(){
        db = new AlchemyDb();
        db.recipes.add(new ExtendedUpgradedBrewingRecipe(this));
    }

    public void startBrewing(BrewerInventory inv, Plugin p){
        var recipe = db.getRecipe(inv);
        if(recipe == null)
            return;

        new BrewClock(recipe, inv).start(p);
    }

    public ItemStack createFusedPotion(Iterable<ItemStack> potions){
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);

        for(var p: potions){
            var fuse = (PotionMeta) p.getItemMeta();
            var pd = fuse.getBasePotionData();
            var eff = pd.getType().getEffectType();
            int duration = db.getDuration(eff, pd.isExtended(), pd.isUpgraded());

            if( eff != null)
                meta.addCustomEffect(
                    new PotionEffect(eff, duration, pd.isUpgraded() ? 1 : 0),
                true);

            for(var ce: fuse.getCustomEffects())
                meta.addCustomEffect(ce, true);
        }

        meta.setBasePotionData(new PotionData(PotionType.THICK));
        var res = new ItemStack(Material.POTION);
        res.setItemMeta(meta);
        return res;
    }

    private void setEffects(PotionMeta meta, PotionEffect... effects){
        for(var eff: effects)
            meta.addCustomEffect(
                getExtendedUpgradedEffect(eff.getType()),
                true
            );
    }

    private PotionEffect getExtendedUpgradedEffect(PotionEffectType potionEffType){
        return new PotionEffect(
                potionEffType,
                db.getDuration(potionEffType, true, true),
                1);
    }

    public ItemStack createExtendedUpgradedPotion(PotionType potionType){
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        //meta.setBasePotionData(new PotionData(potionType));
        meta.setBasePotionData(new PotionData(PotionType.THICK));
        meta.setColor(potionType.getEffectType().getColor());
        meta.setDisplayName(potionType.getEffectType().getName());
        setEffects(meta, getExtendedUpgradedEffect(potionType.getEffectType()));

        var item = new ItemStack(Material.POTION);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createExtendedUpgradedPotion(PotionEffect... effects){
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        meta.setBasePotionData(new PotionData(PotionType.THICK));
        setEffects(meta, effects);

        var res = new ItemStack(Material.POTION);
        res.setItemMeta(meta);
        return res;
    }
}
