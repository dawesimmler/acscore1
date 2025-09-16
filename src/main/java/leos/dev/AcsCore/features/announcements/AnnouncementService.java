package leos.dev.AcsCore.features.announcements;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import leos.dev.AcsCore.util.Titles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.regex.Pattern;

public class AnnouncementService {
    private final AcsCore plugin;
    private long lastGlobal = 0L;
    private Pattern blockedPattern;

    public AnnouncementService(AcsCore plugin) {
        this.plugin = plugin;
        buildPattern();
    }

    private void buildPattern() {
        var list = plugin.getConfig().getStringList("chat.blocked-words");
        if (list.isEmpty()) {
            blockedPattern = null;
            return;
        }
        String joined = list.stream()
                .map(s -> "\\b(?i)" + Pattern.quote(s.toLowerCase(Locale.ROOT)) + "\\b")
                .reduce((a, b) -> a + "|" + b).orElse("");
        blockedPattern = joined.isEmpty() ? null : Pattern.compile(joined);
    }

    public boolean announce(Player sender, String message) {
        String raw = message.trim();
        if (raw.isEmpty()) return false;

        if (!sender.hasPermission("acscore.bypass.filter")) {
            if (blockedPattern != null && blockedPattern.matcher(raw.toLowerCase(Locale.ROOT)).find()) {
                sender.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Blocked word"));
                return false;
            }
        }

        long cd = 30 * 1000L;
        long now = System.currentTimeMillis();
        if (now - lastGlobal < cd) {
            long left = (cd - (now - lastGlobal) + 999) / 1000;
            sender.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Wait " + left + "s"));
            return false;
        }

        lastGlobal = now;

        String chatLine = ColorUtil.color("&#DEB1FB[ACSPVP] &f" + raw);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(chatLine);
            Titles.send(p, "&#DEB1FB&lACSPVP", "&f" + raw, 5, 60, 10);
            try {
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1.2f);
            } catch (Exception ignored) {}
        }
        return true;
    }

    public void init() { }

    public void shutdown() { }
}
