package rama.bungeeutils.combatCheck;

import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

public class readCombatCheck implements Listener {

    public static String checkStatus;

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) throws IOException {

        if (e.getTag().equalsIgnoreCase("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream((e.getData())));
            try {
                String channel = in.readUTF();
                if (channel.equalsIgnoreCase("CombatCheckReturnChannel")) {
                    checkStatus = in.readUTF();
                }

            } catch (EOFException eof) {
                eof.printStackTrace();
            }
        }else{

        }
    }

}
