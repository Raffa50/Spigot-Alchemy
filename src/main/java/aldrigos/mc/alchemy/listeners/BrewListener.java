package aldrigos.mc.alchemy.listeners;

import aldrigos.mc.alchemy.*;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class BrewListener implements Listener {
    private final AlchemyPlugin plugin;

    public BrewListener(AlchemyPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBrew(BrewEvent e){
        var ingredient = e.getContents().getIngredient();
        if(ingredient == null)
            return;

        switch (ingredient.getType()) {
            case GLOWSTONE:
                handleExtension(e);
                break;
            case REDSTONE:
                //handleUpgrade(e);
                break;
            case GUNPOWDER:
                handleSplash(e);
                break;
            case DRAGON_BREATH:
                handleLingering(e);
                break;
        }
    }

    private void handleExtension(BrewEvent e) {
        for(int i=0; i< 3; i++){
            ItemStack result = e.getContents().getItem(i);
            if(result == null || !result.hasItemMeta() || result.getType() != Material.POTION)
                continue;

            var pot = (PotionMeta) result.getItemMeta();
            var pd = pot.getBasePotionData();
            if(pd.isUpgraded() && !pd.isExtended()){
                //var newMeta =
            }
        }
    }

    private void handleLingering(BrewEvent e) {
        for(int i=0; i< 3; i++){
            ItemStack result = e.getContents().getItem(i);
            if(result == null || !result.hasItemMeta() || result.getType() != Material.SPLASH_POTION)
                continue;

            var pot = (PotionMeta) result.getItemMeta();
            if(!pot.hasCustomEffects())
                return;

            var fusedSplash = new ItemStack(Material.LINGERING_POTION);
            fusedSplash.setItemMeta(pot);

            e.getContents().setItem(i, fusedSplash);
        }
    }

    private void handleSplash(BrewEvent e) {
        for(int i=0; i< 3; i++) {
            ItemStack result = e.getContents().getItem(i);
            if(result == null || !result.hasItemMeta() || result.getType() != Material.POTION)
                continue;

            var pot = (PotionMeta) result.getItemMeta();
            if(!pot.hasCustomEffects())
                return;

            var fusedSplash = new ItemStack(Material.SPLASH_POTION);
            fusedSplash.setItemMeta(pot);

            e.getContents().setItem(i, fusedSplash);
        }
    }
}
