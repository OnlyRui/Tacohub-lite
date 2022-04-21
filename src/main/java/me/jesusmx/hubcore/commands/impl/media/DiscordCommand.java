package me.jesusmx.hubcore.commands.impl.media;

import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class DiscordCommand extends Command {

    private ConfigFile toggle = TacoHubLite.getInstance().getTogglesConfig();
    private ConfigFile config = TacoHubLite.getInstance().getMainConfig();

    public DiscordCommand() {
        super("discord");
        this.setAliases(Arrays.asList("dc"));
        this.setUsage(ChatColor.RED + "Usage: /discord");
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (toggle.getBoolean("commands.discord")) {
            config.getStringList("commands.discord").stream().map(CC::translate).forEach(sender::sendMessage);
        }
        return false;
    }
}