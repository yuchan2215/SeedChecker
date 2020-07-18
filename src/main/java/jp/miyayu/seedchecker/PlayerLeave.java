package jp.miyayu.seedchecker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PlayerLeave implements Listener {
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        new BukkitRunnable() {
            @Override
            public void run() {
                playerQuit(event.getPlayer(),false);
            }
        }.runTaskAsynchronously(MainClass.instance);
    }

    public static void playerQuit(Player player,boolean isFinish){
                try{
                    Connection connection = DataBaseUtil.getSQLConnection();
                    assert connection != null;
                    Statement statement = connection.createStatement();
                    String uuid = player.getUniqueId().toString();
                    String seed = player.getLocation().getWorld().getSeed() + "";
                    ResultSet resultSet = statement.executeQuery("SELECT * from users WHERE uuid = '"+uuid+"'");
                    if(resultSet.next()){
                        statement.close();




                        statement = connection.createStatement();
                        statement.executeUpdate("UPDATE users SET leaveseed = '"+seed+"' WHERE uuid = '"+uuid+"'");
                        statement.close();
                    }else{
                        statement.close();
                        statement = connection.createStatement();
                        statement.executeUpdate("INSERT INTO users (uuid,leaveseed) VALUES ('"+uuid+"','"+seed+"')");
                        statement.close();
                    }
                    if(isFinish)
                        Bukkit.getLogger().info(player.getName() + "の退出処理を行いました");
                }catch(Exception e){
                    e.printStackTrace();
                    BroadcastUtils.broadcastMessage(ChatColor.GOLD  + "[SeedChecker]" + ChatColor.RED + "データベースへの接続に失敗しました");
                }
    }

}
