package me.duckyprogrammer.welcomerewards.events;

import me.duckyprogrammer.welcomerewards.WelcomeRewards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Events implements Listener {

    private final Map<UUID, Set<UUID>> activeWelcomes = new ConcurrentHashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (WelcomeRewards.getInstance().isUpdateAvailable() && player.isOp()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bA new version of Welcome Rewards is available!"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDownload it at &a&nhttps://www.spigotmc.org/resources/welcome-rewards.109109/"));
        }
        if (player.hasPlayedBefore()) return;
        UUID playerUuid = player.getUniqueId();
        activeWelcomes.put(playerUuid, new HashSet<>());
        String welcomeMessage = WelcomeRewards.getInstance().getWelcomeMessage().replace("%player%", player.getName());
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', welcomeMessage));
        Bukkit.getScheduler().runTaskLater(WelcomeRewards.getInstance(), () -> activeWelcomes.remove(playerUuid), WelcomeRewards.getInstance().getWelcomingTime() * 20L);



    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (activeWelcomes.isEmpty()) return;
        String message = event.getMessage().toLowerCase();
        
        if (!message.contains("welcome")) return;

        Bukkit.getScheduler().runTask(WelcomeRewards.getInstance(), () -> {
            Player chatter = event.getPlayer();
            UUID chatterUuid = chatter.getUniqueId();
            boolean rewarded = false;

            for (Map.Entry<UUID, Set<UUID>> entry : activeWelcomes.entrySet()) {
                UUID newPlayerUuid = entry.getKey();
                Set<UUID> alreadyWelcomed = entry.getValue();
                
                if (chatterUuid.equals(newPlayerUuid)) continue;

                if (!alreadyWelcomed.contains(chatterUuid)) {
                    alreadyWelcomed.add(chatterUuid);
                    rewarded = true;
                }
            }

            if (rewarded) {
                List<String> commands = WelcomeRewards.getInstance().getWelcomeCommands();
                commands.forEach(command -> {
                    String formattedCmd = command.replace("%player%", chatter.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCmd);
                });

                String rewardedMsg = WelcomeRewards.getInstance().getWelcomedMessage();
                chatter.sendMessage(ChatColor.translateAlternateColorCodes('&', rewardedMsg));
            }
        });
    }

}
