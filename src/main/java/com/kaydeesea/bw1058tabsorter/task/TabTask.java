package com.kaydeesea.bw1058tabsorter.task;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.kaydeesea.bw1058tabsorter.Main;

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TabTask {
   public static int taskID = -1;

   public static void start() {
      taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {
         for (Player player : Bukkit.getOnlinePlayers()) {
            update(player);
         }

      }, 1L, Main.plugin.getConfig().getInt("header-footer.refresh-interval"));
   }

   public static void update(Player player) {
      IArena a = Arena.getArenaByPlayer(player);
      List<String> header;
      List<String> footer;
      if (a == null) {
         header = Main.plugin.getConfig().getStringList("header-footer.noningame.header");
         footer = Main.plugin.getConfig().getStringList("header-footer.noningame.footer");
      } else {
         header = Main.plugin.getConfig().getStringList("header-footer.ingame.header");
         footer = Main.plugin.getConfig().getStringList("header-footer.ingame.footer");
      }

      StringBuilder h = new StringBuilder();

      for(int i = 0; i < header.size(); ++i) {
         String s = header.get(i);
         if (i + 1 == header.size()) {
            h.append(s);
         } else {
            h.append(s).append("\n");
         }
      }

      StringBuilder f = new StringBuilder();

      for(int i = 0; i < footer.size(); ++i) {
         String s = footer.get(i);
         if (i + 1 == footer.size()) {
            f.append(s);
         } else {
            f.append(s).append("\n");
         }
      }

      h = new StringBuilder(h.toString().replaceAll("&", "§"));
      h = new StringBuilder(PlaceholderAPI.setPlaceholders(player, h.toString()));
      f = new StringBuilder(f.toString().replaceAll("&", "§"));
      f = new StringBuilder(PlaceholderAPI.setPlaceholders(player, f.toString()));
      Main.setPlayerTabListInfo(player, h.toString(), f.toString());
   }

   public static void end() {
      if (taskID != -1) {
         Bukkit.getScheduler().cancelTask(taskID);
      }

   }
}
