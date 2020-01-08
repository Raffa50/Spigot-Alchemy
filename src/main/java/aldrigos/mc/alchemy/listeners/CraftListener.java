package aldrigos.mc.alchemy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent e){
        var res = new ItemStack(Material.POTION);
        var meta =  Bukkit.getItemFactory().getItemMeta(Material.POTION);
    }
}
