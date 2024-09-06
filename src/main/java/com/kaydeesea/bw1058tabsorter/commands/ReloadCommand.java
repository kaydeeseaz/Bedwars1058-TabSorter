package com.kaydeesea.bw1058tabsorter.commands;

import com.kaydeesea.bw1058tabsorter.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
   public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
      if (!commandSender.hasPermission("bwTab.admin")) {
         commandSender.sendMessage(ChatColor.RED + "You don't have the required permission to execute this command.");
      } else {
         Main.plugin.reload();
         commandSender.sendMessage(ChatColor.GREEN + "BedWars1058-TabSorter Reloaded Successfully.");
      }
       return true;
   }
}
