package jp.miyayu.seedchecker;

import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtil {
    private static Connection connection;
    public static Connection getSQLConnection() throws SQLException{
        try{
            if(connection != null && !connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");MainClass.instance.getDataFolder().mkdir();
            connection = DriverManager.getConnection("jdbc:sqlite:" + MainClass.instance.getDataFolder().getPath() + "\\data.db");
            return connection;
        }catch(Exception e){
            e.printStackTrace();
            BroadcastUtils.broadcastMessage(ChatColor.GOLD  + "[SeedChecker]" + ChatColor.RED + "データベースへの接続に失敗しました");
            return null;
        }
    }
}
