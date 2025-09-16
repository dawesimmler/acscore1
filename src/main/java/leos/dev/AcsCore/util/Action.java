package leos.dev.AcsCore.util;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Action {
    public static void bar(Player p, String msg) {
        p.sendActionBar(Component.text(ColorUtil.strip(ColorUtil.color(msg))));
    }
}