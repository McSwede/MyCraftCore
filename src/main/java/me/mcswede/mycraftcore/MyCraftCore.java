package me.mcswede.mycraftcore;

import me.mcswede.mycraftcore.modules.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class MyCraftCore extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getConfig().getBoolean("highway.enabled", true)) {
            this.getCommand("highway").setExecutor(new HighwayFinder(this));
        }
        if (getConfig().getBoolean("unstrip.enabled", true)) {
            getServer().getPluginManager().registerEvents(new Unstrip(), this);
        }
        if (getConfig().getBoolean("petowner.enabled", true)) {
            getServer().getPluginManager().registerEvents(new PetOwner(), this);
        }
        if (getConfig().getBoolean("leafdecay.enabled", true)) {
            getServer().getPluginManager().registerEvents(new LeafDecay(this), this);
        }
        getLogger().info("MyCraftCore has been enabled.");

    }

    @Override
    public void onDisable() {
        LeafDecay.scheduledBlocks.clear();
        getLogger().info("MyCraftCore has been disabled.");
    }
}
