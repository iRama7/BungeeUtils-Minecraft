package rama.bungeeutils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import rama.bungeeutils.authhook.getLastServer;
import rama.bungeeutils.combatCheck.readCombatCheck;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class BungeeUtilsBungeeCord extends Plugin {

    public static BungeeUtilsBungeeCord plugin;

    @Override
    public void onEnable() {
        getProxy().registerChannel("my:channel");
        plugin = this;
        registerEvents();
        registerCommands();
        registerConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void registerCommands(){
        getProxy().getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c(BUNGEEUTILS) &6Registrando comandos..."));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoMinas(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoNether(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoEnd(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoSpawn(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoParcelas(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new comandoPH(this));

    }

    public void registerEvents(){
        getProxy().getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c(BUNGEEUTILS) &6Registrando eventos..."));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new getLastServer(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new comandoMinas(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new comandoNether(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new comandoEnd(this));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerJoinMessage());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new readCombatCheck());
    }

    public static BungeeUtilsBungeeCord getPlugin(){
        return plugin;
    }

    public Configuration getConfig() throws IOException {
        Configuration configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        return configuration;
    }


    public void registerConfig(){
        getProxy().getConsole().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c(BUNGEEUTILS) &6Registrando configuración..."));
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");


        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
