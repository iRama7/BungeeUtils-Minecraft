package rama.bungeeutils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static rama.bungeeutils.BungeeUtilsBungeeCord.plugin;

public class comandoSpawn extends Command {
    public comandoSpawn(BungeeUtilsBungeeCord bungeeUtilsBungeeCord) {
        super("villa");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if((sender instanceof ProxiedPlayer)){
            ProxiedPlayer player = (ProxiedPlayer)sender;

            ByteArrayOutputStream bb = new ByteArrayOutputStream();
            DataOutputStream outt = new DataOutputStream(bb);
            try{
                outt.writeUTF(((ProxiedPlayer) sender).getDisplayName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String data1 = "villa";
            String data2 = "sameServer2";
            String channel = "survivalChannel";
            Server playerServer = player.getServer();
            if(playerServer.getInfo().getName().equalsIgnoreCase("villa")){
                sendCustomData(player, data2, channel);
            }else {
                sendCustomData(player, data1, channel);
                player.connect(ProxyServer.getInstance().getServerInfo("villa"));
            }

        }
    }

    public void sendCustomData(ProxiedPlayer player, String data1, String channel){
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        if ( networkPlayers == null || networkPlayers.isEmpty() )
        {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF( channel ); // the channel could be whatever you want
        out.writeUTF(data1); // this data could be whatever you want
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                player.getServer().getInfo().sendData( "my:channel", out.toByteArray() );
                plugin.getProxy().getLogger().info(ChatColor.YELLOW+"[BungeeUtils] está enviando al jugador ("+ChatColor.RED+ player.getDisplayName() +ChatColor.YELLOW+") al servidor Villa para luego transportarlo a (Spawn)");
            }
        }, 500, TimeUnit.MILLISECONDS);
    }
}
