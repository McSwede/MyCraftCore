package me.mcswede.mycraftcore.modules;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class RemoveProjectileRandomness implements Listener {

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        final Projectile projectile = e.getEntity();
        // Only correct arrows, snowballs and eggs
        if (!(e.getEntityType() == EntityType.ARROW || e.getEntityType() == EntityType.SNOWBALL || e.getEntityType() == EntityType.EGG)) return;

        final ProjectileSource shooter = projectile.getShooter();
        if (!(shooter instanceof Player player)) return;

        final Vector playerDirection = player.getLocation().getDirection().normalize();
        final Vector projectileDirection = projectile.getVelocity();

        // Keep original speed
        final double originalMagnitude = projectileDirection.length();
        projectileDirection.normalize();

        final ItemStack item = player.getInventory().getItemInMainHand();

        // If the projectile is not going straight (e.g. multishot arrows)
        if (item.getType() == Material.CROSSBOW && item.getEnchantmentLevel(Enchantment.MULTISHOT) > 0) {
            if (multishot(projectileDirection, rotateAroundY(playerDirection.clone(), 0.17))) {
                rotateAroundY(playerDirection, 0.17);
            } else if (multishot(projectileDirection, rotateAroundY(playerDirection.clone(), -0.17))) {
                rotateAroundY(playerDirection, -0.17);
            }
        }

        playerDirection.multiply(originalMagnitude);
        projectile.setVelocity(playerDirection);
    }

    private boolean multishot(Vector a, Vector b) {
        return Math.abs(a.getX() - b.getX()) < 0.1d &&
                Math.abs(a.getZ() - b.getZ()) < 0.1d;
    }

    private static Vector rotateAroundY(Vector vector, double angle) {
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);

        double newX = cosAngle * vector.getX() + sinAngle * vector.getZ();
        double newZ = -sinAngle * vector.getX() + cosAngle * vector.getZ();

        vector.setX(newX);
        vector.setZ(newZ);

        return vector;
    }
}
