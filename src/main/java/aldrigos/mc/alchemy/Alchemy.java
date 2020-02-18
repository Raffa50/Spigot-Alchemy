package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.recipes.*;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.Arrays;

public final class Alchemy {
    private static ExtendedUpgradedBrewingRecipe eubr;
    public final AlchemyDb db;

    private Alchemy(AlchemyDb db){
        this.db = db;
    }

    @NotNull
    static Alchemy load(String path) throws IOException {
        var saveFile = new File(path+"db.json");

        Alchemy ret;
        if(saveFile.exists()) {
            try(var file = new FileReader(saveFile)) {
                var db = new Gson().fromJson(file, AlchemyDb.class);
                ret = new Alchemy(db);
            }
        }else {
            ret = new Alchemy(new AlchemyDb());
            ret.addRecipes(witherPotionRecipe());
        }

        eubr = new ExtendedUpgradedBrewingRecipe(ret);
        ret.addRecipes(eubr);
        return ret;
    }

    void save(String path) throws IOException{
        var dir = new File(path);
        if(!dir.exists())
            dir.mkdir();

        db.recipes.remove(eubr);
        try(var file = new FileWriter(path+"db.json")){
            file.write(new Gson().toJson(db));
        }
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

    public void startBrewing(@NotNull BrewerInventory inv, @NotNull Plugin p){
        var recipe = db.getRecipe(inv);
        if(recipe == null)
            return;

        new BrewClock(recipe, inv).start(p);
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
        var toBrew = new ItemStack(Material.POTION, 3);
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
