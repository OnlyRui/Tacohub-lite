package me.jesusmx.hubcore.commands.impl.spawn;

import me.jesusmx.hubcore.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SetSpawnCommand extends Command {

   public SetSpawnCommand() {
      super("discord");
      this.setAliases( Arrays.asList("dc"));
      this.setUsage(ChatColor.RED + "Usage: /discord");
   }

   public boolean execute(CommandSender commandSender, String s, String[] strings) {
      Player player = (Player) commandSender;
      if (!player.hasPermission("hubcore.setspawn")) {
         player.sendMessage(CC.translate("&cYou dont have permission to use this command."));
      } else {
         World world = player.getWorld();
         Location loc = player.getLocation();
         world.setSpawnLocation(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ());
         player.sendMessage(CC.translate("&aSuColoressfully set the spawn point."));
      }
      return false;
   }
}