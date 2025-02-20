package io.github.swagree.repokepvp;


import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class Main extends JavaPlugin {

    public static Main plugin;
    public void onEnable() {
        plugin = this;
        this.getCommand("rpp").setExecutor(new CommandBind());
        Bukkit.getConsoleSender().sendMessage("§7[RePokePVP] §b作者§fSwagRee §cQQ:§f352208610");
        Bukkit.getPluginManager().registerEvents(new PokemonEventListener(), (Plugin)this);
        this.saveDefaultConfig();
        this.reloadConfig();
    }

    public void onDisable() {
    }
}
