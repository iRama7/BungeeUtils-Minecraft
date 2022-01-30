package rama.bungeeutils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class bukkitToBungee implements CommandExecutor {

    private static BungeeUtilsSpigot plugin;

    public bukkitToBungee(BungeeUtilsSpigot plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("No puedes usar ese comando desde la consola.");
        } else {
            if (args[0].equalsIgnoreCase("minas")) {
                String data = "minas";
                sendToBungee(data, sender.getName());
            } else if (args[0].equalsIgnoreCase("nether")) {
                String data = "nether";
                sendToBungee(data, sender.getName());
            } else if (args[0].equalsIgnoreCase("dragon")) {
                String data = "dragon";
                sendToBungee(data, sender.getName());
            }
        }
        return false;
    }


    public void sendToBungee(String data, String player_name) {
        String currentServer = plugin.getConfig().getString("config.server");
        assert currentServer != null;
        if (currentServer.equalsIgnoreCase("villa")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            try {
                out.writeUTF("spigotChannel");
                out.writeUTF(data);
                out.writeUTF(player_name);
                plugin.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            } finally {

            }
        }
    }
}

