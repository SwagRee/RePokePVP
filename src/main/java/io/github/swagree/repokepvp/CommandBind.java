package io.github.swagree.repokepvp;

// 原有import保持不变
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.PixelmonPlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

public class CommandBind implements CommandExecutor {
    // 新增队列系统和冷却机制
    private static final Queue<Player> battleQueue = new LinkedList<>();
    private static final Set<UUID> cooldownPlayers = new HashSet<>();
    private static final int QUEUE_DELAY = 3 * 20; // 3秒（20 ticks/秒）

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) return false;

        if (args[0].equalsIgnoreCase("join")) {
            handleJoinCommand((Player) sender);
            return true;
        }
        // 原有reload逻辑保持不变
        return false;
    }

    private void handleJoinCommand(Player player) {

        // 防止重复加入和冷却期
        if (battleQueue.contains(player)) {
            player.sendMessage("§e你已经在匹配队列中！");
            return;
        }
        if (cooldownPlayers.contains(player.getUniqueId())) {
            player.sendMessage("§c你正处于冷却期，请稍后再试！");
            return;
        }

        // 加入队列
        battleQueue.offer(player);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage("§e"+player.getName()+"§a已加入匹配队列，当前排队人数: " + battleQueue.size());
        }

        // 检查队列状态
        checkQueue();
    }

    private void checkQueue() {
        if (battleQueue.size() < 2) return;

        // 获取匹配玩家
        Player[] players = new Player[2];
        players[0] = battleQueue.poll();
        players[1] = battleQueue.poll();

        // 发送通知
        Arrays.stream(players).forEach(p ->
                p.sendMessage("§b匹配成功！战斗将在3秒后开始..."));

        Arrays.stream(players).forEach(p ->
                Pixelmon.storageManager.getParty(p.getUniqueId()).heal());
        // 延迟执行战斗
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    startBattle(players[0], players[1]);
                    // 添加30秒冷却
                    cooldownPlayers.addAll(Arrays.asList(
                            players[0].getUniqueId(),
                            players[1].getUniqueId()
                    ));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cooldownPlayers.removeAll(Arrays.asList(
                                    players[0].getUniqueId(),
                                    players[1].getUniqueId()
                            ));
                        }
                    }.runTaskLater(Main.plugin, 10 * 20); // 10秒冷却
                } catch (Exception e) {
                    Arrays.stream(players).forEach(p ->
                            p.sendMessage("§c战斗初始化失败，请确保携带可用宝可梦！"));
                }
            }
        }.runTaskLater(Main.plugin, QUEUE_DELAY);
    }

    private void startBattle(Player p1, Player p2) throws Exception {
        // 转换玩家实体
        EntityPlayerMP ep1 = PixelmonPlayerUtils.getUniquePlayerStartingWith(p1.getName());
        EntityPlayerMP ep2 = PixelmonPlayerUtils.getUniquePlayerStartingWith(p2.getName());
        // 获取宝可梦
        PlayerPartyStorage party1 = Pixelmon.storageManager.getParty(ep1);
        PlayerPartyStorage party2 = Pixelmon.storageManager.getParty(ep2);

        EntityPixelmon pokemon1 = party1.getAndSendOutFirstAblePokemon(ep1);
        EntityPixelmon pokemon2 = party2.getAndSendOutFirstAblePokemon(ep2);

        // 创建战斗参与者
        PlayerParticipant pp1 = new PlayerParticipant(ep1, pokemon1);
        PlayerParticipant pp2 = new PlayerParticipant(ep2, pokemon2);

        // 配置战斗规则
        BattleRules rules = new BattleRules();
        rules.battleType = EnumBattleType.Single;

        // 开始战斗
        BattleRegistry.startBattle(
                new BattleParticipant[]{pp1},
                new BattleParticipant[]{pp2},
                rules
        );
    }
}