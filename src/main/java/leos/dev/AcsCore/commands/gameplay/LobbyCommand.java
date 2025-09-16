package leos.dev.AcsCore.commands.gameplay;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public LobbyCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player p = (Player) sender;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("lobby");

        p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        return true;
    }
}
