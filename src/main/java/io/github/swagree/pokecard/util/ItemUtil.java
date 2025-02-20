package io.github.swagree.pokecard;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

/**
 * 工具类 包含一些处理的方法
 */
public class ItemUtil {

    public static ItemStack addItemToPlayer(int num, String cardName) {
        ItemStack itemStack = new ItemStack(new ItemStack(Material.getMaterial(Main.plugin.getConfig().getInt("id")), num, (short) Main.plugin.getConfig().getInt("data")));
        ItemMeta id = itemStack.getItemMeta();
        id.setDisplayName(Main.plugin.getConfig().getString(cardName+".name").replace("&", "§"));
        ArrayList<String> lore = new ArrayList();
        List<String> stringList = Main.plugin.getConfig().getStringList(cardName + ".lore");
        for(String s : stringList){
            lore.add(s.replace("&", "§"));
        }
        id.setLore(lore);
        itemStack.setItemMeta(id);
        return itemStack;
    }

    public static void takeItem(Player player, ItemStack newItemStack, String cardName) {
        PlayerInventory inventory = player.getInventory();
        if(inventory.getItemInOffHand().isSimilar(newItemStack)){
            player.sendMessage("§b不要把卡片放在副手奥！");
            return;
        }else{
            HashMap<Integer, ItemStack> removedItems = new HashMap<>();
            removedItems = inventory.removeItem(newItemStack);
            if (removedItems.isEmpty()) {
                player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)));
            } else {
                player.sendMessage("§e您的物品不足");
            }
        }
    }

    public static ItemStack createItem(String cardName) {
        ItemStack itemStack = new ItemStack(new ItemStack(Material.getMaterial(Main.plugin.getConfig().getInt("id"))));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Main.plugin.getConfig().getString(cardName+".name").replace("&", "§"));
        ArrayList<String> lore = new ArrayList();
        List<String> stringList = Main.plugin.getConfig().getStringList(cardName + ".lore");
        for(String s : stringList){
            lore.add(s.replace("&", "§"));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


}
