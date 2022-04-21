package me.jesusmx.hubcore.commands.impl.media;

import me.jesusmx.hubcore.TacoHubLite;
import me.jesusmx.hubcore.util.CC;
import me.jesusmx.hubcore.util.files.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class TelegramCommand extends Command {

    private ConfigFile toggle = TacoHubLite.getInstance().getTogglesConfig();
    private ConfigFile config = TacoHubLite.getInstance().getMainConfig();

    public TelegramCommand() {
        super("telegram");
        this.setAliases( Arrays.asList("tl", "tgm"));
        this.setUsage(ChatColor.RED + "Usage: /telegram");
    }

    public boolean execute(CommandSender sender, String s, String[] strings) {
        if (toggle.getBoolean("commands.telegram")) {
            config.getStringList("commands.telegram").stream().map(CC::translate).forEach(sender::sendMessage);
        }
        return false;
    }
}
