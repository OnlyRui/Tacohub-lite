package me.jesusmx.hubcore.tablist;

import me.clip.placeholderapi.PlaceholderAPI;
import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.queue.QueueManager;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.bungee.BungeeListener;
import me.jesusmx.hubcore.util.files.ConfigFile;
import me.jesusmx.hubcore.util.tablist.shared.entry.TabElement;
import me.jesusmx.hubcore.util.tablist.shared.entry.TabElementHandler;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class tprovider implements TabElementHandler {

    private ConfigFile config = TacoHubLite.getInstance().getTablistConfig();

    @Override
    public TabElement getElement(Player player) {
        TabElement element = new TabElement();
        element.setHeader(CC.translate(config.getString("tablist.header").replace("<line>", "\n")));
        element.setFooter(CC.translate(config.getString("tablist.footer").replace("<line>", "\n")));
        List list = Arrays.asList((Object[])new String[]{"left", "middle", "right", "far-right"});
        for (int i = 0; i < 4; ++i) {
            String s = (String)list.get(i);
            for (int l = 0; l < 20; ++l) {
                String str = PlaceholderAPI.setPlaceholders(player, config.getString(String.valueOf(new StringBuilder().append("tablist.").append(s).append(".").append(l + 1)))
                        .replace("%player%", player.getDisplayName())
                        .replace("%rank%", TacoHubLite.getInstance().getPermissionCore().getRank(player)))
                        .replace("%players%", String.valueOf(BungeeListener.PLAYER_COUNT))
                        .replace("%rank-color%", TacoHubLite.getInstance().getPermissionCore().getRankColor(player));
                element.add(i, l, str);
            }
        }
        return element;
    }
}
