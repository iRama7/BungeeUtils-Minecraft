package rama.bungeeutils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static rama.bungeeutils.BungeeUtilsBungeeCord.getPlugin;
import static rama.bungeeutils.BungeeUtilsBungeeCord.plugin;

public class comandoNether extends Command {

    public comandoNether(BungeeUtilsBungeeCord bungeeUtilsBungeeCord){
        super("nether");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if((sender instanceof ProxiedPlayer)){
            ProxiedPlayer player = (ProxiedPlayer)sender;

            String playerName = ((ProxiedPlayer) sender).getDisplayName();

            String data1 = "nether";
            String data2 = "sameServer";
            String channel = "minasChannel";
            Server playerServer = player.getServer();
            if(playerServer.getInfo().getName().equalsIgnoreCase("minas")){
                sendCustomData(data2, playerName, channel);
            }else {
                sendCustomData(data1, playerName, channel);
                player.connect(ProxyServer.getInstance().getServerInfo("minas"));
            }


        }
    }
    public void sendCustomData(String data1, String playerName, String channel){
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        if ( networkPlayers == null || networkPlayers.isEmpty() )
        {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(channel);
        out.writeUTF(data1);
        out.writeUTF(playerName);
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
                for (Map.Entry<String, ServerInfo> en : servers.entrySet()) {
                    String name = en.getKey();
                    ServerInfo server = ProxyServer.getInstance().getServerInfo(name);
                    server.sendData("my:channel", out.toByteArray());
                }
                plugin.getProxy().getLogger().info(ChatColor.YELLOW+"[BungeeUtils] está enviando al jugador ("+ChatColor.RED+ playerName +ChatColor.YELLOW+") al servidor Minas para luego transportarlo a (world_nether)");
            }
        }, 750, TimeUnit.MILLISECONDS);
    }


}

