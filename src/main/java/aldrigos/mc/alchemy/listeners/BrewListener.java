package aldrigos.mc.alchemy.listeners;

import aldrigos.mc.alchemy.*;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.inventory.BrewEvent;

public class BrewListener implements Listener {
    private final AlchemyPlugin plugin;

    public BrewListener(AlchemyPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBrew(BrewEvent e){
        if( e.getContents().getIngredient() == null || e.getContents().getIngredient().getType() != Material.GUNPOWDER)
            return;

        for(var res: e.getContents().getStorageContents()){
            if(res != null)
                plugin.getLogger().info(res.toString());
        }
    }
}
