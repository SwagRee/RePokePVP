package io.github.swagree.pokecard.ymlmessage;

import io.github.swagree.pokecard.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class YmlMessage {
    public static File file = new File(Main.plugin.getDataFolder(),"message.yml");
    public static YamlConfiguration message = YamlConfiguration.loadConfiguration(file);

    public static void sendColorMessage(Player player, String cardName){
        player.sendMessage(transToColor(YmlMessage.message.getString("Message."+cardName)));
    }
    public static void sendColorWarn(Player player,String cardName){
        player.sendMessage(transToColor(YmlMessage.message.getString("Warn."+cardName)));
    }
    public static String transToColor(String message){
        message.replace("&","§");
        return message;
    }
}
