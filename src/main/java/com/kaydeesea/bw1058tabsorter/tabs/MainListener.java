package com.kaydeesea.bw1058tabsorter.tabs;

import com.andrei1058.bedwars.api.events.player.PlayerBedBreakEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.kaydeesea.bw1058tabsorter.Main;
import com.kaydeesea.bw1058tabsorter.task.TabTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListener implements Listener {
   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent e) {
      Player p = e.getPlayer();
      int i = 0;
      if (!Main.plugin.serialID.containsKey(p)) {
         while(Main.plugin.serialID.containsValue(i)) {
            ++i;
         }

         Main.plugin.serialID.put(p, i);
      }
   }

   @EventHandler
   public void onPlayerLeave(PlayerQuitEvent e) {
      Player p = e.getPlayer();
      Main.plugin.serialID.remove(p);
      Main.plugin.lastTeamName.remove(p);
   }

   @EventHandler
   public void onWorldChange(PlayerChangedWorldEvent e) {
      TabTask.update(e.getPlayer());
   }

   @EventHandler
   public void onKill(PlayerKillEvent e) {
      if (e.getKiller() != null) {
         TabTask.update(e.getKiller());
      }

      TabTask.update(e.getVictim());
   }

   @EventHandler
   public void onBed(PlayerBedBreakEvent e) {
      TabTask.update(e.getPlayer());
   }
}
