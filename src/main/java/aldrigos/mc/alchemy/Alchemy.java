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
            int duration = pd.isExtended() ? 20*180 : 20*90;

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

    private static final Map<PotionType, Integer> potionDuration = new HashMap<>();

    static {

    }

    public static int getDuration(PotionType pt, boolean extended, boolean enhanced){
        return 20*60;
    }
}
