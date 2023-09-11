package me.mcswede.mycraftcore.modules;

import me.mcswede.mycraftcore.MyCraftCore;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import static java.lang.Math.abs;

public class HighwayFinder implements CommandExecutor {

    private final MyCraftCore plugin;
    public HighwayFinder(MyCraftCore plugin) {
        this.plugin = plugin;
    }
    private double distance;
    private String direction;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        MiniMessage miniMessage = MiniMessage.miniMessage(); // Create the MiniMessage instance
        if (label.equalsIgnoreCase("highway")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(miniMessage.deserialize("This command can only be used by players."));
                return true;
            }

            if (!player.hasPermission("mycraftcore.highway")) {
                player.sendMessage(miniMessage.deserialize("<gray>You do not have permission to use this command.</gray>"));
                return true;
            }

            if (player.getWorld().getName().equals("world_nether")) {
                double x = player.getLocation().getX() - plugin.getConfig().getDouble("highway.center.x");
                double z = player.getLocation().getZ() - plugin.getConfig().getDouble("highway.center.z");
                double y = player.getLocation().getY() - plugin.getConfig().getDouble("highway.center.y");

                String highwayDirection = getClosestHighway(x, z);
                if (abs(distance) > (plugin.getConfig().getDouble("highway.dimensions.width")/2)) {
                    player.sendMessage(miniMessage.deserialize("<gray>The closest highway is the " + highwayDirection + " highway.<newline>Which is <green>" + abs((int)Math.round(distance)) + "</green> blocks to the " + direction + "<newline>and " + getVerticalDirection(y) + "."));
                }
                else {
                    if (y >= 0 && y <= plugin.getConfig().getDouble("highway.dimensions.height")) {
                        player.sendMessage(miniMessage.deserialize("<gray>You are already on the highway.</gray>")); //TODO: Tell player what highway they are on
                    }
                    else {
                        player.sendMessage(miniMessage.deserialize("<gray>The highway is " + getVerticalDirection(y) + " from your current position."));
                    }
                }
            } else {
                player.sendMessage(miniMessage.deserialize("<gray>You must be in the Nether to use this command.</gray>"));
            }
        }

        return true;
    }

    private String getClosestHighway(double x, double z) {
        double distanceToNorth = z;
        double distanceToSouth = -z;
        double distanceToEast = -x;
        double distanceToWest = x;

        String closestHighway = "<bold><color:#E02443>North</color></bold>";
        double closestDistance = distanceToNorth;
        direction = x<0 ? "<green>East</green>": "<green>West</green>";
        distance = x;

        if (distanceToSouth < closestDistance) {
            closestHighway = "<bold><color:#17BF63>South</color></bold>";
            closestDistance = distanceToSouth;
            direction = x<0 ? "<green>East</green>": "<green>West</green>";
            distance = x;
        }

        if (distanceToEast < closestDistance) {
            closestHighway = "<bold><color:#EBB617>East</color></bold>";
            closestDistance = distanceToEast;
            direction = z<0 ? "<green>South</green>": "<green>North</green>";
            distance = z;
        }

        if (distanceToWest < closestDistance) {
            closestHighway = "<bold><color:#1D72F2>West</color></bold>";
            direction = z<0 ? "<green>South</green>": "<green>North</green>";
            distance = z;
        }

        return closestHighway;
    }

    private String getVerticalDirection(double y) {
        String upDown;
        if (y>=-0.5 && y<0.5) {
            return "on this height";
        }
        else {
            if (y<0) {
                upDown = "<green>up</green>";
            } else {
                upDown = "<green>down</green>";
            }
            return "<green>" + abs((int)Math.round(y)) + "</green> blocks " + upDown;
        }
    }
}
