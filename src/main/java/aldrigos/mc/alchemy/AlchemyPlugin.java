package aldrigos.mc.alchemy;

import aldrigos.mc.alchemy.listeners.*;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class AlchemyPlugin extends JavaPlugin {
    final static String directory = "plugins/alchemy/";

    private Alchemy api;

    public Alchemy getApi(){ return api; }

    @Override
    public void onEnable(){
        try {
            api = Alchemy.load(directory);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            getLogger().severe("[Alchemy]Failed to load");
            setEnabled(false);
            return;
        }

        var pm = getServer().getPluginManager();
        pm.registerEvents(new CraftListener(this), this);
        pm.registerEvents(new BrewListener(this), this);
    }

    @Override
    public void onDisable(){
        HandlerList.unregisterAll(this);
        try {
            api.save(directory);
        } catch (IOException e) {
            e.printStackTrace();
            getLogger().severe("[Alchemy]Failed to save");
        }
    }
}
