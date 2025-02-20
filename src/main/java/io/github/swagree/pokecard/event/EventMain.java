package io.github.swagree.pokecard;

import static org.bukkit.ChatColor.*;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.swagree.pokecard.enums.EnumCardName;
import io.github.swagree.pokecard.gui.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Event implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (event.getHand() == EquipmentSlot.HAND) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
                    ArrayList<String> cardNames = new ArrayList<>();
                    for (EnumCardName data : EnumCardName.values()) {
                        cardNames.add(data.getCardName());
                    }
                    for (String cardName : cardNames) {

                        String string = ItemUtil.metaIgnoreLoreColor(cardName);
                        final List<String> newItemLore = itemStack.getItemMeta().getLore();

                        List<String> newItemLoreWithoutColor = new ArrayList<>();

                        for (String line : newItemLore) {
                            newItemLoreWithoutColor.add(stripColor(line));
                        }
                        if (string != null && newItemLoreWithoutColor.toString().equals(string)) {
                            if (cardName != null) {
                                GuiMain.Gui(player, EnumCardName.getValueByKey(cardName));
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerPartyStorage party = Pixelmon.storageManager.getParty(player.getUniqueId());
        int slot = event.getRawSlot();
        String title = event.getView().getTitle();

        String cardName = null;

        for (EnumCardName guiTitle : EnumCardName.values()) {
            if (title.contains(guiTitle.getCardNameCN())) {
                event.setCancelled(true);
                if (title.contains(guiTitle.getCardNameCN())) {
                    cardName = guiTitle.getCardName();
                    List<Integer> invSlots = Arrays.asList(11, 13, 15, 29, 31, 33);

                    for (int i = 0; i < invSlots.size(); i++) {
                        int itemSlot = invSlots.get(i);
                        modifySprite(party, player, slot, itemSlot, i, cardName);
                    }
                    break;
                }
            }
        }
    }


    public void modifySprite(PlayerPartyStorage party, Player player, int slot, int invSlot, int spriteSlot, String cardName) {
        if (slot == invSlot) {
            if (party.get(spriteSlot) == null)
                return;

            ItemStack newItemStack = ItemUtil.createItem(cardName);

            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack item = player.getInventory().getItem(i);
                if (item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
                    List<String> itemLore = item.getItemMeta().getLore();
                    List<String> newItemLore = new ArrayList<>(newItemStack.getItemMeta().getLore());

                    List<String> itemLoreWithoutColor = new ArrayList<>();
                    List<String> newItemLoreWithoutColor = new ArrayList<>();
                    for (String line : itemLore) {
                        itemLoreWithoutColor.add(ItemUtil.stripColorCodes(line));
                    }
                    for (String line : newItemLore) {
                        newItemLoreWithoutColor.add(ItemUtil.stripColorCodes(line));
                    }


                    if (itemLoreWithoutColor.equals(newItemLoreWithoutColor)) {
                        switch (cardName) {
                            case "mt":
                                if (party.get(spriteSlot).getAbilitySlot() != 2) {
                                    party.get(spriteSlot).setAbilitySlot(2);
                                    if (party.get(spriteSlot).getAbilitySlot() == 2) {
                                        HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                        if (removedItems.isEmpty()) {
                                            player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)));
                                        } else {
                                            player.sendMessage("你没有足够的物品");
                                        }
                                    } else {
                                        player.sendMessage("§4该精灵不存在梦特!!!");
                                    }
                                } else {
                                    player.sendMessage("§4您这只精灵已经是梦特了！");
                                }
                                player.closeInventory();
                                break;
                            case "shiny":
                                if (!party.get(spriteSlot).isShiny()) {
                                    party.get(spriteSlot).setShiny(true);
                                    HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                    if (removedItems.isEmpty()) {
                                        player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)));
                                    } else {
                                        player.sendMessage("你没有足够的物品");
                                    }
                                } else {
                                    player.sendMessage("§4这只精灵已经是闪光了!!!");
                                }
                                player.closeInventory();


                                break;
                            case "maxLevel":
                                if (party.get(spriteSlot).getLevel() != 100) {
                                    party.get(spriteSlot).setLevel(100);
                                    HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                    if (removedItems.isEmpty()) {
                                        player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)));
                                    } else {
                                        player.sendMessage("你没有足够的物品");
                                    }
                                } else {
                                    player.sendMessage("§4这只精灵已经是满级了!!!");
                                }
                                player.closeInventory();
                                break;
                            case "clearLevel":
                                if (party.get(spriteSlot).getLevel() != 1) {
                                    party.get(spriteSlot).setLevel(1);
                                    HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                    if (removedItems.isEmpty()) {
                                        player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)));
                                    } else {
                                        player.sendMessage("你没有足够的物品");
                                    }
                                } else {
                                    player.sendMessage("§4这只精灵已经是1级了!不能清零了");
                                }
                                player.closeInventory();
                                break;
                            case "clearEvs":
                                if (party.get(spriteSlot).getEVs().getStat(StatsType.Speed) != 0 ||
                                        party.get(spriteSlot).getEVs().getStat(StatsType.Attack) != 0 ||
                                        party.get(spriteSlot).getEVs().getStat(StatsType.SpecialDefence) != 0 ||
                                        party.get(spriteSlot).getEVs().getStat(StatsType.SpecialAttack) != 0 ||
                                        party.get(spriteSlot).getEVs().getStat(StatsType.Defence) != 0 ||
                                        party.get(spriteSlot).getEVs().getStat(StatsType.HP) != 0
                                ) {
                                    party.get(spriteSlot).getEVs().setStat(StatsType.Speed, 0);
                                    party.get(spriteSlot).getEVs().setStat(StatsType.Attack, 0);
                                    party.get(spriteSlot).getEVs().setStat(StatsType.SpecialDefence, 0);
                                    party.get(spriteSlot).getEVs().setStat(StatsType.SpecialAttack, 0);
                                    party.get(spriteSlot).getEVs().setStat(StatsType.Defence, 0);
                                    party.get(spriteSlot).getEVs().setStat(StatsType.HP, 0);
                                    HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                    if (removedItems.isEmpty()) {
                                        player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)));
                                    } else {
                                        player.sendMessage("你没有足够的物品");
                                    }
                                } else {
                                    player.sendMessage("§4这只精灵没有一项有努力值！！！");
                                }
                                player.closeInventory();
                                break;
                            case "maxIvs":
                                if (!(party.get(spriteSlot).getIVs().getStat(StatsType.Speed) == 31 &&
                                        party.get(spriteSlot).getIVs().getStat(StatsType.Attack) == 31 &&
                                        party.get(spriteSlot).getIVs().getStat(StatsType.SpecialDefence) == 31 &&
                                        party.get(spriteSlot).getIVs().getStat(StatsType.SpecialAttack) == 31 &&
                                        party.get(spriteSlot).getIVs().getStat(StatsType.Defence) == 31 &&
                                        party.get(spriteSlot).getIVs().getStat(StatsType.HP) == 31)
                                ) {
                                    HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                    if (removedItems.isEmpty()) {
                                        player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)));
                                        party.get(spriteSlot).getIVs().setStat(StatsType.Speed, 31);
                                        party.get(spriteSlot).getIVs().setStat(StatsType.Attack, 31);
                                        party.get(spriteSlot).getIVs().setStat(StatsType.SpecialDefence, 31);
                                        party.get(spriteSlot).getIVs().setStat(StatsType.SpecialAttack, 31);
                                        party.get(spriteSlot).getIVs().setStat(StatsType.Defence, 31);
                                        party.get(spriteSlot).getIVs().setStat(StatsType.HP, 31);
                                    } else {
                                        player.sendMessage("你没有足够的物品");
                                    }
                                } else {
                                    player.sendMessage("§4这只精灵已经是6v了！！！");
                                }
                                player.closeInventory();
                                break;
                            case "gender":
                                if (party.get(spriteSlot).getGender().equals(Gender.Male)) {
                                    party.get(spriteSlot).setGender(Gender.Female);
                                    HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                    if (removedItems.isEmpty()) {
                                        player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName + "ToFemale")));
                                    } else {
                                        player.sendMessage("你没有足够的物品");
                                    }
                                } else if (party.get(spriteSlot).getGender().equals(Gender.Female)) {
                                    party.get(spriteSlot).setGender(Gender.Male);
                                    HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                    if (removedItems.isEmpty()) {
                                        player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName + "ToMale")));
                                    } else {
                                        player.sendMessage("你没有足够的物品");
                                    }
                                } else {
                                    player.sendMessage("§b您这只宝可梦没有性别,无法修改");
                                }
                                player.closeInventory();
                                break;
                            case "anyIvs":
                                GuiModifyIvs.Gui(player, spriteSlot);
                                break;
                            case "anyEvs":
                                GuiModifyEvs.Gui(player, spriteSlot);
                                break;
                            case "anyGrowth":
                                GuiModifyGrowth.Gui(player, spriteSlot);
                                break;
                            case "anyNature":
                                GuiModifyNature.Gui(player, spriteSlot);
                                break;
                            case "rdNature":
                                EnumNature randomNature = EnumNature.getRandomNature();
                                party.get(spriteSlot).setNature(randomNature);
                                HashMap<Integer, ItemStack> removedItemss = player.getInventory().removeItem(newItemStack);
                                if (removedItemss.isEmpty()) {
                                    player.sendMessage("随机性格成功，您的"+party.get(spriteSlot).getLocalizedName()+"性格变成了"+randomNature.getLocalizedName());
                                } else {
                                    player.sendMessage("你没有足够的物品");
                                }
                                break;
                            case "form":
                                GuiModifyForm.Gui(player, spriteSlot);
                                break;
                            case "bind":
                                if (party.get(spriteSlot).hasSpecFlag("untradeable")) {
                                    player.sendMessage("§b他已经绑定了哦");
                                    return;
                                } else {
                                    HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                    if (removedItems.isEmpty()) {
                                        player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)));
                                        party.get(spriteSlot).addSpecFlag("untradeable");
                                    } else {
                                        player.sendMessage("你没有足够的物品");
                                    }
                                }
                                player.closeInventory();
                                break;
                            case "unbind":
                                if (party.get(spriteSlot).hasSpecFlag("")) {
                                    player.sendMessage("§b他无需要解绑了哦");
                                    return;
                                } else {
                                    HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(newItemStack);
                                    if (removedItems.isEmpty()) {
                                        player.sendMessage(translateAlternateColorCodes('&', Main.plugin.getConfig().getString("Message." + cardName)));
                                        party.get(spriteSlot).removeSpecFlag("untradeable");
                                    } else {
                                        player.sendMessage("你没有足够的物品");
                                    }
                                }
                                player.closeInventory();
                                break;

                        }
                        break;
                    }
                }
            }
        }
    }



    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickPokemonInfo(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerPartyStorage party = Pixelmon.storageManager.getParty(player.getUniqueId());
        int slot = event.getRawSlot();
        String title = event.getView().getTitle();
        String guiTitle = Main.plugin.getConfig().getString("guiTitle");
        if (title.contains(guiTitle)) {
            List<Integer> invSlots = Arrays.asList(10, 13, 16, 28, 31, 34);

            for (int i = 0; i < invSlots.size(); i++) {
                int invSlot = invSlots.get(i);
                int spriteSlot = getSpriteSlot(invSlot);

                if (spriteSlot == -1) {
                    continue;
                }

                if (slot == invSlot) {
                    if (!isTrade(player, spriteSlot)) {
                        event.setCancelled(true); // 取消事件
                        player.closeInventory();
                        player.sendMessage("您这只精灵绑定了，不能转换！");
                    }
                }

            }

        }
    }

    public boolean isTrade(Player player, int spriteSlot) {
        PlayerPartyStorage party = Pixelmon.storageManager.getParty(player.getUniqueId());

        if (party.get(spriteSlot) == null) {
            return false;
        }

        if (party.get(spriteSlot).hasSpecFlag("untradeable")) {
            return false;
        }

        return true;
    }

    // 根据背包宝可梦的位置获取对应的精灵槽位编号
    public int getSpriteSlot(int invSlot) {
        switch (invSlot) {
            case 10:
                return 0;
            case 13:
                return 1;
            case 16:
                return 2;
            case 28:
                return 3;
            case 31:
                return 4;
            case 34:
                return 5;
            default:
                return -1; // 表示无效的位置
        }
    }
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String command1 = event.getMessage().split(" ")[0].substring(1); // 获取玩家输入的指令
        if (command1.equalsIgnoreCase("pokeegg") && event.getMessage().contains("create")) { // 目标指令名
            for(int i = 1 ;i<=6 ; i++){
                if(event.getMessage().contains(String.valueOf(i))){
                    PlayerPartyStorage party = Pixelmon.storageManager.getParty(event.getPlayer().getUniqueId());
                    if(party.get(i-1).hasSpecFlag("untradeable")){
                        event.setCancelled(true);
                        event.getPlayer().sendMessage("对不起 这只精灵已经绑定了！");
                    }
                }
            }
        }
    }
}