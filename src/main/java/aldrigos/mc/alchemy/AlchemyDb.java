package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.recipes.BrewingRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.*;

import java.util.*;

public final class AlchemyDb {
    private final Map<PotionDataAdapter, Integer> potionsDuration = new HashMap<>();
    final Collection<BrewingRecipe> recipes = new ArrayList<>();

    AlchemyDb() {
        setDuration(PotionEffectType.REGENERATION, false, false, 42*20);
        setDuration(PotionEffectType.REGENERATION, true, false, 90*20);
        setDuration(PotionEffectType.REGENERATION, false, true, 22*20);
        setDuration(PotionEffectType.REGENERATION, true, true, 66*20);

        setDuration(PotionEffectType.SPEED, false, false, 180*20);
        setDuration(PotionEffectType.SPEED, true, false, 480*20);
        setDuration(PotionEffectType.SPEED, false, true, 90*20);
        setDuration(PotionEffectType.SPEED, true, true, 120*20);

        setDuration(PotionEffectType.FIRE_RESISTANCE, false, false, 180*20);
        setDuration(PotionEffectType.FIRE_RESISTANCE, true, false, 480*20);

        setDuration(PotionEffectType.NIGHT_VISION, false, false, 180*20);
        setDuration(PotionEffectType.NIGHT_VISION, true, false, 480*20);

        setDuration(PotionEffectType.INCREASE_DAMAGE, false, false, 180*20);
        setDuration(PotionEffectType.INCREASE_DAMAGE, true, false, 480*20);
        setDuration(PotionEffectType.INCREASE_DAMAGE, false, true, 90*20);
        setDuration(PotionEffectType.INCREASE_DAMAGE, true, true, 120*20);

        setDuration(PotionEffectType.JUMP, false, false, 180*20);
        setDuration(PotionEffectType.JUMP, true, false, 480*20);
        setDuration(PotionEffectType.JUMP, false, true, 90*20);
        setDuration(PotionEffectType.JUMP, true, true, 120*20);

        setDuration(PotionEffectType.WATER_BREATHING, false, false, 180*20);
        setDuration(PotionEffectType.WATER_BREATHING, true, false, 480*20);

        setDuration(PotionEffectType.INVISIBILITY, false, false, 180*20);
        setDuration(PotionEffectType.INVISIBILITY, true, false, 480*20);

        setDuration(PotionEffectType.SLOW_FALLING, false, false, 90*20);
        setDuration(PotionEffectType.SLOW_FALLING, true, false, 240*20);

        setDuration(PotionEffectType.LUCK, false, false, 300*20);

        setDuration(PotionEffectType.POISON, false, false, 42*20);
        setDuration(PotionEffectType.POISON, true, false, 90*20);
        setDuration(PotionEffectType.POISON, false, true, 22*20);
        setDuration(PotionEffectType.POISON, true, true, 66*20);

        setDuration(PotionEffectType.WEAKNESS, false, false, 90*20);
        setDuration(PotionEffectType.WEAKNESS, true, false, 240*20);
    }

    public void remove(Material ingredient){
        recipes.removeIf(r -> r.getIngredient().getType() == ingredient);
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

    @NotNull
    public Iterable<BrewingRecipe> getRecipes(){ return recipes; }

    public BrewingRecipe getRecipe(@NotNull BrewerInventory inventory)
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
