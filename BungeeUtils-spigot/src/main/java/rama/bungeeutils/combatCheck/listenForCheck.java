package rama.bungeeutils.combatCheck;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import nl.marido.deluxecombat.api.DeluxeCombatAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class listenForCheck implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player unused, @NotNull byte[] bytes) {
        if (!channel.equalsIgnoreCase("my:channel")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subChannel = in.readUTF();
        if(subChannel.equalsIgnoreCase("CombatCheckChannel")){
            String player_name = in.readUTF();

            Player player = Bukkit.getServer().getPlayer(player_name);
            DeluxeCombatAPI api = new DeluxeCombatAPI();
            if(api.isInCombat(player)){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo puedes viajar en combate"));
                sendBackCheckStatus.sendBackCheckStatus(String.valueOf(api.isInCombat(player)));
            }else{
                sendBackCheckStatus.sendBackCheckStatus(String.valueOf(api.isInCombat(player)));
            }
        }
    }
}
