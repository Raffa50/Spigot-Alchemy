package aldrigos.mc.alchemy.commands;

import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.*;

public class AlchemyDebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String name, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("[Alchemy]Player only command");
            return false;
        }

        var player = (Player) sender;
        var item = player.getInventory().getItemInMainHand();
        if(item.getType() != Material.AIR){
            sender.sendMessage("[Alchemy]"+item.toString());
        }

        return true;
    }
}
