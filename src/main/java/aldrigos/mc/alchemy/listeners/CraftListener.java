package aldrigos.mc.alchemy.listeners;

import aldrigos.mc.alchemy.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import java.util.Arrays;

public class CraftListener implements Listener {
    private final AlchemyPlugin p;

    public CraftListener(AlchemyPlugin plugin){
        p = plugin;
    }

    private ItemStack potionFusion(ItemStack[] craftMatrix){
        if(Arrays.stream(craftMatrix).filter(s -> s != null && s.getType() == Material.POTION).count() < 2)
            return null;
        var meta = (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);

        for(var ing: craftMatrix) {
            if(ing == null)
                continue;
            if (ing.getType() != Material.POTION || !ing.hasItemMeta())
                return null;

            var fuse = (PotionMeta) ing.getItemMeta();
            var pd = fuse.getBasePotionData();
            if(pd.getType().getEffectType() != null)
                meta.addCustomEffect(new PotionEffect(pd.getType().getEffectType(), 20*60, 1), true);

            for(var ce: fuse.getCustomEffects())
                meta.addCustomEffect(ce, true);
        }

        meta.setBasePotionData(new PotionData(PotionType.THICK));
        var res = new ItemStack(Material.POTION);
        res.setItemMeta(meta);
        return res;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(PrepareItemCraftEvent e){
        ItemStack res = potionFusion(e.getInventory().getMatrix());
        if(res != null)
            e.getInventory().setResult(res);
    }
}
