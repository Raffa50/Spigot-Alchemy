package aldrigos.mc.alchemy.listeners;

import aldrigos.mc.alchemy.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

public class CraftListener implements Listener {
    private final AlchemyPlugin p;

    public CraftListener(AlchemyPlugin plugin){
        p = plugin;
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        var res = new ItemStack(Material.POTION);
        var meta =  (PotionMeta) Bukkit.getItemFactory().getItemMeta(Material.POTION);

        for(var ing: e.getInventory().getMatrix()) {
            p.getLogger().info("[Alchemy]"+ing.getType().toString());
            if (ing.getType() != Material.POTION)
                return;
        }

        var data = new PotionData(PotionType.THICK);
        meta.setBasePotionData(data);
        res.setItemMeta(meta);
    }
}
