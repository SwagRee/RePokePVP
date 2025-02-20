package io.github.swagree.pokecard.gui;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import org.bukkit.entity.*;
import org.bukkit.*;
import com.pixelmonmod.pixelmon.*;
import com.pixelmonmod.pixelmon.items.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import java.util.*;

public class GuiMain {

    private static final List<Integer> invSlot = Arrays.asList(11, 13, 15, 29, 31, 33);
    private static final String TITLE_PREFIX = "修改界面";

    public static void Gui(Player player, String guiName) {
        Inventory inv = Bukkit.createInventory(null, 45, "§b" + guiName + TITLE_PREFIX);
        UUID uuid = player.getUniqueId();
        PlayerPartyStorage pokemon = Pixelmon.storageManager.getParty(uuid);

        for (int i = 0; i < 6; i++) {
            int item = invSlot.get(i);
            SpriteInGui(pokemon, inv, i, item);
        }
        player.openInventory(inv);
    }

    private static void SpriteInGui(PlayerPartyStorage pokemon, Inventory inv, int playerSlot, int invSlot) {
        if (pokemon.get(playerSlot) == null || pokemon.get(playerSlot).isEgg()) {
            ItemStack kong = new ItemStack(Material.BARRIER, 1);
            ItemMeta itemMeta = kong.getItemMeta();
            if (pokemon.get(playerSlot) == null) {
                itemMeta.setDisplayName("§c无精灵");
            } else {
                itemMeta.setDisplayName("§蛋");
            }
            kong.setItemMeta(itemMeta);
            inv.setItem(invSlot, kong);
        } else {
            net.minecraft.item.ItemStack nmeitem = ItemPixelmonSprite.getPhoto(pokemon.get(playerSlot));
            ItemStack poke = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) (Object) nmeitem);
            ItemMeta pmeta = poke.getItemMeta();
            pmeta.setDisplayName("§a" + pokemon.get(playerSlot).getLocalizedName());
            List<String> list = new ArrayList<>();
            final int level = pokemon.get(playerSlot).getLevel();
            final boolean equalMale = pokemon.get(playerSlot).getGender().equals(Gender.Male);
            final int speed = pokemon.get(playerSlot).getIVs().getStat(StatsType.Speed);
            final int attach = pokemon.get(playerSlot).getIVs().getStat(StatsType.Attack);
            final int specialDefence = pokemon.get(playerSlot).getIVs().getStat(StatsType.SpecialDefence);
            final int specicalAttach = pokemon.get(playerSlot).getIVs().getStat(StatsType.SpecialAttack);
            final int defence = pokemon.get(playerSlot).getIVs().getStat(StatsType.Defence);
            final int hp = pokemon.get(playerSlot).getIVs().getStat(StatsType.HP);

            String gender = equalMale ? "男" : "女";
            list.add("§b等级：§f" + level);
            list.add("§b等级：§f" + gender);
            list.add("§b速度：§f" + speed);
            list.add("§b攻击：§f" + attach);
            list.add("§b特防：§f" + specialDefence);
            list.add("§b特攻：§f" + specicalAttach);
            list.add("§b防御：§f" + defence);
            list.add("§b血量：§f" + hp);
            pmeta.setLore(list);
            poke.setItemMeta(pmeta);
            inv.setItem(invSlot, poke);
        }
    }
}