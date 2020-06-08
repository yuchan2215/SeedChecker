package jp.miyayu.seedchecker;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.Statement;

public final class MainClass extends JavaPlugin {
    public static MainClass instance;
    @Override
    public void onEnable() {
        instance = this;
        try(Connection connection = DataBaseUtil.getSQLConnection()){
            assert connection != null;
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users(uuid text,leaveseed INTEGER)");
            statement.close();
        }catch(Exception e){
            e.printStackTrace();
            BroadcastUtils.broadcastMessage(ChatColor.GOLD  + "[SeedChecker]" + ChatColor.RED + "データベースへの接続に失敗しました");
        }

        getServer().getPluginManager().registerEvents(new PlayerLeave(),this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(),this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
