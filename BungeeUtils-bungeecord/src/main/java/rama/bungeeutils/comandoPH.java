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
import rama.bungeeutils.combatCheck.sendCombatCheck;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static rama.bungeeutils.BungeeUtilsBungeeCord.plugin;
import static rama.bungeeutils.combatCheck.readCombatCheck.checkStatus;

public class comandoPH extends Command {

    public comandoPH(BungeeUtilsBungeeCord bungeeUtilsBungeeCord) {
        super("p", "comando.parcelas");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args[0].equalsIgnoreCase("h")) {


            if ((sender instanceof ProxiedPlayer)) {
                ProxiedPlayer player = (ProxiedPlayer) sender;

                String playerName = ((ProxiedPlayer) sender).getDisplayName();

                String data1 = "parcelas";
                String data2 = "sameServer2_to_parcelas";
                String channel = "survivalChannel";
                Server playerServer = player.getServer();
                if (playerServer.getInfo().getName().equalsIgnoreCase("villa")) {
                    sendCustomData(data2, playerName, channel);
                }else {
                    sendCombatCheck.sendCombatCheck(playerName);
                    plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                        @Override
                        public void run() {
                            if(checkStatus.equalsIgnoreCase("false")){
                                sendCustomData(data1, playerName, channel);
                                player.connect(ProxyServer.getInstance().getServerInfo("villa"));
                            }
                        }
                    }, 1, TimeUnit.SECONDS);
                }

            }
        }return;
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
                plugin.getProxy().getLogger().info(ChatColor.YELLOW+"[BungeeUtils] está enviando al jugador ("+ChatColor.RED+ playerName +ChatColor.YELLOW+") al servidor Villa para luego transportarlo a (mundo_parcelas)");
            }
        }, 750, TimeUnit.MILLISECONDS);
    }
}
