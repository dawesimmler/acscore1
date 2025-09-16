package leos.dev.AcsCore.security;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    private final AcsCore plugin;
    private final Set<String> lastMessages = ConcurrentHashMap.newKeySet();
    private Pattern blockedPattern;

    public ChatListener(AcsCore plugin) {
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
                .reduce((a,b) -> a + "|" + b)
                .orElse("");
        blockedPattern = joined.isEmpty() ? null : Pattern.compile(joined);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        var cfg = plugin.getConfig();
        if (!p.hasPermission("acscore.bypass.filter")) {
            if (blockedPattern != null && blockedPattern.matcher(e.getMessage().toLowerCase(Locale.ROOT)).find()) {
                e.setCancelled(true);
                p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Message blocked"));
                return;
            }
            if (cfg.getBoolean("chat.block-links", true)) {
                if (LinkBlocker.containsLink(e.getMessage(), cfg.getStringList("chat.link-allowlist"))) {
                    e.setCancelled(true);
                    p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Links not allowed"));
                    return;
                }
            }
            int maxPerWindow = cfg.getInt("chat.max-message-per-2s",1);
            if (!RateChatLimiter.check(p.getUniqueId(), maxPerWindow, 2000)) {
                e.setCancelled(true);
                p.sendMessage(ColorUtil.color(plugin.configService().prefix() + "&#FB2626Slow down"));
                return;
            }
        }
        String prefix = plugin.configService().prefix();
        e.setFormat(ColorUtil.color(prefix + "&f%1$s&7: &f%2$s"));
    }
}