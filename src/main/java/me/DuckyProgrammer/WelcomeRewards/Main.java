package me.DuckyProgrammer.WelcomeRewards;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {


    public static String welcomeMessage;
    public static int welcomingTime;
    public static String welcomeCommand;
    public static String welcomedMessage;
    public void onEnable() {
        int pluginId = 18158;
        Metrics metrics = new Metrics(this, pluginId);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        welcomeMessage = getConfig().getString("welcome-message");
        welcomingTime = getConfig().getInt("welcoming-time");
        welcomeCommand = getConfig().getString("welcome-command");
        welcomedMessage = getConfig().getString("welcomed-message");
        getServer().getPluginManager().registerEvents(new Events(), this);
    }
}
