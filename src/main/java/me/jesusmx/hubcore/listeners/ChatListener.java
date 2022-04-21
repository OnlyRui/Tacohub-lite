package me.jesusmx.hubcore.listeners;

import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private ConfigFile config = TacoHubLite.getInstance().getMainConfig();
    private ConfigFile toggle = TacoHubLite.getInstance().getTogglesConfig();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (toggle.getBoolean("normal.chat.muted")) {
            if(event.getPlayer().hasPermission("hubcore.chat-bypass")) return;
            event.getPlayer().sendMessage(CC.translate(config.getString("chat-format.muted.message")));
            event.setCancelled(true);
        } else if (toggle.getBoolean("normal.chat.normal")) {
            String msg = ChatColor.translateAlternateColorCodes('&',
                    config.getString("chat-format.normal.message")
                            .replace("%player%", player.getName())
                            .replace("%message%", event.getMessage())
                            .replace("%suffix%", TacoHubLite.chat.getPlayerSuffix(player))
                            .replace("%prefix%", TacoHubLite.chat.getPlayerPrefix(player)));
            event.setCancelled(true);
            String finalMsg = msg;
            TacoHubLite.getInstance().getOnlinePlayers().forEach(p -> p.sendMessage(finalMsg));
        }
    }
}
