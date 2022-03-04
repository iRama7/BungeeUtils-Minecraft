package rama.bungeeutils.authhook;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import rama.bungeeutils.BungeeUtilsBungeeCord;

import java.io.*;
import java.util.UUID;


public class getLastServer implements Listener{

    private BungeeUtilsBungeeCord plugin;

    public getLastServer(BungeeUtilsBungeeCord plugin){
        this.plugin = plugin;
    }

    static Boolean isNew = false;
    @EventHandler
    public void getIfNew(ServerConnectEvent e){
        ServerConnectEvent.Reason reason = e.getReason();
        if(reason.toString().equalsIgnoreCase("JOIN_PROXY")){
            isNew = true;
        }else{
            isNew = false;
        }
    }

    @EventHandler
    public void writeConnectedServer(ServerConnectedEvent e) throws IOException {
        String serverName = e.getServer().getInfo().getName();
        ProxiedPlayer player = e.getPlayer();
        Configuration config = plugin.getConfig();
        UUID uuid = player.getUniqueId();
        if(!isNew) {
            if (serverName.equalsIgnoreCase("villa")) {
                config.set(uuid + ".villa", true);
                config.set(uuid + ".minas", false);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(plugin.getDataFolder(), "config.yml"));
            } else if (serverName.equalsIgnoreCase("minas")) {
                config.set(uuid + ".villa", false);
                config.set(uuid + ".minas", true);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(plugin.getDataFolder(), "config.yml"));
            }
        }
    }
    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) throws IOException {

            if (e.getTag().equalsIgnoreCase("BungeeCord")) {
                DataInputStream in = new DataInputStream(new ByteArrayInputStream((e.getData())));
                try {
                    String channel = in.readUTF();
                    if (channel.equalsIgnoreCase("authChannel")) {
                    String uuid = in.readUTF();
                    String player_name = in.readUTF();
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(player_name);
                    Configuration config = plugin.getConfig();
                        Boolean lastServerIsMinas = config.getBoolean(uuid+".minas");
                        if(lastServerIsMinas){
                            player.connect(ProxyServer.getInstance().getServerInfo("minas"));
                        }
                    }

                } catch (EOFException eof) {
                    eof.printStackTrace();
                }
            }
        }
    }

