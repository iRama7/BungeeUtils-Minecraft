package rama.bungeeutils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class bukkitToBungee implements CommandExecutor {

    private BungeeUtilsSpigot plugin;

    public bukkitToBungee(BungeeUtilsSpigot plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            plugin.getLogger().warning("No puedes usar ese comando desde la consola.");
        }else{
            if(args[0].equalsIgnoreCase("minas")){
                String data = "minas";
                sendToBungee(data, ((Player) sender).getPlayer());
            }else if (args[0].equalsIgnoreCase("nether")){
                String data = "nether";
                sendToBungee(data, ((Player) sender).getPlayer());
            }else if(args[0].equalsIgnoreCase("dragon")){
                String data = "dragon";
                sendToBungee(data, ((Player) sender).getPlayer());
            }
        }

     return false;
    }
    public void sendToBungee(String data, Player player) {
        String currentServer = plugin.getConfig().getString("config.server");
        if (currentServer.equalsIgnoreCase("villa")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("spigotChannel");
            out.writeUTF(data);
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        }
    }
}
