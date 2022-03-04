package rama.bungeeutils.combatCheck;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static rama.bungeeutils.BungeeUtilsBungeeCord.plugin;

public class sendCombatCheck {

    public static void sendCombatCheck(String player_name){
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        if ( networkPlayers == null || networkPlayers.isEmpty() )
        {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("CombatCheckChannel");
        out.writeUTF(player_name);
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
                for (Map.Entry<String, ServerInfo> en : servers.entrySet()) {
                    String name = en.getKey();
                    if(name.equalsIgnoreCase("minas")) {
                        ServerInfo server = ProxyServer.getInstance().getServerInfo(name);
                        server.sendData("my:channel", out.toByteArray());
                    }
                }
                plugin.getProxy().getLogger().info(ChatColor.YELLOW+"[BungeeUtils] está enviando un CombatCheck para ("+ChatColor.RED+ player_name +ChatColor.YELLOW+")");
            }
        }, 750, TimeUnit.MILLISECONDS);
    }

}
