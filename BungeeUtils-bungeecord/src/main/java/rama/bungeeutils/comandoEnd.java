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
import java.util.concurrent.TimeUnit;

import static rama.bungeeutils.BungeeUtilsBungeeCord.getPlugin;
import static rama.bungeeutils.BungeeUtilsBungeeCord.plugin;

public class comandoEnd extends Command {

    public comandoEnd(BungeeUtilsBungeeCord bungeeUtilsBungeeCord){
        super("dragon");
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
            String data1 = "end";
            String data2 = "sameServer";
            String channel = "minasChannel";
            Server playerServer = player.getServer();
            if(playerServer.getInfo().getName().equalsIgnoreCase("minas")){
                sendCustomData(player, data2, channel);
            }else {
                sendCustomData(player, data1, channel);
                player.connect(ProxyServer.getInstance().getServerInfo("minas"));
            }

        }
    }
    public void sendCustomData(ProxiedPlayer player, String data1, String channel){
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        // perform a check to see if globally are no players
        if ( networkPlayers == null || networkPlayers.isEmpty() )
        {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(channel);
        out.writeUTF(data1);
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                player.getServer().getInfo().sendData( "my:channel", out.toByteArray() );
                plugin.getProxy().getLogger().info(ChatColor.YELLOW+"[BungeeUtils] está enviando al jugador ("+ChatColor.RED+ player.getDisplayName() +ChatColor.YELLOW+") al servidor Minas para luego transportarlo a (world_the_end)");
            }
        }, 500, TimeUnit.MILLISECONDS);
    }


}

