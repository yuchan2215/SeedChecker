package jp.miyayu.seedchecker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        new BukkitRunnable(){
            @Override
            public void run() {
                try(Connection connection = DataBaseUtil.getSQLConnection()){
                    assert connection != null;
                    Statement statement = connection.createStatement();
                    String uuid = event.getPlayer().getUniqueId().toString();
                    ResultSet resultSet = statement.executeQuery("SELECT * from users WHERE uuid = '"+uuid+"'");
                    if(resultSet.next()){
                        long leaveSeed = resultSet.getLong("leaveseed");
                        long joinSeed = event.getPlayer().getLocation().getWorld().getSeed();
                        if(leaveSeed != joinSeed){
                            event.getPlayer().sendMessage(ChatColor.AQUA + "ログアウトしたワールドにリセットがかかっていたため、メインワールドにリスポーンしました");
                            Location oldLocation = event.getPlayer().getLocation();
                            String world = oldLocation.getWorld().getName();
                            String x = oldLocation.getBlockX()+"";
                            String y = oldLocation.getBlockY()+"";
                            String z = oldLocation.getBlockZ()+"";
                            String name = event.getPlayer().getName();
                            BroadcastUtils.broadcastMessage(String.format(ChatColor.GRAY + "%sは%s(%s,%s,%s)でログアウトしましたが、シード値の変更を検知したため初期スポーン地点に転送されました",
                                    name,world,x,y,z));
                            Location spawn = Bukkit.getWorld("world").getSpawnLocation();
                            event.getPlayer().teleport(spawn);
                            event.getPlayer().setFireTicks(0);
                        }

                    }
                }catch(Exception e){
                    e.printStackTrace();
                    BroadcastUtils.broadcastMessage(ChatColor.GOLD  + "[SeedChecker]" + ChatColor.RED + "データベースへの接続に失敗しました");
                }
            }
        }.runTaskAsynchronously(MainClass.instance);

    }
}
