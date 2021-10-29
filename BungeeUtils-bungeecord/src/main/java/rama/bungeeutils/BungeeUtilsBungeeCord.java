package rama.bungeeutils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeUtilsBungeeCord extends Plugin {

    public static BungeeUtilsBungeeCord plugin;

    @Override
    public void onEnable() {
        getProxy().registerChannel("my:channel");
        getLogger().info( "Ha sido activado!" );
        plugin = this;
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void registerCommands(){
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoMinas(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoNether(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoEnd(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoSpawn(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoParcelas(this));

    }

    public static BungeeUtilsBungeeCord getPlugin(){
        return plugin;
    }

}
