package leos.dev.AcsCore.util;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {
    private static final Pattern HEX = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String color(String input) {
        if (input == null || input.isEmpty()) return "";
        Matcher matcher = HEX.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(sb, ChatColor.of("#" + hex).toString());
        }
        matcher.appendTail(sb);
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }

    public static String strip(String s) {
        return ChatColor.stripColor(color(s));
    }
}