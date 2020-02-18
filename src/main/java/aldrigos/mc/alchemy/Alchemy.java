package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.recipes.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.*;
import org.jetbrains.annotations.*;

import java.util.Arrays;

public final class Alchemy {
    public final AlchemyDb db;

    Alchemy(){
        this.db = new AlchemyDb();
        addRecipes(witherPotionRecipe(), new ExtendedUpgradedBrewingRecipe(this));
    }

    public static boolean isEffectPotion(@NotNull ItemStack i){
        return i != null && i.getType() == Material.POTION && i.hasItemMeta();
    }

    public void addRecipes(@NotNull BrewingRecipe... recipes){
        db.recipes.addAll(Arrays.asList(recipes));
    }

    public boolean isIngredient(ItemStack item){
        for(var r: db.recipes){
            if(r.getIngredient().isSimilar(item))
                return true;
        }
        return false;
    }

    public void startBrewing(@NotNull HumanEntity crafter, @NotNull BrewerInventory inv, @NotNull Plugin p){
        var recipe = db.getRecipe(inv);
        if(recipe == null) {
            if(couldCraft(inv))
                crafter.sendMessage(ChatColor.RED+"[Alchemy]No recipes for this combination"+ChatColor.RESET);
            return;
        }

        crafter.sendMessage(ChatColor.GREEN+"[Alchemy]Brewing..."+ChatColor.RESET);
        new BrewClock(recipe, inv).start(p);
    }

    private static boolean couldCraft(@NotNull BrewerInventory inv){
        if(inv.getIngredient() == null || inv.getIngredient().getType() == Material.AIR)
            return false;

        for(int i= 0; i < 3; i++){
            var item = inv.getItem(i);
            if(item != null && item.getType() != Material.AIR)
                return true;
        }

        return false;
    }

    @NotNull
    public ItemStack createFusedPotion(@NotNull Iterable<ItemStack> potions){
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

    private void setEffects(@NotNull PotionMeta meta, @NotNull PotionEffect... effects){
        for(var eff: effects)
            meta.addCustomEffect(
                getExtendedUpgradedEffect(eff.getType()),
                true
            );
    }

    @NotNull
    private PotionEffect getExtendedUpgradedEffect(PotionEffectType potionEffType){
        return new PotionEffect(
                potionEffType,
                db.getDuration(potionEffType, true, true),
                1);
    }

    @NotNull
    public ItemStack createExtendedUpgradedPotion(PotionType potionType){
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        meta.setBasePotionData(new PotionData(PotionType.THICK));
        meta.setColor(potionType.getEffectType().getColor());
        meta.setDisplayName(potionType.getEffectType().getName());
        setEffects(meta, getExtendedUpgradedEffect(potionType.getEffectType()));

        var item = new ItemStack(Material.POTION);
        item.setItemMeta(meta);
        return item;
    }

    @NotNull
    public ItemStack createExtendedUpgradedPotion(@NotNull PotionEffect... effects){
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        meta.setBasePotionData(new PotionData(PotionType.THICK));
        setEffects(meta, effects);

        var res = new ItemStack(Material.POTION);
        res.setItemMeta(meta);
        return res;
    }

    @NotNull
    static BrewingRecipe witherPotionRecipe(){
        var toBrew = new ItemStack(Material.POTION);
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        meta.setBasePotionData(new PotionData(PotionType.AWKWARD));
        toBrew.setItemMeta(meta);

        return new SimpleBrewingRecipe(Material.NETHER_STAR, toBrew, createWitherPotion(40*20));
    }

    @NotNull
    static ItemStack createWitherPotion(int duration){
        var item = new ItemStack(Material.POTION, 3);
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);

        meta.setColor(Color.BLACK);
        meta.setDisplayName("Potion of Decay");
        meta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, duration, 0), true);

        item.setItemMeta(meta);
        return item;
    }
}
