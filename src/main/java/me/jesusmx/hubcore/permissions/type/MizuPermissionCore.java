package me.jesusmx.hubcore.permissions.type;

import com.broustudio.MizuAPI.MizuAPI;
import me.jesusmx.hubcore.permissions.PermissionCore;
import org.bukkit.entity.Player;

public class MizuPermissionCore implements PermissionCore {
    @Override
    public String getRankColor(Player player) {
        return MizuAPI.getAPI().getRankColor(MizuAPI.getAPI().getRank(player.getUniqueId()));
    }

    @Override
    public String getRank(Player player) {
        return MizuAPI.getAPI().getRank(player.getUniqueId());
    }

}
