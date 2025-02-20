package io.github.swagree.repokepvp;

import catserver.api.bukkit.event.ForgeEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class PokemonEventListener implements Listener {

    @EventHandler
    public void onBattleStarted(ForgeEvent event) {
        if (event.getForgeEvent() instanceof BattleEndEvent) {
            handleBattleEnd((BattleEndEvent) event.getForgeEvent());
        }
    }

    private void handleBattleEnd(BattleEndEvent forgeEvent) {
        List<PlayerParticipant> winners = new ArrayList<>();
        List<PlayerParticipant> losers = new ArrayList<>();

        // 遍历所有战斗参与者
        for (BattleParticipant participant : forgeEvent.bc.participants) {
            BattleResults result = forgeEvent.results.get(participant);

            if (participant instanceof PlayerParticipant) {
                PlayerParticipant playerParticipant = (PlayerParticipant) participant;

                if (result == BattleResults.VICTORY) {
                    winners.add(playerParticipant);
                } else if (isDefeatResult(result)) {
                    losers.add(playerParticipant);
                }
            }
        }

        // 执行对应命令
        if (!winners.isEmpty()) {
            executeCommands(winners, "WinCommand");
        }
        if (!losers.isEmpty()) {
            executeCommands(losers, "LoseCommand");
        }
    }

    private boolean isDefeatResult(BattleResults result) {
        return result == BattleResults.DEFEAT ||
                result == BattleResults.DRAW ||
                result == BattleResults.FLEE;
    }

    private void executeCommands(List<PlayerParticipant> participants, String commandType) {
        List<String> commands = Main.plugin.getConfig().getStringList(commandType);
        for (PlayerParticipant participant : participants) {
            String playerName = participant.player.getName();
            if (!playerName.isEmpty()) {
                for (String command : commands) {
                    Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            command.replace("%player%", playerName)
                    );
                }
            }
        }
    }
}