package me.jesusmx.hubcore.commands.impl.media;

import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class TwitterCommand extends Command {

    private ConfigFile toggle = TacoHubLite.getInstance().getTogglesConfig();
    private ConfigFile config = TacoHubLite.getInstance().getMainConfig();

    public TwitterCommand() {
        super("twitter");
        this.setAliases( Arrays.asList("tw"));
        this.setUsage(ChatColor.RED + "Usage: /twitter");
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (toggle.getBoolean("commands.twitter")) {
            config.getStringList("commands.twitter").stream().map(CC::translate).forEach(sender::sendMessage);
        }
        return false;
    }
}
