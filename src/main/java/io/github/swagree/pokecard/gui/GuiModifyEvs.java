package io.github.swagree.pokecard.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.swagree.pokecard.Event;
import io.github.swagree.pokecard.ItemUtil;
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

public class GuiModifyEvs implements Listener {
    private static final List<Integer> invSlot = Arrays.asList(11, 13, 15, 29, 31, 33);
    private static final String TITLE_PREFIX = "§b选择一项努力值修改吧";
    private static final String cardName = "anyEvs";

    private static GuiModifyEvs guiModifyIvs; // 声明为静态变量
    private int myParameter;

    public void setParameter(int value) {
        this.myParameter = value;
    }

    public int getParameter() {
        return myParameter;
    }


    public static void Gui(Player player, int slot) {
        guiModifyIvs = new GuiModifyEvs();

        Inventory inv = Bukkit.createInventory(null, 45, TITLE_PREFIX);

        final ArrayList<ItemStack> itemStacks = new ArrayList<>();

        // 属性名称和对应的lore值
        HashMap<String, String> attributeLoreMap = new LinkedHashMap<>();
        attributeLoreMap.put("速度", "§b点击就可以修改速度努力值为252了");
        attributeLoreMap.put("攻击", "§b点击就可以修改攻击努力值为252了");
        attributeLoreMap.put("特防", "§b点击就可以修改特防努力值为252了");
        attributeLoreMap.put("特攻", "§b点击就可以修改特攻努力值为252了");
        attributeLoreMap.put("防御", "§b点击就可以修改防御努力值为252了");
        attributeLoreMap.put("生命值", "§b点击就可以修改生命值努力值为252了");

        for (Map.Entry<String, String> entry : attributeLoreMap.entrySet()) {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§e修改" + entry.getKey() + "努力值为252了");

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

        guiModifyIvs.setParameter(slot);
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
                case 11: // 速度
                    party.get(guiModifyIvs.getParameter()).getIVs().setStat(StatsType.Speed, 31);
                    if (party.get(guiModifyIvs.getParameter()).getIVs().getStat(StatsType.Speed) == 31) {
                        ItemUtil.takeItem(player,newItemStack,cardName);
                    }else{
                        player.sendMessage("§4您这只精灵的速度努力值已经是252了");
                    }
                    break;
                case 13: // 攻击'
                    party.get(guiModifyIvs.getParameter()).getIVs().setStat(StatsType.Attack, 31);
                    if (party.get(guiModifyIvs.getParameter()).getIVs().getStat(StatsType.Attack) == 31) {
                        ItemUtil.takeItem(player,newItemStack,cardName);
                    }else{
                        player.sendMessage("§4您这只精灵的速度努力值已经是252了");
                    }
                    break;
                case 15: // 特防
                    party.get(guiModifyIvs.getParameter()).getIVs().setStat(StatsType.SpecialDefence, 31);
                    if (party.get(guiModifyIvs.getParameter()).getIVs().getStat(StatsType.SpecialDefence) == 31) {
                        ItemUtil.takeItem(player,newItemStack,cardName);
                    }else{
                        player.sendMessage("§4您这只精灵的特防努力值已经是252了");
                    }
                    break;
                case 29: // 特攻
                    party.get(guiModifyIvs.getParameter()).getIVs().setStat(StatsType.SpecialAttack, 31);
                    if (party.get(guiModifyIvs.getParameter()).getIVs().getStat(StatsType.SpecialAttack) == 31) {
                        ItemUtil.takeItem(player,newItemStack,cardName);
                    }else{
                        player.sendMessage("§4您这只精灵的特攻努力值已经是252了");
                    }
                    break;
                case 31: // 防御
                    party.get(guiModifyIvs.getParameter()).getIVs().setStat(StatsType.Defence, 31);
                    if (party.get(guiModifyIvs.getParameter()).getIVs().getStat(StatsType.Defence) == 31) {
                        ItemUtil.takeItem(player,newItemStack,cardName);
                    }else{
                        player.sendMessage("§4您这只精灵的防御努力值已经是252了");
                    }
                    break;
                case 33: // 生命值
                    party.get(guiModifyIvs.getParameter()).getIVs().setStat(StatsType.HP, 31);
                    if (party.get(guiModifyIvs.getParameter()).getIVs().getStat(StatsType.HP) == 31) {
                        ItemUtil.takeItem(player,newItemStack,cardName);
                    }else{
                        player.sendMessage("§4您这只精灵的生命值努力值已经是252了");
                    }
                    break;
                default:
                    // 如果点击的位置不是上述属性项，则不执行任何操作
                    return;
            }
            player.closeInventory();
        }

    }



}