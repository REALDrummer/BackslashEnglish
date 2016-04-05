package myWolfFinder;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

/** @author connor */
public class myWolfFinder extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        // print out the locations of all the wolves currently on the server
        for (Wolf entity : getServer().getWorlds().get(0).getEntitiesByClass(Wolf.class))
            for (Player player : getServer().getOnlinePlayers())
                if (player.isOp())
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "There's a wolf at (" + entity.getLocation().getBlockX() + ", " + entity.getLocation().getBlockY() + ", "
                            + entity.getLocation().getBlockZ() + ")!");
    }

    @EventHandler
    public void findWolvesAsTheySpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.WOLF) {
            for (Player player : getServer().getOnlinePlayers())
                if (player.isOp())
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "A wolf spawned at (" + event.getLocation().getBlockX() + ", " + event.getLocation().getBlockY() + ", "
                            + event.getLocation().getBlockZ() + ")!");
        }
    }
}
