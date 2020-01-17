package aldrigos.mc.alchemy;

import org.bukkit.potion.PotionType;

class PotionDataAdapter {
    public PotionType potionType;
    public boolean isExtended;
    public boolean isEnhanced;

    public PotionDataAdapter(){}

    public PotionDataAdapter(PotionType potionType, boolean isExtended, boolean isEnhanced){
        this.potionType = potionType;
        this.isExtended = isExtended;
        this.isEnhanced = isEnhanced;
    }

    @Override
    public int hashCode(){
        return potionType.hashCode() + (isExtended ? 1 : 0) + (isEnhanced ? 1 : 0);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof PotionDataAdapter))
            return false;

        return equals((PotionDataAdapter)o);
    }

    public boolean equals(PotionDataAdapter p){
        return p != null && potionType == p.potionType && isEnhanced == p.isEnhanced && isExtended == p.isExtended;
    }
}
