package aldrigos.mc.alchemy.listeners;

import aldrigos.mc.alchemy.*;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;
import java.util.logging.Logger;

public class BrewListener implements Listener {
    private final AlchemyPlugin plugin;
    private final Logger log;

    public BrewListener(AlchemyPlugin plugin){
        this.plugin = plugin;
        log = plugin.getLogger();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBrewInventory(InventoryClickEvent e){
        if(e.getInventory().getType() != InventoryType.BREWING || e.getAction() != InventoryAction.PLACE_ALL)
            return;

        var brewInv = (BrewerInventory) e.getInventory();
        var ingr = e.getCursor();

        //log.info(" brewInventory: "+brewInv.toString());

        if(ingr != null && ingr.getType() == Material.GOLD_NUGGET &&
            Arrays.stream(brewInv.getStorageContents()).anyMatch(Utils::isEffectPotion)
        ) {
            e.getWhoClicked().getInventory().remove(ingr);
            //TODO
            //brewInv.setIngredient(ingr); //doesn't work
            //brewInv.getHolder().setBrewingTime(20 * 20);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBrew(BrewEvent e){
        var ingredient = e.getContents().getIngredient();
        if(ingredient == null)
            return;

        switch (ingredient.getType()) {
            case GOLD_NUGGET:
                log.info("handleExtendedUpgrade");
                handleExtendedUpgrade(e.getContents());
                break;
            case GUNPOWDER:
                handleSplash(e);
                break;
            case DRAGON_BREATH:
                handleLingering(e);
                break;
        }
    }

    private void handleExtendedUpgrade(BrewerInventory inv) {
        for(int i=0; i< 3; i++){
            ItemStack result = inv.getItem(i);
            if(!Utils.isEffectPotion(result))
                continue;

            var pot = (PotionMeta) result.getItemMeta();
            var pd = pot.getBasePotionData();
            if(pd.isUpgraded() ^ pd.isExtended()){
                var eup =
                        pot.hasCustomEffects() ?
                        Alchemy.createExtendedUpgradedPotion(Utils.toArray(pot.getCustomEffects())) :
                        Alchemy.createExtendedUpgradedPotion(pd.getType());

                inv.setItem(i, eup);
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
            if(!Utils.isEffectPotion(result))
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
