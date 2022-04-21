package me.jesusmx.hubcore.permissions.type;

import club.frozed.core.ZoomAPI;
import me.jesusmx.hubcore.permissions.PermissionCore;
import org.bukkit.entity.Player;

public class ZoomPermissionCore implements PermissionCore {

    @Override
    public String getRankColor(Player player) {
        return null;
    }

    @Override
    public String getRank(Player player) {
        return ZoomAPI.getRankName(player);
    }
}
