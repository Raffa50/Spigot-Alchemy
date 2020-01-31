package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.listeners.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class AlchemyPlugin extends JavaPlugin {
    private Alchemy api;

    public Alchemy getApi(){ return api; }

    @Override
    public void onEnable(){
        api = new Alchemy();

        var pm = getServer().getPluginManager();
        pm.registerEvents(new CraftListener(this), this);
        pm.registerEvents(new BrewListener(this), this);
    }

    @Override
    public void onDisable(){
        HandlerList.unregisterAll(this);
    }
}
