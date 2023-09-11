package me.mcswede.mycraftcore.modules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Unstrip implements Listener {
    public static List<Material> axeArray = new ArrayList<>(Arrays.asList(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE));

    @EventHandler
    public void OnBlockRightClick(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("mycraftcore.unstrip")) {
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                && axeArray.contains(event.getPlayer().getEquipment().getItemInMainHand().getType())) {

            Location strippedLoc = Objects.requireNonNull(event.getClickedBlock()).getLocation();
            World world = event.getClickedBlock().getWorld();
            switch (event.getClickedBlock().getType()) {
                case STRIPPED_ACACIA_LOG -> world.getBlockAt(strippedLoc).setType(Material.ACACIA_LOG);
                case STRIPPED_ACACIA_WOOD -> world.getBlockAt(strippedLoc).setType(Material.ACACIA_WOOD);
                case STRIPPED_BIRCH_LOG -> world.getBlockAt(strippedLoc).setType(Material.BIRCH_LOG);
                case STRIPPED_BIRCH_WOOD -> world.getBlockAt(strippedLoc).setType(Material.BIRCH_WOOD);
                case STRIPPED_CHERRY_LOG -> world.getBlockAt(strippedLoc).setType(Material.CHERRY_LOG);
                case STRIPPED_CHERRY_WOOD -> world.getBlockAt(strippedLoc).setType(Material.CHERRY_WOOD);
                case STRIPPED_CRIMSON_STEM -> world.getBlockAt(strippedLoc).setType(Material.CRIMSON_STEM);
                case STRIPPED_CRIMSON_HYPHAE -> world.getBlockAt(strippedLoc).setType(Material.CRIMSON_HYPHAE);
                case STRIPPED_DARK_OAK_LOG -> world.getBlockAt(strippedLoc).setType(Material.DARK_OAK_LOG);
                case STRIPPED_DARK_OAK_WOOD -> world.getBlockAt(strippedLoc).setType(Material.DARK_OAK_WOOD);
                case STRIPPED_JUNGLE_LOG -> world.getBlockAt(strippedLoc).setType(Material.JUNGLE_LOG);
                case STRIPPED_JUNGLE_WOOD -> world.getBlockAt(strippedLoc).setType(Material.JUNGLE_WOOD);
                case STRIPPED_MANGROVE_LOG -> world.getBlockAt(strippedLoc).setType(Material.MANGROVE_LOG);
                case STRIPPED_MANGROVE_WOOD -> world.getBlockAt(strippedLoc).setType(Material.MANGROVE_WOOD);
                case STRIPPED_OAK_LOG -> world.getBlockAt(strippedLoc).setType(Material.OAK_LOG);
                case STRIPPED_OAK_WOOD -> world.getBlockAt(strippedLoc).setType(Material.OAK_WOOD);
                case STRIPPED_SPRUCE_LOG -> world.getBlockAt(strippedLoc).setType(Material.SPRUCE_LOG);
                case STRIPPED_SPRUCE_WOOD -> world.getBlockAt(strippedLoc).setType(Material.SPRUCE_WOOD);
                case STRIPPED_WARPED_STEM -> world.getBlockAt(strippedLoc).setType(Material.WARPED_STEM);
                case STRIPPED_WARPED_HYPHAE -> world.getBlockAt(strippedLoc).setType(Material.WARPED_HYPHAE);
            }

            ItemStack axe = event.getPlayer().getEquipment().getItemInMainHand();
            Damageable axeMeta = (Damageable) axe.getItemMeta();
            axeMeta.setDamage(axeMeta.getDamage() + 1);
            axe.setItemMeta(axeMeta);

        }
    }
}
