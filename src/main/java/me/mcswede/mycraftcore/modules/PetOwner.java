package me.mcswede.mycraftcore.modules;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.*;

public class PetOwner implements Listener {

    private static final Map<EntityType, List<Sound>> patSound = Maps.newHashMap();
    static {
        patSound.put(EntityType.CAT, ImmutableList.of(Sound.ENTITY_CAT_PURR, Sound.ENTITY_CAT_PURREOW));
        patSound.put(EntityType.WOLF, ImmutableList.of(Sound.ENTITY_WOLF_SHAKE, Sound.ENTITY_WOLF_PANT));
        patSound.put(EntityType.PARROT, ImmutableList.of(Sound.ENTITY_PARROT_AMBIENT, Sound.ENTITY_PARROT_FLY));
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        MiniMessage miniMessage = MiniMessage.miniMessage(); // Create the MiniMessage instance
        Player player = event.getPlayer();
        if (!player.hasPermission("mycraftcore.petowner")) {
            return;
        }
        if(event.getRightClicked() instanceof Tameable tameable && tameable.isTamed() && isPattable(tameable)) {
            OfflinePlayer owner = (OfflinePlayer) tameable.getOwner();
            if (owner == player) {
                return;
            }
            if(player.getInventory().getItemInMainHand().getType() == Material.AIR && player.getInventory().getItemInOffHand().getType() == Material.AIR) {
                player.sendMessage(String.valueOf(miniMessage.deserialize("<yellow>You petted " + Objects.requireNonNull(owner).getName() + "'s " + getFriendlyPetName(tameable))));
                final Random r = new Random();
                final List<Sound> sounds = getPatSounds(tameable);
                World world = player.getWorld();
                if (!sounds.isEmpty())
                    world.playSound(tameable.getEyeLocation(), sounds.get(r.nextInt(sounds.size())), 1.0F, 1.0F);
                world.spawnParticle(Particle.HEART, tameable.getEyeLocation(), 1);
            }
        }
    }

    private static String getFriendlyPetName(Tameable entity) {
        switch (entity.getType()) {
            case CAT -> {
                return "Cat";
            }
            case PARROT -> {
                return "Parrot";
            }
            case WOLF -> {
                return "Dog";
            }
            default -> {
                return "pet";
            }
        }
    }

    private static List<Sound> getPatSounds(Entity e) {
        final List<Sound> l = new ArrayList<>();
        for (final Map.Entry<EntityType, List<Sound>> t : patSound.entrySet())
            if (e.getType().equals(t.getKey())) l.addAll(t.getValue());
        return l;
    }

    private static boolean isPattable(Tameable pet) {
        if (pet == null) return false;
        return patSound.containsKey(pet.getType());
    }
}
