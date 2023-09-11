package me.mcswede.mycraftcore;

import me.mcswede.mycraftcore.modules.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MyCraftCore extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getConfig().getBoolean("highway.enabled")) {
            this.getCommand("highway").setExecutor(new HighwayFinder(this));
        }
        if (getConfig().getBoolean("unstrip.enabled")) {
            getServer().getPluginManager().registerEvents(new Unstrip(), this);
        }
        getLogger().info("MyCraftCore has been enabled.");

    }

    @Override
    public void onDisable() {
        getLogger().info("MyCraftCore has been disabled.");
    }
}
