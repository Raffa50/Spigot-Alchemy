package aldrigos.mc.alchemy;

import org.bukkit.potion.PotionEffectType;

import java.io.Serializable;

class PotionDataAdapter implements Serializable {
    public PotionEffectType potionEffectType;
    public boolean isExtended;
    public boolean isEnhanced;

    PotionDataAdapter(){}

    public PotionDataAdapter(PotionEffectType pet, boolean extended, boolean enhanced){
        potionEffectType = pet;
        this.isExtended = extended;
        this.isEnhanced = enhanced;
    }

    @Override
    public int hashCode(){
        return potionEffectType.hashCode() + (isExtended ? 1 : 0) + (isEnhanced ? 1 : 0);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof PotionDataAdapter))
            return false;

        return equals((PotionDataAdapter)o);
    }

    public boolean equals(PotionDataAdapter p){
        return p != null && potionEffectType == p.potionEffectType && isEnhanced == p.isEnhanced && isExtended == p.isExtended;
    }
}
