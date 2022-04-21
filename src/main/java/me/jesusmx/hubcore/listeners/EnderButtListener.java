package me.jesusmx.hubcore.listeners;

import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.List;

public class EnderButtListener implements Listener {

    private ConfigFile toggle = TacoHubLite.getInstance().getTogglesConfig();
    private ConfigFile config = TacoHubLite.getInstance().getMainConfig();

    @EventHandler
    public void RegisterListeners(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (toggle.getBoolean("normal.butt.enabled")) {
                    ItemStack stack = new ItemStack(Material.valueOf(config.getString("butt.material")));
                    stack.setAmount(config.getInt("butt.amount") - 0);
                    ItemMeta meta = stack.getItemMeta();

                    List<String> lore = new ArrayList<String>();
                    for (String add : config.getStringList("butt.lore")) {
                        lore.add(CC.translate(add));
                    }
                    meta.setLore(lore);
                    meta.setDisplayName(CC.translate(config.getString("butt.name")));
                    stack.setItemMeta(meta);
                    player.getInventory().setItem(config.getInt("butt.slot"), stack);
                    player.updateInventory();
                }
            }
        } .runTaskLater(TacoHubLite.getInstance(), 1);
    }

    @EventHandler
    public void onPearl(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL) {
                event.setCancelled(true);
                if (player.isInsideVehicle()) {
                    player.getVehicle().remove();
                }
                event.setUseItemInHand(org.bukkit.event.Event.Result.DENY);
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                if(toggle.getBoolean("normal.butt.ride")) {
                    EnderPearl pearl = player.launchProjectile(EnderPearl.class);
                    pearl.setPassenger(player);
                    pearl.setVelocity(player.getLocation().getDirection().normalize().multiply(3F));
                } else {
                    player.setVelocity(player.getLocation().getDirection().multiply(2.5f));
                }
                player.playSound(player.getLocation(), Sound.valueOf(config.getString("butt.sound")), 2.0f, 1.0f);
                player.spigot().setCollidesWithEntities(false);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        event.getDismounted().remove();
    }

}
