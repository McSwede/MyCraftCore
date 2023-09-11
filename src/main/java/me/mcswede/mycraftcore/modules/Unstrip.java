package me.mcswede.mycraftcore.modules;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

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

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && axeArray.contains(event.getPlayer().getEquipment().getItemInMainHand().getType())) {

            Block block = event.getClickedBlock();
            Location blockLoc = Objects.requireNonNull(block).getLocation();

            if(!Objects.requireNonNull(block.getType().getBlockTranslationKey()).contains("stripped")) {
                return;
            }
            Material mat = Material.valueOf(block.getType().name().replace("STRIPPED_", ""));
            Axis axis = ((Orientable) block.getBlockData()).getAxis();
            block.setType(mat);
            Orientable orientable = (Orientable) block.getBlockData();
            if (!orientable.getAxis().equals(axis)) {
                orientable.setAxis(axis);
                block.setBlockData(orientable);
            }
            event.getPlayer().playSound(blockLoc, Sound.ITEM_AXE_STRIP , SoundCategory.BLOCKS, 1f, 1f);
        }
    }
}
