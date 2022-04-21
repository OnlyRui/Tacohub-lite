package me.jesusmx.hubcore.listeners;

import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJumpListener implements Listener {

    private ConfigFile config = TacoHubLite.getInstance().getMainConfig();
    private ConfigFile toggle = TacoHubLite.getInstance().getTogglesConfig();

    @EventHandler
    public void onPlayerJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if(player.getGameMode() == GameMode.CREATIVE) return;
        if (toggle.getBoolean("normal.double-jump"))
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
        event.setCancelled(true);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setVelocity(player.getLocation().getDirection().multiply(config.getDouble("double-jump.velocity")).setY(1));
        player.playSound(player.getLocation(), Sound.valueOf(config.getString("double-jump.sound").toUpperCase()), 1.0F, 1.0F);
        player.getWorld().spigot().playEffect(player.getLocation(), Effect.valueOf(config.getString("double-jump.effect").toUpperCase()), 26, 0, 0.2F, 0.5F, 0.2F, 0.2F, 12, 387);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(player.getGameMode() == GameMode.CREATIVE) return;
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            player.setAllowFlight(true);
        }
    }
}
