package aldrigos.mc.alchemy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Alchemy {
    public static ItemStack createFusedPotion(Iterable<ItemStack> potions){
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);

        for(var p: potions){
            var fuse = (PotionMeta) p.getItemMeta();
            var pd = fuse.getBasePotionData();
            var eff = pd.getType().getEffectType();
            int duration = getDuration(eff, pd.isExtended(), pd.isUpgraded());

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

    private static void setEffects(PotionMeta meta, PotionEffect... effects){
        for(var eff: effects)
            meta.addCustomEffect(
                getExtendedUpgradedEffect(eff.getType()),
                true
            );
    }

    private static PotionEffect getExtendedUpgradedEffect(PotionEffectType potionEffType){
        return new PotionEffect(
                potionEffType,
                getDuration(potionEffType, true, true),
                1);
    }

    public static ItemStack createExtendedUpgradedPotion(PotionType potionType){
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        meta.setBasePotionData(new PotionData(potionType));
        /*meta.setBasePotionData(new PotionData(PotionType.THICK));
        meta.setColor(potionType.getEffectType().getColor());
        meta.setDisplayName(potionType.getEffectType().getName());*/
        setEffects(meta, getExtendedUpgradedEffect(potionType.getEffectType()));

        var item = new ItemStack(Material.POTION);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createExtendedUpgradedPotion(PotionEffect... effects){
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        meta.setBasePotionData(new PotionData(PotionType.THICK));
        setEffects(meta, effects);

        var res = new ItemStack(Material.POTION);
        res.setItemMeta(meta);
        return res;
    }

    private static final Map<PotionDataAdapter, Integer> potionDuration = new HashMap<>();

    public static void setDuration(PotionEffectType pt, boolean extended, boolean enhanced, int duration){
        potionDuration.put(new PotionDataAdapter(pt, extended, enhanced), duration);
    }

    static {
        setDuration(PotionEffectType.REGENERATION, false, false, 42*20);
        setDuration(PotionEffectType.REGENERATION, true, false, 90*20);
        setDuration(PotionEffectType.REGENERATION, false, true, 22*20);
        setDuration(PotionEffectType.REGENERATION, true, true, 66*20);
    }

    public static int getDuration(PotionEffectType pet, boolean extended, boolean enhanced){
        Integer duration = potionDuration.get(new PotionDataAdapter(pet, extended, enhanced));
        return duration != null ?
                duration :
                (extended ? (enhanced ? 90*20 : 20*180) : (enhanced ? 20*60 : 20*90));
    }
}
