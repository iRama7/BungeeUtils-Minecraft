package rama.bungeeutils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class databaseConstructor implements Listener {

    private BungeeUtilsSpigot plugin;

    public databaseConstructor(BungeeUtilsSpigot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        String currentServer = plugin.getConfig().getString("config.server");
        if (currentServer.equalsIgnoreCase("minas")) {
            Player player = e.getPlayer();
            String currentWorldName = player.getWorld().getName();
            UUID playerUUID = player.getUniqueId();
            String playerUUIDString = playerUUID.toString();
            FileConfiguration config = plugin.getConfig();
            FileConfiguration Database = plugin.getDatabase();
            File DatabaseFile = plugin.getDatabaseFile();
            Boolean saveLogoutIsEnabled = config.getBoolean("config.save_logout_world");

            if (saveLogoutIsEnabled) {

                Boolean playerPathExists = Database.isSet(playerUUIDString);
                if (!playerPathExists) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "warp minas "+player.getName());
                    Database.set(playerUUIDString + ".world", false);
                    Database.set(playerUUIDString + ".world_nether", false);
                    Database.set(playerUUIDString + ".world_the_end", false);
                    try {
                        Database.save(DatabaseFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                Database.set(playerUUIDString + "." + currentWorldName, true);
                try {
                    Database.save(DatabaseFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }else if(currentServer.equalsIgnoreCase("villa")){


            Player player = e.getPlayer();
            String currentWorldName = player.getWorld().getName();
            UUID playerUUID = player.getUniqueId();
            String playerUUIDString = playerUUID.toString();
            FileConfiguration config = plugin.getConfig();
            FileConfiguration Database = plugin.getDatabase();
            File DatabaseFile = plugin.getDatabaseFile();
            Boolean saveLogoutIsEnabled = config.getBoolean("config.save_logout_world");

            if (saveLogoutIsEnabled) {

                Boolean playerPathExists = Database.isSet(playerUUIDString);
                if (!playerPathExists) {
                    Database.set(playerUUIDString + ".spawn", false);
                    Database.set(playerUUIDString + ".mundo_parcelas", false);
                    try {
                        Database.save(DatabaseFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                Database.set(playerUUIDString + "." + currentWorldName, true);
                try {
                    Database.save(DatabaseFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }else{
            plugin.getLogger().severe("El servidor que has ingresado en la configuración no es válido (Server: "+currentServer+")");
            plugin.getLogger().severe("Stopping server.");
            plugin.getServer().shutdown();
        }
    }
}

