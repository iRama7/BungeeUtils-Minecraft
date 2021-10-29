package rama.bungeeutils;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class saveLogout implements Listener {

    private BungeeUtilsSpigot plugin;

    public saveLogout(BungeeUtilsSpigot plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void cambioMundo(PlayerChangedWorldEvent e) {

        FileConfiguration Database = plugin.getDatabase();
        File DatabaseFile = plugin.getDatabaseFile();
        FileConfiguration config = plugin.getConfig();

        Player player = e.getPlayer();
        String prevWorldName = e.getFrom().getName();
        String newWorldName = player.getWorld().getName();
        UUID playerUUID = player.getUniqueId();
        String playerUUIDString = playerUUID.toString();
        Boolean saveLogoutIsEnabled = config.getBoolean("config.save_logout_world");

        if (saveLogoutIsEnabled) {
            Database.set(playerUUIDString + "." + prevWorldName, false);
            Database.set(playerUUIDString + "." + newWorldName, true);
            try {
                Database.save(DatabaseFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}
