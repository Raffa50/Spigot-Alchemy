package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.recipes.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.*;

import java.io.*;
import java.util.Arrays;

public final class Alchemy {
    private static ExtendedUpgradedBrewingRecipe eubr;
    public final AlchemyDb db;

    private Alchemy(AlchemyDb db){
        this.db = db;
    }

    static Alchemy load(String path) throws IOException, ClassNotFoundException {
        var saveFile = new File(path+"db.bin");

        Alchemy ret;
        if(saveFile.exists()) {
            try(var file = new FileInputStream(path+"db.bin")) {
                var in = new ObjectInputStream(file);
                var db = (AlchemyDb) in.readObject();
                ret = new Alchemy(db);
                ret.addRecipes(witherPotionRecipe());
            }
        }else {
            var db = new AlchemyDb();
            ret = new Alchemy(db);
        }

        eubr = new ExtendedUpgradedBrewingRecipe();
        ret.addRecipes(eubr);
        return ret;
    }

    void save(String path) throws IOException{
        var dir = new File(path);
        if(!dir.exists())
            dir.mkdir();

        db.recipes.remove(eubr);
        try(var file = new FileOutputStream(path+"db.bin")){
            var out = new ObjectOutputStream(file);
            out.writeObject(db);
        }
    }

    public static boolean isEffectPotion(ItemStack i){
        return i != null && i.getType() == Material.POTION && i.hasItemMeta();
    }

    public void addRecipes(BrewingRecipe... recipes){
        db.recipes.addAll(Arrays.asList(recipes));
    }

    public boolean isIngredient(ItemStack item){
        for(var r: db.recipes){
            if(r.getIngredient().isSimilar(item))
                return true;
        }
        return false;
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

    static BrewingRecipe witherPotionRecipe(){
        var toBrew = new ItemStack(Material.POTION, 3);
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        meta.setBasePotionData(new PotionData(PotionType.AWKWARD));
        toBrew.setItemMeta(meta);

        return new SimpleBrewingRecipe(Material.NETHER_STAR, toBrew, createWitherPotion(40*20));
    }

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
