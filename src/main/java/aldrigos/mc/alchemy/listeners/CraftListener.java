package aldrigos.mc.alchemy.listeners;

import aldrigos.mc.alchemy.*;
import org.bukkit.Material;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CraftListener implements Listener {
    private final AlchemyPlugin p;

    public CraftListener(AlchemyPlugin plugin){
        p = plugin;
    }

    private ItemStack potionFusion(ItemStack[] craftMatrix){
        if(Arrays.stream(craftMatrix)
                .anyMatch(s -> s != null && (s.getType() != Material.POTION || !s.hasItemMeta()) ))
            return null;

        List<ItemStack> potions = Arrays.stream(craftMatrix)
                .filter(s -> s != null && s.getType() == Material.POTION && s.hasItemMeta())
                .collect(Collectors.toList());

        if(potions.size() < 2)
            return null;

        return p.getApi().createFusedPotion(potions);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(PrepareItemCraftEvent e){
        ItemStack res = potionFusion(e.getInventory().getMatrix());
        if(res != null)
            e.getInventory().setResult(res);
    }
}
