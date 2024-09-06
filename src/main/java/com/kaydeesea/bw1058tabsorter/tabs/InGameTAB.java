package com.kaydeesea.bw1058tabsorter.tabs;

import com.andrei1058.bedwars.api.events.player.PlayerLeaveArenaEvent;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.kaydeesea.bw1058tabsorter.Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InGameTAB implements Listener {
   @EventHandler
   public void onArenaLeave(PlayerLeaveArenaEvent e) {
      List<Player> allPlayers = new ArrayList<>(e.getArena().getPlayers());
      allPlayers.addAll(e.getArena().getSpectators());
      allPlayers.addAll(e.getArena().getLeavingPlayers());

      for (Player p : allPlayers) {
         String teamName = Main.plugin.lastTeamName.get(p);
         if (teamName != null) {
            PacketContainer packet = new PacketContainer(Server.SCOREBOARD_TEAM);
            packet.getStrings().write(0, teamName);
            packet.getIntegers().write(0, 1);

            try {
               ProtocolLibrary.getProtocolManager().sendServerPacket(e.getPlayer(), packet);
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }
      }

   }
}
