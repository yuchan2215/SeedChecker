package jp.miyayu.seedchecker;

import org.bukkit.Bukkit;
import org.bukkit.permissions.ServerOperator;

public class BroadcastUtils {
    public static void broadcastMessage(String message){
        Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> player.sendMessage(message));
        Bukkit.getLogger().info(message);
    }
}
