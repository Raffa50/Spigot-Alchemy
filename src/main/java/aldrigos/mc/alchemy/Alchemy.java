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
            int duration = getDuration(pd.getType(), pd.isExtended(), pd.isUpgraded());

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

    }

    private static PotionEffect getExtendedUpgradedEffect(PotionType potionType){
        return new PotionEffect(
                potionType.getEffectType(),
                getDuration(potionType, true, true),
                1);
    }

    public static ItemStack createExtendedUpgradedPotion(PotionType potionType){
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);
        meta.setBasePotionData(new PotionData(PotionType.THICK));
        meta.setColor(potionType.getEffectType().getColor());
        meta.setDisplayName(potionType.getEffectType().getName());
        setEffects(meta, getExtendedUpgradedEffect(potionType));

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

    public static void setDuration(PotionType pt, boolean extended, boolean enhanced, int duration){
        potionDuration.put(new PotionDataAdapter(pt, extended, enhanced), duration);
    }

    static {
        setDuration(PotionType.REGEN, false, false, 42*20);
        setDuration(PotionType.REGEN, true, false, 90*20);
        setDuration(PotionType.REGEN, false, true, 22*20);
        setDuration(PotionType.REGEN, true, true, 66*20);
    }

    public static int getDuration(PotionType pt, boolean extended, boolean enhanced){
        Integer duration = potionDuration.get(new PotionDataAdapter(pt, extended, enhanced));
        return duration != null ?
                duration :
                (extended ? (enhanced ? 90*20 : 20*180) : (enhanced ? 20*60 : 20*90));
    }
}
