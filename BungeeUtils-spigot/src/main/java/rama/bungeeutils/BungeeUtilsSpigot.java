package rama.bungeeutils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import rama.bungeeutils.authhook.authMain;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public final class BungeeUtilsSpigot extends JavaPlugin implements PluginMessageListener {

    public String rutaConfig;
    private FileConfiguration Database = null;
    private File DatabaseFile = null;



    @Override
    public void onEnable() {
        if (!getServer().getPluginManager().isPluginEnabled(this)) {
            return;
        }
        getServer().getMessenger().registerIncomingPluginChannel(this, "my:channel", this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getLogger().info("enabled successfully.");
        registrarConfig();
        registrarComandos();
        createDatabase();
        registrarEventos();
        registrarErrores();
    }




    @Override
    public void onDisable()
    {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    public void registrarErrores(){
        String currentServer = getConfig().getString("config.server");
        if(currentServer == null){
            getLogger().severe("No has especificado qué servidor es este en la configuración del plugin.");
            getLogger().severe("Stopping server.");
            getServer().shutdown();
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player unused, byte[] bytes) {



        FileConfiguration config = this.getConfig();
        String minas_lang = ChatColor.translateAlternateColorCodes('&', config.getString("lang.minas_lang"));
        String nether_lang = ChatColor.translateAlternateColorCodes('&', config.getString("lang.nether_lang"));
        String end_lang = ChatColor.translateAlternateColorCodes('&', config.getString("lang.end_lang"));

        String minas_command = ChatColor.translateAlternateColorCodes('&', config.getString("config.minas_command"));
        String nether_command = ChatColor.translateAlternateColorCodes('&', config.getString("config.nether_command"));
        String end_command = ChatColor.translateAlternateColorCodes('&', config.getString("config.end_command"));

        String villa_lang = ChatColor.translateAlternateColorCodes('&', config.getString("lang.villa_lang"));
        String parcelas_lang = ChatColor.translateAlternateColorCodes('&', config.getString("lang.parcelas_lang"));

        String villa_command = ChatColor.translateAlternateColorCodes('&', config.getString("config.villa_command"));
        String parcelas_command = ChatColor.translateAlternateColorCodes('&', config.getString("config.parcelas_command"));


        if (!channel.equalsIgnoreCase("my:channel")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();
        if (subChannel.equalsIgnoreCase("minasChannel")) {
            String data1 = in.readUTF();
            String uuid = in.readUTF();

            FileConfiguration Database = this.getDatabase();
            Player player = Bukkit.getServer().getPlayer(uuid);
            if(player == null){
                return;
            }
            UUID playerUUID = player.getUniqueId();
            String playerUUIDString = playerUUID.toString();

            Boolean isInWorld = Database.getBoolean(playerUUIDString + ".world");
            Boolean isInWorld_nether = Database.getBoolean(playerUUIDString + ".world_nether");

            if (data1.equalsIgnoreCase("sameServer_to_world")) {
                Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), minas_command.replaceAll("%player%", player.getName()));
                player.sendMessage(minas_lang);
            }else if (data1.equalsIgnoreCase("sameServer_to_nether")) {
                Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), nether_command.replaceAll("%player%", player.getName()));
                player.sendMessage(nether_lang);
            }else if (data1.equalsIgnoreCase("sameServer_to_end")) {
                Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), end_command.replaceAll("%player%", player.getName()));
                player.sendMessage(end_lang);
            }else if (data1.equalsIgnoreCase("world")) {
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        if (!isInWorld) {
                            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), minas_command.replaceAll("%player%", player.getName()));
                            player.sendMessage(minas_lang);
                        } else {
                            getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e[Debug] &7" + uuid + " &7no fue teletransportado ya que ya estaba en ese mundo (world) al desconectarse."));
                        }
                    }
                }, 5);
            } else if (data1.equalsIgnoreCase("nether")) {
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        if (!isInWorld_nether) {
                            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), nether_command.replaceAll("%player%", player.getName()));
                            player.sendMessage(nether_lang);
                        } else {
                            getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e[Debug] &7" + uuid + " &7no fue teletransportado ya que ya estaba en ese mundo (world_nether) al desconectarse."));
                        }
                    }
                }, 5);
            } else if (data1.equalsIgnoreCase("end")) {
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), end_command.replaceAll("%player%", player.getName()));

                        Double x = 1.5;
                        Double y = 1.5;
                        Double z = 1.5;
                        float pitch = 1;
                        float yaw = 1;
                        String worldName = "world_the_end";
                        World world = Bukkit.getWorld(worldName);
                        Location warpLoc = new Location(world, x, y, z, yaw, pitch);


                        if(warpLoc.isWorldLoaded()) {

                            player.sendMessage(end_lang);
                        }
                    }
                }, 5);
            }
        } else if (subChannel.equalsIgnoreCase("survivalChannel")) {
            String data1 = in.readUTF();
            String uuid = in.readUTF();

            FileConfiguration Database = this.getDatabase();
            Player player = Bukkit.getServer().getPlayer(uuid);
            if(player == null){
                return;
            }
            UUID playerUUID = player.getUniqueId();
            String playerUUIDString = playerUUID.toString();

            Boolean isInVillage = Database.getBoolean(playerUUIDString + ".spawn");
            Boolean isInParcelas = Database.getBoolean(playerUUIDString + ".mundo_parcelas");

            if (data1.equalsIgnoreCase("sameServer2_to_villa")) {
                Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), villa_command.replaceAll("%player%", player.getName()));
                player.sendMessage(villa_lang);
            }else if(data1.equalsIgnoreCase("sameServer2_to_parcelas")){
                PlotAPI api = new PlotAPI();
                api.registerListener(this);

                PlotPlayer plotPlayer = api.wrapPlayer(playerUUID);
                int plotCount = plotPlayer.getPlotCount();
                if(plotCount == 0){

                    double x = -54.5;
                    double y = 60;
                    double z = -54.5;
                    float pitch = 5;
                    float yaw = -44;
                    World w = Bukkit.getWorld("mundo_parcelas");
                    Location loc = new Location(w, x, y, z, pitch, yaw);
                    player.teleport(loc);
                }else {

                    player.performCommand(parcelas_command);
                    player.sendMessage(parcelas_lang);
                }
            }else if(data1.equalsIgnoreCase("villa")) {
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        if (!isInVillage) {
                            Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), villa_command.replaceAll("%player%", player.getName()));
                            player.sendMessage(villa_lang);
                        } else {
                            getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e[Debug] &7" + uuid + " &7no fue teletransportado ya que ya estaba en ese mundo (Spawn) al desconectarse."));
                        }
                    }
                }, 5);
            }else if(data1.equalsIgnoreCase("parcelas")) {
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        if (!isInParcelas) {
                            PlotAPI api = new PlotAPI();
                            api.registerListener(this);

                            PlotPlayer plotPlayer = api.wrapPlayer(playerUUID);
                            int plotCount = plotPlayer.getPlotCount();
                            if(plotCount == 0){

                                double x = -54.5;
                                double y = 60;
                                double z = -54.5;
                                float pitch = 5;
                                float yaw = -44;
                                World w = Bukkit.getWorld("mundo_parcelas");
                                Location loc = new Location(w, x, y, z, pitch, yaw);
                                player.teleport(loc);
                            }else {
                                player.performCommand(parcelas_command);
                                player.sendMessage(parcelas_lang);
                            }
                        } else {
                            getLogger().info(ChatColor.translateAlternateColorCodes('&', "&e[Debug] &7" + uuid + " &7no fue teletransportado ya que ya estaba en ese mundo (mundo_parcelas) al desconectarse."));
                        }
                    }
                }, 5);
            }
        }else if(subChannel.equalsIgnoreCase("BungeeChannel")){
            String data1 = in.readUTF();
            String player_name = in.readUTF();
            if(data1.equalsIgnoreCase("JoinEvent")){
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a▲ &7"+player_name));
            }
        }
    }
    public void registrarConfig(){
        File config = new File(this.getDataFolder(),"config.yml");
        rutaConfig = config.getPath();
        if(!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    public void registrarComandos(){
        this.getCommand("bungeeutils").setExecutor(new reloadCommand(this));
        this.getCommand("bungee-utils").setExecutor(new bukkitToBungee(this));
    }
    public void registrarEventos(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new databaseConstructor(this), this);
        pm.registerEvents(new saveLogout(this),this);
        pm.registerEvents(new authMain(this), this);
    }

    public FileConfiguration getDatabase(){
        return this.Database;
    }
    public File getDatabaseFile(){
        return this.DatabaseFile;
    }
    public void reloadDatabase(){

    }
    private void createDatabase(){
        DatabaseFile = new File(getDataFolder(), "database.yml");
        if(!DatabaseFile.exists()){
            DatabaseFile.getParentFile().mkdirs();
            saveResource("database.yml", false);
        }

        Database = new YamlConfiguration();
        try{
            Database.load(DatabaseFile);
        }catch(IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }
}