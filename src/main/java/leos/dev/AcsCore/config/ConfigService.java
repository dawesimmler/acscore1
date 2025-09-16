package leos.dev.AcsCore.config;

import leos.dev.AcsCore.AcsCore;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigService {
    private final AcsCore plugin;

    public ConfigService(AcsCore plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration raw() {
        return plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
    }

    public String prefix() {
        return raw().getString("chat.prefix", "");
    }
}