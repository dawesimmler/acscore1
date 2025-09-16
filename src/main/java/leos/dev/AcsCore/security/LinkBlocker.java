package leos.dev.AcsCore.security;

import java.util.List;
import java.util.regex.Pattern;

public class LinkBlocker {
    private static final Pattern LINK = Pattern.compile("(https?://|www\\.)\\S+", Pattern.CASE_INSENSITIVE);

    public static boolean containsLink(String msg, List<String> allow) {
        var m = LINK.matcher(msg);
        while (m.find()) {
            String found = m.group().toLowerCase();
            boolean allowed = allow.stream().anyMatch(a -> found.contains(a.toLowerCase()));
            if (!allowed) return true;
        }
        return false;
    }
}