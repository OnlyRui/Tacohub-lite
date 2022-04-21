package me.jesusmx.hubcore.commands.impl.media;

import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StoreCommand extends Command {

    private ConfigFile toggle = TacoHubLite.getInstance().getTogglesConfig();
    private ConfigFile config = TacoHubLite.getInstance().getMainConfig();

    public StoreCommand() {
        super("store");
        this.setUsage(ChatColor.RED + "Usage: /store");
    }

    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (toggle.getBoolean("commands.teamspeak")) {
            config.getStringList("commands.teamspeak").stream().map(CC::translate).forEach(sender::sendMessage);
        }
        return false;
    }
}
