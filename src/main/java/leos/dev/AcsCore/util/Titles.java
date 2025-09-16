package leos.dev.AcsCore.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import java.time.Duration;
import org.bukkit.entity.Player;

public class Titles {
    public static void send(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        Title t = Title.title(
                Component.text(ColorUtil.color(title)),
                Component.text(ColorUtil.color(subtitle)),
                Title.Times.times(
                        Duration.ofMillis(fadeIn*50L),
                        Duration.ofMillis(stay*50L),
                        Duration.ofMillis(fadeOut*50L))
        );
        p.showTitle(t);
    }
}