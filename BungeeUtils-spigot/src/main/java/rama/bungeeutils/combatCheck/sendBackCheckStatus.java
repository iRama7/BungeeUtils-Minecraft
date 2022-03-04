package rama.bungeeutils.combatCheck;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import static rama.bungeeutils.BungeeUtilsSpigot.plugin;

public class sendBackCheckStatus {

   static void sendBackCheckStatus(String checkStatus) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            try {
                out.writeUTF("CombatCheckReturnChannel");
                out.writeUTF(checkStatus);
                plugin.getServer().sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            } finally {

            }
    }

}
