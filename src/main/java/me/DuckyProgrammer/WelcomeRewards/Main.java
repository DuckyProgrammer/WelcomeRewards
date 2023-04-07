package me.DuckyProgrammer.WelcomeRewards;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends JavaPlugin {


    public static String welcomeMessage;
    public static int welcomingTime;
    public static String welcomeCommand;
    public static String welcomedMessage;
    public static boolean updateAvailable;
    public void onEnable() {
        int pluginId = 18158;
        Metrics metrics = new Metrics(this, pluginId);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        updateAvailable = checkUpdate();
        welcomeMessage = getConfig().getString("welcome-message");
        welcomingTime = getConfig().getInt("welcoming-time");
        welcomeCommand = getConfig().getString("welcome-command");
        welcomedMessage = getConfig().getString("welcomed-message");
        getServer().getPluginManager().registerEvents(new Events(), this);
        if (updateAvailable) {
            getLogger().warning("A new version of SignEditor is available!");
            getLogger().warning("Download it at https://api.spiget.org/v2/resources/109109/versions/latest");
            for (Player ops : Bukkit.getOnlinePlayers()) {
                if (ops.isOp()) {
                    ops.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bA new version of Sign Editor is available!"));
                    ops.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDownload it at &a&nhttps://www.spigotmc.org/resources/welcome-rewards.109109/"));
                }
            }
        }
        getLogger().info("WelcomeRewards has been enabled!");
    }

    public boolean checkUpdate() {
        try {
            URL url = new URL("https://api.spiget.org/v2/resources/109109/versions/latest");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            JSONObject obj = (JSONObject) JSONValue.parse(content.toString());
            String version = (String) obj.get("name");
            if (version.equals(getDescription().getVersion())) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
