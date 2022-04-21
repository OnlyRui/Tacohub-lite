package me.jesusmx.hubcore.listeners;

import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {

    private ConfigFile toggle = TacoHubLite.getInstance().getTogglesConfig();
    private ConfigFile config = TacoHubLite.getInstance().getSettingsConfig();

    @EventHandler
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        if (toggle.getBoolean("world.no-mobs-spawn.enabled")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (toggle.getBoolean("world.no-damage.enabled")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntitySpawn(EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        if (!event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) event.setCancelled(true);
    }

    @EventHandler
    private void onPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (toggle.getBoolean("world.no-drop-items.enabled")) {
            event.setCancelled(true);
        }
        player.sendMessage(CC.translate(config.getString("world.no-drop-items.message")));
    }


    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (toggle.getBoolean("world.no-place-blocks.enabled")) {
            if (!player.hasPermission("hubcore.command.place") || !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(true);
            }
            player.sendMessage(CC.translate(config.getString("world.no-place-blocks.message")));
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (toggle.getBoolean("world.no-break-blocks.enabled")) {
            if (!player.hasPermission("hubcore.command.break") || !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                event.setCancelled(true);
            }
            player.sendMessage(CC.translate(config.getString("world.no-break-blocks.message")));
        }
    }

    @EventHandler
    private void bucketEmpty(PlayerBucketFillEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void entityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
            if (toggle.getBoolean("world.void-teleport.enabled") && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                event.getEntity().teleport(event.getEntity().getWorld().getSpawnLocation());
            }
        }
    }


    @EventHandler
    private void disablePhysicsBlocks(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onPopDamager(EntityDamageByEntityEvent event) {
        Player player;
        Entity damager = event.getDamager();
        boolean b = event.getDamager() == null || event.getDamager() instanceof EnderPearl;
        boolean e = false;
        if (!toggle.getBoolean("world.minehq-hideplayers.enabled")) return;
        if (Bukkit.getVersion().contains("1.7")) {
            if (damager instanceof org.bukkit.craftbukkit.v1_7_R4.entity.CraftEnderPearl) {
                e = true;
            }
        } else if (Bukkit.getVersion().contains("1.8")) {
            if (damager instanceof org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderPearl) {
                e = true;
            }
            if (b && e) {
                player = null;
            } else {
                player = (Player) event.getDamager();
            }
            Player damaged = (Player) event.getEntity();
            if (damaged != null && player != null &&
                    event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                player.hidePlayer(damaged);
                player.sendMessage(CC.translate(config.getString("world.minehq-hideplayers.message")));
                player.playSound(player.getLocation(), Sound.valueOf(config.getString("world.minehq-hideplayers.sound")), 2.0f, 1.0f);
            }
            event.setCancelled(true);
        }
    }
}
