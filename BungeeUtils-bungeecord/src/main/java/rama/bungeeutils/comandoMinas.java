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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static rama.bungeeutils.BungeeUtilsBungeeCord.getPlugin;
import static rama.bungeeutils.BungeeUtilsBungeeCord.plugin;

public class comandoMinas extends Command {

    public comandoMinas(BungeeUtilsBungeeCord bungeeUtilsBungeeCord){
        super("minas");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if((sender instanceof ProxiedPlayer)){
            ProxiedPlayer player = (ProxiedPlayer)sender;

            String playerName = ((ProxiedPlayer) sender).getDisplayName();

            ByteArrayOutputStream bb = new ByteArrayOutputStream();
            DataOutputStream outt = new DataOutputStream(bb);
            try{
                outt.writeUTF(((ProxiedPlayer) sender).getDisplayName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data1 = "world";
            String data2 = "sameServer";
            String uuid = playerName;
            String channel = "minasChannel";
            Server playerServer = player.getServer();
            if(playerServer.getInfo().getName().equalsIgnoreCase("minas")){
                sendCustomData(data2,uuid, channel);
            }else {
                sendCustomData(data1,uuid, channel);
                player.connect(ProxyServer.getInstance().getServerInfo("minas"));
            }

        }
    }
    public void sendCustomData(String data1,String uuid, String channel){
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        if ( networkPlayers == null || networkPlayers.isEmpty() )
        {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(channel);
        out.writeUTF(data1);
        out.writeUTF(uuid);

        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                Map<String, ServerInfo> servers = ProxyServer.getInstance().getServers();
                for (Map.Entry<String, ServerInfo> en : servers.entrySet()) {
                    String name = en.getKey();
                    ServerInfo server = ProxyServer.getInstance().getServerInfo(name);
                    server.sendData("my:channel", out.toByteArray());
                }
                plugin.getProxy().getLogger().info(ChatColor.YELLOW+"[BungeeUtils] está enviando al jugador ("+ChatColor.RED+ uuid +ChatColor.YELLOW+") al servidor Minas para luego transportarlo a (world)");
            }
        }, 750, TimeUnit.MILLISECONDS);
    }


}

