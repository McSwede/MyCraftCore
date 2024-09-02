package me.mcswede.mycraftcore;

import com.tchristofferson.configupdater.ConfigUpdater;
import me.mcswede.mycraftcore.modules.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public final class MyCraftCore extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");
        try {
            ConfigUpdater.update(this, "config.yml", configFile, Collections.emptyList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadConfig();

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
        if (getConfig().getBoolean("removeprojectilerandomness.enabled", true)) {
            getServer().getPluginManager().registerEvents(new RemoveProjectileRandomness(), this);
        }
        if (getServer().getPluginManager().getPlugin("ProtocolLib") != null && getConfig().getBoolean("anvillimit.enabled", true)) {
            getServer().getPluginManager().registerEvents(new AnvilLimit(this), this);
        }
        else {
            getLogger().warning("ProtocolLib not found! Some features of MCC will be disabled.");
        }
        getLogger().info("MyCraftCore has been enabled.");

    }

    @Override
    public void onDisable() {
        LeafDecay.scheduledBlocks.clear();
        getLogger().info("MyCraftCore has been disabled.");
    }
}
