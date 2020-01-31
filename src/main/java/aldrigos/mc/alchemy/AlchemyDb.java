package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.recipes.BrewingRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public final class AlchemyDb {
    private final Map<PotionDataAdapter, Integer> potionsDuration = new HashMap<>();
    public final Collection<BrewingRecipe> recipes = new ArrayList<>();

    public AlchemyDb() {
        setDuration(PotionEffectType.REGENERATION, false, false, 42*20);
        setDuration(PotionEffectType.REGENERATION, true, false, 90*20);
        setDuration(PotionEffectType.REGENERATION, false, true, 22*20);
        setDuration(PotionEffectType.REGENERATION, true, true, 66*20);
    }

    public void setDuration(PotionEffectType pt, boolean extended, boolean enhanced, int duration){
        potionsDuration.put(new PotionDataAdapter(pt, extended, enhanced), duration);
    }

    public int getDuration(PotionEffectType pet, boolean extended, boolean enhanced){
        Integer duration = potionsDuration.get(new PotionDataAdapter(pet, extended, enhanced));
        return duration != null ?
                duration :
                (extended ? (enhanced ? 90*20 : 20*180) : (enhanced ? 20*60 : 20*90));
    }

    public BrewingRecipe getRecipe(BrewerInventory inventory)
    {
        var ingr = inventory.getIngredient();
        if(ingr == null || ingr.getType() == Material.AIR)
            return null;

        for(int i= 0; i < 3; i++) {
            var toBrew = inventory.getContents()[i];
            if(toBrew == null || toBrew.getType() == Material.AIR)
                continue;

            for (var recipe : recipes) {
                if (recipe.matches(ingr, toBrew))
                    return recipe;
            }
        }
        return null;
    }
}
