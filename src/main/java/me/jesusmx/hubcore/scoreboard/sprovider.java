package me.jesusmx.hubcore.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.bungee.BungeeListener;
import me.jesusmx.hubcore.util.files.ConfigFile;
import me.jesusmx.hubcore.util.scoreboard.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class sprovider implements AssembleAdapter {

    private ConfigFile config = TacoHubLite.getInstance().getScoreboardConfig();

    @Override
    public String getTitle(Player player) {
        return CC.translate(config.getString("scoreboard.title"));
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> toReturn = new ArrayList<>();
        if (TacoHubLite.getInstance().getQueueManager().inQueue(player)) {
            config.getStringList("scoreboard.mode.queue").stream()
                    .map(CC::translate)
                    .map(line -> PlaceholderAPI.setPlaceholders(player, line))
                    .map(line -> line.replace("%players%", String.valueOf(BungeeListener.PLAYER_COUNT)))
                    .map(line -> line.replace("%rank%", TacoHubLite.getInstance().getPermissionCore().getRank(player)))
                    .map(line -> line.replace("%rank-color%", TacoHubLite.getInstance().getPermissionCore().getRankColor(player)))
                    .map(line -> line.replace("%server-queue%", String.valueOf(TacoHubLite.getInstance().getQueueManager().getQueueIn(player))))
                    .map(line -> line.replace("%position-queue%", String.valueOf(TacoHubLite.getInstance().getQueueManager().getPosition(player))))
                    .map(line -> line.replace("%size-queue%", String.valueOf(TacoHubLite.getInstance().getQueueManager().getInQueue(TacoHubLite.getInstance().getQueueManager().getQueueIn(player)))))
                    .forEach(toReturn::add);
        } else {
            config.getStringList("scoreboard.mode.normal").stream()
                    .map(CC::translate)
                    .map(line -> PlaceholderAPI.setPlaceholders(player, line))
                    .map(line -> line.replace("%players%", String.valueOf(BungeeListener.PLAYER_COUNT)))
                    .map(line -> line.replace("%rank%", TacoHubLite.getInstance().getPermissionCore().getRank(player)))
                    .map(line -> line.replace("%rank-color% ", TacoHubLite.getInstance().getPermissionCore().getRankColor(player)))
                    .forEach(toReturn::add);
        }
        return toReturn;
    }
}