package rama.bungeeutils.authhook;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rama.bungeeutils.BungeeUtilsSpigot;

import java.util.UUID;

public class authMain implements Listener {

    private BungeeUtilsSpigot plugin;

    public authMain(BungeeUtilsSpigot plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void getLoginEvent(LoginEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        sendToBungee(uuid, player);
    }
    public void sendToBungee(UUID uuid, Player player){
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("authChannel");
            out.writeUTF(String.valueOf(uuid));
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
