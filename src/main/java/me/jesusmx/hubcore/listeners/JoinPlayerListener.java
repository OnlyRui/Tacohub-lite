package me.jesusmx.hubcore.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinPlayerListener implements Listener {

    private ConfigFile toggle = TacoHubLite.getInstance().getTogglesConfig();
    private ConfigFile config = TacoHubLite.getInstance().getMainConfig();

    @EventHandler
    public void RegisterSelector(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (toggle.getBoolean("normal.server-selector.enabled")) {
                    ItemStack stack = new ItemStack(Material.getMaterial(config.getString("server-selector.material")), 1, (short) config.getInt("server-selector.data"));
                    ItemMeta meta = stack.getItemMeta();
                    meta.setDisplayName(CC.translate(config.getString("server-selector.name")));
                    meta.setLore(CC.translate(config.getStringList("server-selector.lore")));
                    stack.setItemMeta(meta);
                    player.getInventory().setItem(config.getInt("server-selector.slot"), stack);
                }
            }
        } .runTaskLater(TacoHubLite.getInstance(), 0);
    }

    @EventHandler
    public void RegisterListeners(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        player.getInventory().clear();
        player.teleport(player.getWorld().getSpawnLocation());
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        if (toggle.getBoolean("normal.join-message")) {
            config.getStringList("join-message.lines").stream()
                    .map(line -> PlaceholderAPI.setPlaceholders(player, line))
                    .map(line -> line.replace("%rank%", TacoHubLite.getInstance().getPermissionCore().getRank(player)))
                    .map(line -> line.replace("%rankcolor%", TacoHubLite.getInstance().getPermissionCore().getRankColor(player)))
                    .map(line -> line.replace("%player%", player.getName()))
                    .forEach(m -> player.sendMessage(CC.translate(m)));
        }

        if (toggle.getBoolean("normal.join-sound")) {
            player.playSound(player.getLocation(), Sound.valueOf(config.getString("join-sound.sound").toUpperCase()), 1.0F, 1.0F);
        }
        player.updateInventory();
    }

    @EventHandler
    public void JoinSpeed(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        if (toggle.getBoolean("normal.join-speed")) {
            player.setWalkSpeed((float) config.getDouble("join-speed.velocity"));
        } else {
            player.setWalkSpeed(0.2F);
        }
    }
}