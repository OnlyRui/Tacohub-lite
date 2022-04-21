package me.jesusmx.hubcore.util.bungee;

import me.jesusmx.hubcore.TacoHubLite;

public class BungeeUpdateTask implements Runnable {
    @Override
    public void run() {
        TacoHubLite.getInstance().getOnlinePlayers().stream().findFirst().ifPresent(BungeeListener::updateCount);
    }
}
