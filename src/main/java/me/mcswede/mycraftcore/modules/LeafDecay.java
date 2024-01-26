package me.mcswede.mycraftcore.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.mcswede.mycraftcore.MyCraftCore;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class LeafDecay implements Listener {
    private final MyCraftCore plugin;
    public LeafDecay(MyCraftCore plugin) {
        this.plugin = plugin;
    }
    public static final List<Block> scheduledBlocks = new ArrayList<>();
    private static final List<BlockFace> NEIGHBORS = Arrays
            .asList(BlockFace.UP,
                    BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
                    BlockFace.DOWN);

    /**
     * Whenever a player breaks a log or leaves block, there is a chance
     * that its surrounding blocks should also decay.  We could just
     * wait for the first leaves to decay naturally, but this way, the
     * instant feedback will avoid confusion for players.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        onBlockRemove(event.getBlock(), plugin.getConfig().getLong("leafdecay.breakDelay", 5));
    }

    /**
     * Leaves decay has a tendency to cascade.  Whenever leaves decay,
     * we want to check its neighbors to find out if they will also
     * decay.
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLeavesDecay(LeavesDecayEvent event) {
        onBlockRemove(event.getBlock(), plugin.getConfig().getLong("leafdecay.decayDelay", 5));
    }

    /**
     * Check if block is either leaves or a log and whether any of the
     * blocks surrounding it are non-persistent leaves blocks.  If so,
     * schedule their respective removal via
     * {@link #decay(Block) block()}.  The latter will perform all
     * necessary checks, including distance.
     *
     * @param oldBlock the block
     * @param delay the delay of the scheduled check, in ticks
     */
    private void onBlockRemove(final Block oldBlock, long delay) {
        if (!Tag.LOGS.isTagged(oldBlock.getType())
                && !Tag.LEAVES.isTagged(oldBlock.getType())) {
            return;
        }
        // No return
        Collections.shuffle(NEIGHBORS);
        for (BlockFace neighborFace: NEIGHBORS) {
            final Block block = oldBlock.getRelative(neighborFace);
            if (!Tag.LEAVES.isTagged(block.getType())) continue;
            Leaves leaves = (Leaves) block.getBlockData();
            if (leaves.isPersistent()) continue;
            if (scheduledBlocks.contains(block)) continue;
            if (plugin.getConfig().getBoolean("leafdecay.oneByOne", false)) {
                if (scheduledBlocks.isEmpty()) {
                    plugin.getServer().getScheduler().runTaskLater(plugin, this::decayOne, delay);
                }
                scheduledBlocks.add(block);
            } else {
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> decay(block), delay);
            }
            scheduledBlocks.add(block);
        }
    }

    /**
     * Decay if it is a leaves block and its distance the nearest log
     * block is 7 or greater.
     *
     * This method may only be called by a scheduler if the given
     * block has previously been added to the scheduledBlocks set,
     * from which it will be removed.
     *
     * This method calls {@link LeavesDecayEvent} and will not act if
     * the event is cancelled.
     * @param block The block
     *
     * @return true if the block was decayed, false otherwise.
     */
    private boolean decay(Block block) {
        if (!scheduledBlocks.remove(block)) return false;
        if (!block.getWorld().isChunkLoaded(block.getX() >> 4, block.getZ() >> 4)) return false;
        if (!Tag.LEAVES.isTagged(block.getType())) return false;
        Leaves leaves = (Leaves) block.getBlockData();
        if (leaves.isPersistent()) return false;
        if (leaves.getDistance() < 7) return false;
        LeavesDecayEvent event = new LeavesDecayEvent(block);
        plugin.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;
        if (plugin.getConfig().getBoolean("leafdecay.particles", true)) {
            block.getWorld()
                    .spawnParticle(Particle.BLOCK_CRACK,
                            block.getLocation().add(0.5, 0.5, 0.5),
                            8, 0.2, 0.2, 0.2, 0,
                            block.getType().createBlockData());
        }
        if (plugin.getConfig().getBoolean("leafdecay.sound", true)) {
            block.getWorld().playSound(block.getLocation(),
                    Sound.BLOCK_GRASS_BREAK,
                    SoundCategory.BLOCKS, 0.05f, 1.2f);
        }
        block.breakNaturally();
        return true;
    }

    /**
     * Decay one block from the list of scheduled blocks. Schedule the
     * same function again if the list is not empty.
     * This gets called if OneByOne is activated in the
     * config. Therefore, we wait at least one tick.
     *
     * This could undermine the BlockDelay if the DecayDelay
     * significantly smaller and the list devoid of valid leaf blocks.
     */
    private void decayOne() {
        boolean decayed;
        do {
            if (scheduledBlocks.isEmpty()) return;
            Block block = scheduledBlocks.get(0);
            decayed = decay(block); // Will remove block from list.
        } while (!decayed);
        if (!scheduledBlocks.isEmpty()) {
            long delay = plugin.getConfig().getLong("leafdecay.decayDelay", 5);
            if (delay <= 0) delay = 1L;
            plugin.getServer().getScheduler().runTaskLater(plugin, this::decayOne, delay);
        }
    }
}
