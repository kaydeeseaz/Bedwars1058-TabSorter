package com.kaydeesea.bw1058tabsorter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.kaydeesea.bw1058tabsorter.commands.ReloadCommand;
import com.kaydeesea.bw1058tabsorter.tabs.PacketListener;
import com.kaydeesea.bw1058tabsorter.task.TabTask;
import com.kaydeesea.bw1058tabsorter.tabs.InGameTAB;
import com.kaydeesea.bw1058tabsorter.tabs.MainListener;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {
   public HashMap<Player, Integer> serialID;
   public HashMap<Player, String> lastTeamName;
   public static Main plugin;

   public static void runBukkitTask(final Runnable e, long time) {
      (new BukkitRunnable() {
         public void run() {
            e.run();
         }
      }).runTaskLater(plugin, time);
   }

   public void onEnable() {
      this.saveDefaultConfig();
      plugin = this;
      this.serialID = new HashMap<>();
      this.lastTeamName = new HashMap<>();
      this.getServer().getPluginManager().registerEvents(new InGameTAB(), this);
      this.getServer().getPluginManager().registerEvents(new MainListener(), this);
      ProtocolLibrary.getProtocolManager().addPacketListener(new PacketListener(this, new PacketType[]{Server.SCOREBOARD_TEAM}));
      this.getCommand("bwtabreload").setExecutor(new ReloadCommand());
      TabTask.start();
   }

   public void onDisable() {
      TabTask.end();
   }

   public static String parsePAPI(Player p, String s) {
      return plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") == null ? s : PlaceholderAPI.setPlaceholders(p, s);
   }

   public static String List2String(List<String> list) {
      return String.join("\n", list);
   }

   public void reload() {
      this.reloadConfig();
   }

   public static void setPlayerTabListInfo(Player player, String headertext, String footertext) {
      CraftPlayer craftplayer = (CraftPlayer)player;
      PlayerConnection connection = craftplayer.getHandle().playerConnection;
      if (headertext == null) {
         headertext = "";
      }

      if (footertext == null) {
         footertext = "";
      }

      IChatBaseComponent header = ChatSerializer.a("{\"translate\": \"" + headertext + "\"}");
      IChatBaseComponent footer = ChatSerializer.a("{\"translate\": \"" + footertext + "\"}");
      PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(header);

      try {
         Field headerField = packet.getClass().getDeclaredField("b");
         headerField.setAccessible(true);
         headerField.set(packet, footer);
         headerField.setAccessible(false);
      } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException var9) {
         var9.printStackTrace();
      }

      connection.sendPacket(packet);
   }
}
