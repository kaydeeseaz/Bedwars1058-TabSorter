package com.kaydeesea.bw1058tabsorter.tabs;

import com.andrei1058.bedwars.BedWars;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.kaydeesea.bw1058tabsorter.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PacketListener extends PacketAdapter {
   public PacketListener(Plugin plugin, PacketType... types) {
      super(plugin, types);
   }

   public void onPacketSending(PacketEvent e) {
      if (e.getPacketType() == Server.SCOREBOARD_TEAM) {
         PacketContainer packet = e.getPacket();
         Player playerTarget = Bukkit.getPlayer(packet.getStrings().read(0));
         if (playerTarget == null) {
            return;
         }

         if (!BedWars.getAPI().getArenaUtil().isPlaying(playerTarget)) {
            return;
         }

         if (BedWars.getAPI().getArenaUtil().getArenaByPlayer(playerTarget) == null) {
            return;
         }

         if (BedWars.getAPI().getArenaUtil().getArenaByPlayer(playerTarget).getTeam(playerTarget) == null) {
            return;
         }

         String teamName = BedWars.getAPI().getArenaUtil().getArenaByPlayer(playerTarget).getTeam(playerTarget).getColor().name();
         int position = Main.plugin.getConfig().getStringList("Sorting-Teams").indexOf(teamName);
         String newTeam = String.format("%02d", position) + Main.plugin.serialID.get(playerTarget);
         Main.plugin.lastTeamName.put(playerTarget, newTeam);
         packet.getStrings().write(0, newTeam);
      }

   }
}
