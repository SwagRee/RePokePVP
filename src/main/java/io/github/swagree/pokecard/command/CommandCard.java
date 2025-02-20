package io.github.swagree.pokecard;

import io.github.swagree.pokecard.gui.GuiModifyIvs;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandCard implements CommandExecutor {

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command1, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("&b<&m*-----=======&c热&e宝可梦卡&b &m=======-----*&b>");
            sender.sendMessage("§e/rpc 玩家 [类型] 数量 §f- 给予指定的宝可梦卡");
            sender.sendMessage("§e/rpc list §f- 查看卡的类型");
            sender.sendMessage("§e/rpc reload §f- 重载插件");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Main.plugin.reloadConfig();
            sender.sendMessage("§b插件已完成重载");
        }
        if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage("&b<&m*-----=======&c热&e宝可梦卡&b &m=======-----*&b>");
            sender.sendMessage(
                            "§bmt:" + "§f梦特卡" + "\n" +
                            "§bshiny:" + "§f闪光卡" + "\n" +
                            "§bmaxLevel:" + "§f满级卡" + "\n" +
                            "§bclearLevel:" + "§f等级清零卡" + "\n" +
                            "§bgender:" + "§f性别卡" + "\n" +
                            "§bclearEvs:" + "§f努力值清零卡" + "\n" +
                            "§bmaxIvs:" + "§f个体6V卡" + "\n" +
                            "§banyIvs:" + "§f指定个体V卡" + "\n" +
                            "§banyEvs:" + "§f指定努力值为满卡" + "\n" +
                            "§banyGrowth:" + "§f自选体型卡" + "\n" +
                            "§banynature:" + "§f指定性格卡" + "\n" +
                            "§brdnature:" + "§f随机性格卡" + "\n" +
                            "§bbind:" + "§f绑定卡" + "\n" +
                            "§bunbind:" + "§f解绑卡" + "\n"

            );
        }
        if (args.length == 3) {
            if (!sender.isOp()) {
                sender.sendMessage("§bRePokeCard: §f你并非op，无权使用！");
                return true;
            }
            String playername = args[0];
            String cardName = args[1];
            Player p = Bukkit.getPlayer(playername);
            int num = Integer.parseInt(args[2]);
            if (p == null) {
                sender.sendMessage("§6RePokeCard : §f玩家不在线或不存在！");
                return true;
            }
            try {
                p.getInventory().addItem(new ItemStack[]{ItemUtil.item_ChangeItemName(num, cardName)});
            } catch (Exception e) {
                sender.sendMessage("§6RePokeCard : §c类型或者数量似乎不正确，请检查您的指令");
            }
            return true;
        }
        return true;
    }
}

