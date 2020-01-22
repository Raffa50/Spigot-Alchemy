package aldrigos.mc.alchemy;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Utils {
    public static <T> T[] toArray(List<T> list){
        return (T[])list.toArray();
    }

    public static boolean isEffectPotion(ItemStack i){
        return i != null && i.getType() == Material.POTION && i.hasItemMeta();
    }
}
