package rama.bungeeutils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;


public class reloadCommand implements CommandExecutor {

    private BungeeUtilsSpigot plugin;

    public reloadCommand(BungeeUtilsSpigot plugin){
        this.plugin = plugin;
    }


    String prefix = ChatColor.translateAlternateColorCodes('&', "&3[&6BungeeUtils&3]");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        File config = new File(plugin.getDataFolder(), "config.yml");
        if(args.length == 0){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &eUtiliza &e&o/bungeeutils reload &epara recargar la configuración."));
        }else if(args[0].equalsIgnoreCase("reload")){
            if(!config.exists()){
                plugin.getConfig().options().copyDefaults(true);
                plugin.saveDefaultConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &eSe ha creado el archivo de configuración desde 0 correctamente."));
            }else{
                plugin.reloadConfig();
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" &eSe ha recargado el archivo de configuración correctamente."));
            }
        }




        return false;
    }
}
