package me.DuckyProgrammer.WelcomeRewards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class Events implements Listener {
    private static boolean welcomingTime = false;
    private static ArrayList<Player> welcomers = new ArrayList<>();
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPlayedBefore()) {
            return;
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.welcomeMessage.replace("%player%", player.getName())));
        welcomingTime = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
            welcomingTime = false;
            welcomers.remove(event.getPlayer());
        }, Main.welcomingTime * 20L);
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (welcomingTime) {
            if (event.getMessage().contains("welcome") || event.getMessage().contains("Welcome")) {
                if (welcomers.contains(event.getPlayer())) {
                    return;
                }
                welcomers.add(event.getPlayer());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.welcomeCommand.replace("%player%", event.getPlayer().getName()));
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Main.welcomedMessage));
            }
        }
    }
}
