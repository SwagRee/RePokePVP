package io.github.swagree.pokecard.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.swagree.pokecard.Event;
import io.github.swagree.pokecard.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class GuiModifyGrowth implements Listener {
    private static final List<Integer> invSlot = Arrays.asList(2, 4, 6, 20, 22, 24, 38, 40, 42);
    private static final String TITLE_PREFIX = "§b选择一项体型修改吧";
    private static final String cardName = "anyGrowth";

    private static GuiModifyGrowth guiModifyGrowth; // 声明为静态变量
    private int myParameter;

    public void setParameter(int value) {
        this.myParameter = value;
    }

    public int getParameter() {
        return myParameter;
    }


    public static void Gui(Player player, int slot) {
        guiModifyGrowth = new GuiModifyGrowth();

        Inventory inv = Bukkit.createInventory(null, 45, TITLE_PREFIX);

        final ArrayList<ItemStack> itemStacks = new ArrayList<>();

        // 属性名称和对应的lore值
        HashMap<String, String> attributeLoreMap = new LinkedHashMap<>();
        attributeLoreMap.put("袖珍", "§b修改为袖珍");
        attributeLoreMap.put("迷你", "§b修改为迷你");
        attributeLoreMap.put("侏儒", "§b修改为侏儒");
        attributeLoreMap.put("较小", "§b修改为较小");
        attributeLoreMap.put("普通", "§b修改为普通");
        attributeLoreMap.put("高大", "§b修改为高大");
        attributeLoreMap.put("巨人", "§b修改为巨人");
        attributeLoreMap.put("庞大", "§b修改为庞大");
        attributeLoreMap.put("巨大", "§b修改为巨大");


        for (Map.Entry<String, String> entry : attributeLoreMap.entrySet()) {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§e修改体型为" + entry.getKey());

            ArrayList<String> lores = new ArrayList<>();
            lores.add(entry.getValue());
            itemMeta.setLore(lores);

            itemStack.setItemMeta(itemMeta);
            itemStacks.add(itemStack);
        }

        for (int i = 0; i < itemStacks.size(); i++) {
            if (i < inv.getSize()) {
                inv.setItem(invSlot.get(i), itemStacks.get(i));
            }
        }

        guiModifyGrowth.setParameter(slot);
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title.equalsIgnoreCase(TITLE_PREFIX)) {
            Player player = (Player) event.getWhoClicked();
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(player.getUniqueId());

            int slot = event.getSlot();
            event.setCancelled(true);

            final Event e = new Event();
            ItemStack newItemStack = e.createItem(cardName);
            List<String> newItemLore = new ArrayList<>(newItemStack.getItemMeta().getLore());
            List<String> newItemLoreWithoutColor = new ArrayList<>();
            for (String line : newItemLore) {
                newItemLoreWithoutColor.add(e.stripColorCodes(line));
            }
            // 根据点击的位置执行相应的命令
            switch (slot) {
                case 2:
                    if (!party.get(guiModifyGrowth.getParameter()).getGrowth().equals(EnumGrowth.Microscopic)) {
                        party.get(guiModifyGrowth.getParameter()).setGrowth(EnumGrowth.Microscopic);
                        takeItem(player, newItemStack,EnumGrowth.Microscopic.getLocalizedName());
                    } else {
                        player.sendMessage("§4您这只精灵的体型已经是袖珍了");
                    }
                    break;
                case 4:
                    if (!party.get(guiModifyGrowth.getParameter()).getGrowth().equals(EnumGrowth.Pygmy)) {
                        party.get(guiModifyGrowth.getParameter()).setGrowth(EnumGrowth.Pygmy);
                        takeItem(player, newItemStack,EnumGrowth.Pygmy.getLocalizedName());
                    } else {
                        player.sendMessage("§4您这只精灵的体型已经是迷你了");
                    }
                    break;
                case 6:
                    if (!party.get(guiModifyGrowth.getParameter()).getGrowth().equals(EnumGrowth.Runt)) {
                        party.get(guiModifyGrowth.getParameter()).setGrowth(EnumGrowth.Runt);
                        takeItem(player, newItemStack,EnumGrowth.Runt.getLocalizedName());
                    } else {
                        player.sendMessage("§4您这只精灵的体型已经是侏儒了");
                    }
                    break;
                case 20:
                    if (!party.get(guiModifyGrowth.getParameter()).getGrowth().equals(EnumGrowth.Small)) {
                        party.get(guiModifyGrowth.getParameter()).setGrowth(EnumGrowth.Small);
                        takeItem(player, newItemStack,EnumGrowth.Small.getLocalizedName());
                    } else {
                        player.sendMessage("§4您这只精灵的体型已经是娇小了");
                    }
                    break;
                case 22:
                    if (!party.get(guiModifyGrowth.getParameter()).getGrowth().equals(EnumGrowth.Ordinary)) {
                        party.get(guiModifyGrowth.getParameter()).setGrowth(EnumGrowth.Ordinary);
                        takeItem(player, newItemStack,EnumGrowth.Ordinary.getLocalizedName());
                    } else {
                        player.sendMessage("§4您这只精灵的体型已经是普通了");
                    }
                    break;
                case 24:
                    if (!party.get(guiModifyGrowth.getParameter()).getGrowth().equals(EnumGrowth.Huge)) {
                        party.get(guiModifyGrowth.getParameter()).setGrowth(EnumGrowth.Huge);
                        takeItem(player, newItemStack,EnumGrowth.Huge.getLocalizedName());
                    } else {
                        player.sendMessage("§4您这只精灵的体型已经是高大了");
                    }
                    break;
                case 38:
                    if (!party.get(guiModifyGrowth.getParameter()).getGrowth().equals(EnumGrowth.Giant)) {
                        party.get(guiModifyGrowth.getParameter()).setGrowth(EnumGrowth.Giant);
                        takeItem(player, newItemStack,EnumGrowth.Giant.getLocalizedName());
                    } else {
                        player.sendMessage("§4您这只精灵的体型已经是巨人了");
                    }
                    break;
                case 40:
                    if (!party.get(guiModifyGrowth.getParameter()).getGrowth().equals(EnumGrowth.Enormous)) {
                        party.get(guiModifyGrowth.getParameter()).setGrowth(EnumGrowth.Enormous);
                        takeItem(player, newItemStack,EnumGrowth.Enormous.getLocalizedName());
                    } else {
                        player.sendMessage("§4您这只精灵的体型已经是庞大了");
                    }
                    break;
                case 42:
                    if (!party.get(guiModifyGrowth.getParameter()).getGrowth().equals(EnumGrowth.Ginormous)) {
                        party.get(guiModifyGrowth.getParameter()).setGrowth(EnumGrowth.Ginormous);
                        takeItem(player, newItemStack,EnumGrowth.Ginormous.getLocalizedName());
                    } else {
                        player.sendMessage("§4您这只精灵的体型已经是巨大了");
                    }
                    break;
                default:
                    // 如果点击的位置不是上述属性项，则不执行任何操作
                    return;
            }
            player.closeInventory();
        }

    }


    public void takeItem(Player player, ItemStack newItemStack,String growth) {
        HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
        if (removedItems.isEmpty()) {
            player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)).replaceAll("%growth%",growth));
        } else {
            player.sendMessage("你没有足够的物品");
        }
    }


}