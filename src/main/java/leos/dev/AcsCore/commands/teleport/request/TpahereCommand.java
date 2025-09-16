package leos.dev.AcsCore.commands.teleport.request;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.teleport.request.TpaManager;
import leos.dev.AcsCore.util.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpahereCommand implements CommandExecutor {
    private final AcsCore plugin;
    private final TpaManager manager;

    public TpahereCommand(AcsCore plugin, TpaManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    private void clickable(Player target, Player from) {
        TextComponent base = new TextComponent(from.getName() + " requests you to teleport to them. ");
        base.setColor(ChatColor.of("#DEB1FB"));
        TextComponent accept = new TextComponent("[ACCEPT]");
        accept.setColor(ChatColor.GREEN);
        accept.setBold(true);
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpaccept"));
        TextComponent deny = new TextComponent("[DENY]");
        deny.setColor(ChatColor.RED);
        deny.setBold(true);
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpdeny"));
        base.addExtra(accept);
        base.addExtra(new TextComponent(" "));
        base.addExtra(deny);
        target.spigot().sendMessage(base);
        target.sendMessage(ColorUtil.color("&7Expires in &a15s"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        if (args.length != 1) {
            p.sendMessage(ColorUtil.color(AcsCore.get().configService().prefix() + "&fUsage: /tpahere <player>"));
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            p.sendMessage(ColorUtil.color(AcsCore.get().configService().prefix() + "&#FB2626Player not found"));
            return true;
        }
        if (target == p) {
            p.sendMessage(ColorUtil.color(AcsCore.get().configService().prefix() + "&#FB2626Cannot self"));
            return true;
        }
        long exp = System.currentTimeMillis() + 15000;
        manager.put(new TpaManager.Request(p.getUniqueId(), target.getUniqueId(), exp, TpaManager.Type.TPAHERE));
        p.sendMessage(ColorUtil.color(AcsCore.get().configService().prefix() + "&fRequest sent"));
        clickable(target, p);
        return true;
    }
}