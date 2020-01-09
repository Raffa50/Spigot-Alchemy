package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public class AlchemyPlugin extends JavaPlugin {
    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new CraftListener(this), this);
    }
}
