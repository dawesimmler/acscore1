package leos.dev.AcsCore.bootstrap;

import leos.dev.AcsCore.AcsCore;
import leos.dev.AcsCore.commands.core.CommandRegistrar;

public class CoreBootstrap {
    private final AcsCore plugin;

    public CoreBootstrap(AcsCore plugin) {
        this.plugin = plugin;
    }

    public void registerEarly() {
        CommandRegistrar.register(plugin);
    }
}