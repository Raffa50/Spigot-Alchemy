package aldrigos.mc.alchemy.listeners;

import aldrigos.mc.alchemy.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class BrewListener implements Listener {
    private final AlchemyPlugin plugin;
    private final Alchemy api;

    public BrewListener(AlchemyPlugin plugin){
        this.plugin = plugin;
        this.api = plugin.getApi();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBrewPlaceIngredient(InventoryClickEvent e){
        if(e.getClickedInventory() == null || e.getClickedInventory().getType() != InventoryType.BREWING ||
            e.getSlotType() != InventoryType.SlotType.FUEL ||
            e.getAction() != InventoryAction.PLACE_ALL ||
            e.getClick() != ClickType.LEFT)
            return;

        final var brewInv = (BrewerInventory) e.getClickedInventory();
        final ItemStack ingr = e.getCursor();

        if(ingr != null && api.isIngredient(ingr)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ingr.setAmount(ingr.getAmount() - 1);
                brewInv.setIngredient(new ItemStack(ingr.getType()));

                api.startBrewing(brewInv, plugin);
            }, 1L);
            ((Player) e.getWhoClicked()).updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBrewPlace(InventoryClickEvent e){
        if(e.getClickedInventory() == null || e.getClickedInventory().getType() != InventoryType.BREWING ||
            e.getSlotType() == InventoryType.SlotType.FUEL ||
            e.getAction() != InventoryAction.PLACE_ALL ||
            e.getClick() != ClickType.LEFT)
            return;

        var brewInv = (BrewerInventory) e.getClickedInventory();

        api.startBrewing(brewInv, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBrew(BrewEvent e){
        var ingredient = e.getContents().getIngredient();
        if(ingredient == null)
            return;

        switch (ingredient.getType()) {
            case GUNPOWDER:
                handleSplash(e);
                break;
            case DRAGON_BREATH:
                handleLingering(e);
                break;
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
            if(!Alchemy.isEffectPotion(result))
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
